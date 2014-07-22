package org.springframework.samples.petclinic.mongo;

import java.awt.image.DataBufferByte;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

@Service
//@ManagedResource("petclinic:type=Clinic")
public class SimpleMongoClinic implements Clinic {

	DB database;
	
	@Autowired
	public void init() {
		try {
			MongoClient mongoClient = new MongoClient("localhost" , 27017);
			database = mongoClient.getDB("petClinic");
			System.out.println("MongoDB connection initialized.");
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
		}
		
		Set<String> collectionNames = database.getCollectionNames();
		Iterator<String> it = collectionNames.iterator();

		System.out.println("Collections found:");
		while (it.hasNext()) {
			System.out.println(" - " + it.next() + ".");
		}
	}
	
	public Collection<Vet> getVets() throws DataAccessException {
		
		List<Vet> returnVets = new ArrayList<Vet>(); 
		
		DBCollection dbVets = database.getCollection("vet");
		DBCollection dbVetSpecialties = database.getCollection("vetSpecialty");
		DBCollection dbSpecialties = database.getCollection("specialty");
		DBCursor cursor = dbVets.find();
		DBObject actual;
		Vet actualVet;
		while (cursor.hasNext()) {
			actual = cursor.next();
			actualVet = new Vet();
			actualVet.setId(((Double)actual.get("_id")).intValue());
			actualVet.setFirstName((String)actual.get("firstName"));
			actualVet.setLastName((String)actual.get("lastName"));
			
			
			BasicDBObject query = new BasicDBObject("vetId", actualVet.getId());
			DBCursor specCursor = dbVetSpecialties.find(query);
			while (specCursor.hasNext()) {
				Specialty specialty = new Specialty();
				DBObject actualSpec = specCursor.next();
				Double specialtyId = (Double)actualSpec.get("_id");
				BasicDBObject query2 = new BasicDBObject("_id", specialtyId);
				DBCursor specDefCursor = dbSpecialties.find(query2);
				while (specDefCursor.hasNext()) {
					DBObject actualSpec2 = specDefCursor.next();
					specialty.setId(((Double)actualSpec2.get("_id")).intValue());
					specialty.setName((String)actualSpec2.get("name"));
					actualVet.addSpecialty(specialty);
				}
			}
			
			returnVets.add(actualVet);
		}

		return returnVets;
	}
	
	public Collection<PetType> getPetTypes() throws DataAccessException {
		List<PetType> returnPetType = new ArrayList<PetType>(); 
		
		DBCollection dbPetType = database.getCollection("petType");
		DBCursor cursor = dbPetType.find();
		while (cursor.hasNext()) {
			returnPetType.add(mapToPetType(cursor.next()));
		}

		return returnPetType;
	}
	
    public Collection<Owner> findOwners(String lastName) throws DataAccessException {
		List<Owner> returnOwner = new ArrayList<Owner>(); 
		
		DBCollection dbOwner = database.getCollection("owner");
		DBCursor cursor;
		if (lastName == null || lastName.length() == 0)
			cursor = dbOwner.find();
		else {
			BasicDBObject query = new BasicDBObject("lastName", lastName);
			cursor = dbOwner.find(query);
		}
		System.out.println("Hemos recuperado " + cursor.size() + " owners");
		while (cursor.hasNext()) {
			returnOwner.add(mapToOwner(cursor.next()));
		}

		return returnOwner;
	}
    
    public Owner loadOwner(int id) throws DataAccessException {
    	
    	DBCollection dbOwner = database.getCollection("owner");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	Owner owner = mapToOwner(dbOwner.findOne(query));
    	
		return owner;
	}
    
    public Pet loadPet(int id) throws DataAccessException {
    	DBCollection dbPet = database.getCollection("pet");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	Pet pet = mapToPet(dbPet.findOne(query), null);
    	
		return pet;
	}
    
    public void storeOwner(Owner owner) throws DataAccessException {
		DBCollection collOwner = database.getCollection("owner");
		if (owner.getId() == null) {
			DBObject dbOwner = mapOwnerToDBObject(owner, getNextIdValue("ownerid"));
			collOwner.insert(dbOwner);
		} else {
			DBObject dbOwner = mapOwnerToDBObject(owner, owner.getId());
			DBObject dbOwnerId = new BasicDBObject("_id", owner.getId());
			collOwner.update(dbOwnerId, dbOwner);
		}
	}

    public void storePet(Pet pet) throws DataAccessException {
		
		DBCollection collPet = database.getCollection("pet");
		if (pet.getId() == null) {
			DBObject dbPet = mapPetToDBObject(pet, getNextIdValue("petid"));
			collPet.insert(dbPet);
		} else {
			DBObject dbPet = mapPetToDBObject(pet, pet.getId());
			DBObject dbPetId = new BasicDBObject("_id", pet.getId());
			collPet.update(dbPetId, dbPet);
		}
		
	}
    
    public void storeVisit(Visit visit) throws DataAccessException {
		DBObject dbVisit = mapVisitToDBObject(visit, getNextIdValue("visitid"));
		DBCollection collVisit = database.getCollection("visit");
		collVisit.insert(dbVisit);
	}
    
