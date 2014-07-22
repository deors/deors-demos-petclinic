package org.springframework.samples.petclinic.mongo;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Vet;

public class SimpleMongoClinicTest {

	public static void main(String[] args) {
		SimpleMongoClinic smc = new SimpleMongoClinic();
		smc.init();
		
		storeTestOwners(smc);
		
		
		Collection<Vet> vets = smc.getVets();
		Iterator<Vet> itVet = vets.iterator();
		while (itVet.hasNext()) {
			Vet vet = itVet.next();
			System.out.println("Vet : [ " + vet.getFirstName() + " " + vet.getLastName() + "]");
		}

		Collection<PetType> petTypes = smc.getPetTypes();
		Iterator<PetType> itPetType = petTypes.iterator();
		while (itPetType.hasNext()) {
			PetType petType = itPetType.next();
			System.out.println("PetType : [ " + petType.getName() + "]");
		}
		
		Collection<Owner> petOwners = smc.findOwners(null);
		Iterator<Owner> itOwner = petOwners.iterator();
		while (itOwner.hasNext()) {
			Owner owner = itOwner.next();
			System.out.println("Owner : [ " + owner.getFirstName() + " " + owner.getLastName() + "]");
		}
	}

	private static void storeTestOwners(SimpleMongoClinic smc) {
		Owner owner1 = new Owner();
		owner1.setId(1);
		owner1.setAddress("Address 1");
		owner1.setCity("City 1");
		owner1.setFirstName("Name 1");
		owner1.setLastName("Last Name 1");
		owner1.setTelephone("Telephone 1");
		
		smc.storeOwner(owner1);
		
		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setAddress("Address 2");
		owner2.setCity("City 2");
		owner2.setFirstName("Name 2");
		owner2.setLastName("Last Name 2");
		owner2.setTelephone("Telephone 2");
		
		smc.storeOwner(owner2);
	}

}
