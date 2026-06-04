package org.springframework.samples.petclinic.jpa;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * JPA implementation of the Clinic interface using EntityManager.
 *
 * <p>The mappings are defined in "orm.xml" located in the META-INF directory.
 *
 * @author Mike Keith
 * @author Rod Johnson
 * @author Sam Brannen
 * @since 22.4.2006
 */
@Repository
@Transactional
public class EntityManagerClinic implements Clinic {

    @PersistenceContext
    private EntityManager em;


    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Collection<Vet> getVets() {
        return this.em.createQuery("SELECT vet FROM Vet vet ORDER BY vet.lastName, vet.firstName").getResultList();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Collection<PetType> getPetTypes() {
        return this.em.createQuery("SELECT ptype FROM PetType ptype ORDER BY ptype.name").getResultList();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public Collection<Owner> findOwners(String lastName) {
        Query query = this.em.createQuery("SELECT owner FROM Owner owner WHERE owner.lastName LIKE :lastName");
        query.setParameter("lastName", lastName + "%");
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    public Owner loadOwner(int id) {
        Owner owner = this.em.find(Owner.class, id);
        if (owner == null) {
            throw new ObjectRetrievalFailureException(Owner.class, id);
        }
        return owner;
    }

    @Transactional(readOnly = true)
    public Pet loadPet(int id) {
        Pet pet = this.em.find(Pet.class, id);
        if (pet == null) {
            throw new ObjectRetrievalFailureException(Pet.class, id);
        }
        return pet;
    }

    public void storeOwner(Owner owner) {
        if (owner.isNew()) {
            this.em.persist(owner);
        }
        else if (!this.em.contains(owner)) {
            this.em.merge(owner);
        }
        this.em.flush();
    }

    public void storePet(Pet pet) {
        if (pet.isNew()) {
            this.em.persist(pet);
        }
        else if (!this.em.contains(pet)) {
            this.em.merge(pet);
        }
        this.em.flush();
    }

    public void storeVisit(Visit visit) {
        if (visit.isNew()) {
            this.em.persist(visit);
        }
        else if (!this.em.contains(visit)) {
            this.em.merge(visit);
        }
        this.em.flush();
    }

    public void deletePet(int id) throws DataAccessException {
        Pet pet = loadPet(id);
        Owner owner = pet.getOwner();
        if (owner != null) {
            owner.getPetsInternal().remove(pet);
        }
        this.em.remove(pet);
        this.em.flush();
    }

    @Transactional(readOnly = true)
    public Visit loadVisit(int id) {
        Visit visit = this.em.find(Visit.class, id);
        if (visit == null) {
            throw new ObjectRetrievalFailureException(Visit.class, id);
        }
        return visit;
    }

    @Override
    public void deleteVisit(int id) throws DataAccessException {
        Visit visit = loadVisit(id);
        this.em.remove(visit);
        this.em.flush();
    }

    @Override
    public void deleteOwner(int id) throws DataAccessException {
        Owner owner = loadOwner(id);
        this.em.remove(owner);
        this.em.flush();
    }
}