    public void deletePet(int id) throws DataAccessException {
    	BasicDBObject dbPet = new BasicDBObject("_id", id);
    	DBCollection collPet = database.getCollection("pet");
    	collPet.remove(dbPet);
    	
	}
    
    public void deleteVisit(int id) throws DataAccessException {
    	DBCollection dbVisit = database.getCollection("visit");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	dbVisit.remove(query);
    }
    
    
    private DBObject mapVisitToDBObject(Visit visit, Integer id) {
    	BasicDBObject dbVisit = new BasicDBObject();
    	
    	dbVisit.append("_id", id)
    		   .append("date", visit.getDate())
    		   .append("description", visit.getDescription())
    		   .append("pet", visit.getPet().getId());
    	
    	return dbVisit;    	
    }
    
    private DBObject mapOwnerToDBObject(Owner owner, Integer id) {
    	BasicDBObject dbOwner = new BasicDBObject();
    	
    	dbOwner.append("_id", id)
    	 	   .append("address", owner.getAddress())
    		   .append("city", owner.getCity())
    		   .append("firstName", owner.getFirstName())
    		   .append("lastName", owner.getLastName())
    		   .append("telephone", owner.getTelephone());
    		   
    	
    	return dbOwner;
    }
    
    private DBObject mapPetToDBObject(Pet pet, Integer id) {
    	BasicDBObject dbPet = new BasicDBObject();
    	
    	dbPet.append("_id", id)
    		 .append("birthDate", pet.getBirthDate())
    		 .append("name", pet.getName())
    		 .append("owner", pet.getOwner().getId())
    		 .append("petType", pet.getType().getId());
    	
    	return dbPet;
    }
    
    private Owner mapToOwner(DBObject actual) {
    	Owner actualOwner = new Owner();
		actualOwner.setId(((Integer)actual.get("_id")));
		actualOwner.setAddress((String)actual.get("address"));
		actualOwner.setCity((String)actual.get("city"));
		actualOwner.setFirstName((String)actual.get("firstName"));
		actualOwner.setLastName((String)actual.get("lastName"));
		actualOwner.setTelephone((String)actual.get("telephone"));
		
		DBCollection collPet = database.getCollection("pet");
		BasicDBObject filter = new BasicDBObject("owner", actualOwner.getId());
		DBCursor curr = collPet.find(filter);
		while (curr.hasNext()) {
			actualOwner.addPet(mapToPet(curr.next(), actualOwner));	
		}
		return actualOwner;
    }
    
    private PetType mapToPetType(DBObject actual) {
    	PetType actualPetType = new PetType();
		actualPetType.setId(((Double)actual.get("_id")).intValue());
		actualPetType.setName((String)actual.get("name"));
		return actualPetType;
    }
    
    private Pet mapToPet(DBObject actual, Owner owner) {
    	Pet actualPet = new Pet();
    	actualPet.setId((Integer)actual.get("_id"));
    	actualPet.setBirthDate((Date)actual.get("birthDate"));
    	actualPet.setName((String)actual.get("name"));
    	actualPet.setType(getPetTypeById((Integer)actual.get("petType")));
    	if (owner != null) {
    		actualPet.setOwner(owner);
    	} else {
    		actualPet.setOwner(findPetsOwner(actualPet.getId()));
    	}
    	
		DBCollection collVisit = database.getCollection("visit");
		BasicDBObject filter = new BasicDBObject("pet", actualPet.getId());
		DBCursor curr = collVisit.find(filter);
		while (curr.hasNext()) {
			actualPet.addVisit(mapToVisit(curr.next(), actualPet));	
		}
    	return actualPet;
    }
    
    private Owner findPetsOwner(Integer petId) {
    	Owner owner = null;
    	
    	DBCollection dbOwner = database.getCollection("owner");
		DBCursor cursor = dbOwner.find();
    	
		Owner tempOwner;
		while (cursor.hasNext() && (owner == null)) {
			tempOwner = mapToOwner(cursor.next());
			for (Pet pet : tempOwner.getPets()) {
				if (pet.getId() == petId) {
					owner = tempOwner;
					break;
				}
			}
		}
    	
    	return owner;
    }
    
    private Visit mapToVisit(DBObject actual, Pet pet) {
    	Visit actualVisit = new Visit();
    	actualVisit.setId((Integer)actual.get("_id"));
    	actualVisit.setDescription((String)actual.get("description"));
    	actualVisit.setDate((Date)actual.get("date"));
    	actualVisit.setPet(pet);
    	
    	return actualVisit;
    	
    }
    
    private PetType getPetTypeById(Integer id) {

    	DBCollection dbPetType = database.getCollection("petType");
		BasicDBObject query = new BasicDBObject("_id", id);
		PetType petType = mapToPetType(dbPetType.findOne(query));

		return petType;
    }
    
    private Integer getNextIdValue(String collection) {
    	DBCollection dbCounters = database.getCollection("counters");
    	Double counterValue;
    	synchronized (dbCounters) {
        	BasicDBObject filter = new BasicDBObject("_id", collection);
        	DBObject counter = dbCounters.findOne(filter);
        	counterValue = (Double)counter.get("seq");
        	counter.put("seq", counterValue +1);
        	dbCounters.update(filter, counter);
		}
    	return counterValue.intValue();
    }
}
