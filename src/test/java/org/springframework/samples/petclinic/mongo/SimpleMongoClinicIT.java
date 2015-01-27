package org.springframework.samples.petclinic.mongo;

import java.util.Collection;
import java.util.Iterator;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.AbstractClinicTests;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@DirtiesContext
public class SimpleMongoClinicIT extends AbstractClinicTests {

    @Autowired
    protected Clinic clinic;

    private static final Logger logger = LoggerFactory.getLogger(SimpleMongoClinicIT.class);

    @Test
    public void testMongoClinic() {

        // loads test data
        storeTestOwners(clinic);

        Collection<Vet> vets = clinic.getVets();
        Iterator<Vet> itVet = vets.iterator();
        while (itVet.hasNext()) {
            Vet vet = itVet.next();
            logger.info("Vet : [ " + vet.getFirstName() + " " + vet.getLastName() + "]");
        }

        Collection<PetType> petTypes = clinic.getPetTypes();
        Iterator<PetType> itPetType = petTypes.iterator();
        while (itPetType.hasNext()) {
            PetType petType = itPetType.next();
            logger.info("PetType : [ " + petType.getName() + "]");
        }

        Collection<Owner> petOwners = clinic.findOwners(null);
        Iterator<Owner> itOwner = petOwners.iterator();
        while (itOwner.hasNext()) {
            Owner owner = itOwner.next();
            logger.info("Owner : [ " + owner.getFirstName() + " " + owner.getLastName() + "]");
        }
    }

    private static void storeTestOwners(Clinic clinic) {
        Owner owner1 = new Owner();
        owner1.setId(1);
        owner1.setAddress("Address 1");
        owner1.setCity("City 1");
        owner1.setFirstName("Name 1");
        owner1.setLastName("Last Name 1");
        owner1.setTelephone("Telephone 1");

        clinic.storeOwner(owner1);

        Owner owner2 = new Owner();
        owner2.setId(2);
        owner2.setAddress("Address 2");
        owner2.setCity("City 2");
        owner2.setFirstName("Name 2");
        owner2.setLastName("Last Name 2");
        owner2.setTelephone("Telephone 2");

        clinic.storeOwner(owner2);
    }
}
