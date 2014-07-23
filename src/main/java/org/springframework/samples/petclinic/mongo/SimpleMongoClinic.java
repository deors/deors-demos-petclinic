package org.springframework.samples.petclinic.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import com.mongodb.MongoClientURI;

@Service
//@ManagedResource("petclinic:type=Clinic")
public class SimpleMongoClinic implements Clinic {

	/** Database connection. */
	DB database;
	
	/**
	 * Init method.
	 */
	@Autowired
	public void init() {
		try {
			MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017, localhost:27018,localhost:27019"));
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
	
	/**
	 * Get all the vets information in the system.
	 * @return vets information
	 * @throws DataAccessException
	 */
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
	
	/**
	 * Get all the pet types in the system
	 * @return pet types
	 * @throws DataAccessException
	 */
	public Collection<PetType> getPetTypes() throws DataAccessException {
		List<PetType> returnPetType = new ArrayList<PetType>(); 
		
		DBCollection dbPetType = database.getCollection("petType");
		DBCursor cursor = dbPetType.find();
		while (cursor.hasNext()) {
			returnPetType.add(mapToPetType(cursor.next()));
		}

		return returnPetType;
	}
	
	/**
	 * Find owners based in their last name.
	 * @param lastName last name
	 * @return owners that match with last name
	 * @throws DataAccessException
	 */
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
    
    /**
     * Return owner based in their id.
     * @param id owner id
     * @return owner retrieved
     * @throws DataAccessException
     */
    public Owner loadOwner(int id) throws DataAccessException {
    	
    	DBCollection dbOwner = database.getCollection("owner");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	Owner owner = mapToOwner(dbOwner.findOne(query));
    	
		return owner;
	}
    
    /**
     * Return pet based on its id.
     * @param id pet id
     * @return pet retrieved
     * @throws DataAccessException
     */
    public Pet loadPet(int id) throws DataAccessException {
    	DBCollection dbPet = database.getCollection("pet");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	Pet pet = mapToPet(dbPet.findOne(query), null);
    	
		return pet;
	}
    
    /**
     * Store owner information into database.
     * @param owner owner to be stored
     * @throws DataAccessException
     */
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

    /**
     * Store pet information into database.
     * @param pet pet to be stored
     * @throws DataAccessException
     */
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
    
    /**
     * Store visit into database
     * @param visit visit to be stored
     * @throws DataAccessException
     */
    public void storeVisit(Visit visit) throws DataAccessException {
		DBObject dbVisit = mapVisitToDBObject(visit, getNextIdValue("visitid"));
		DBCollection collVisit = database.getCollection("visit");
		collVisit.insert(dbVisit);
	}
    
    /**
     * Removes pet from database.
     * @param id pet id to be removed
     * @throws DataAccessException
     */
    public void deletePet(int id) throws DataAccessException {
    	BasicDBObject dbPet = new BasicDBObject("_id", id);
    	DBCollection collPet = database.getCollection("pet");
    	collPet.remove(dbPet);
    	
	}
    
    /**
     * Removes visit from database.
     * @param id visit id to be removed
     * @throws DataAccessException
     */
    public void deleteVisit(int id) throws DataAccessException {
    	DBCollection dbVisit = database.getCollection("visit");
    	BasicDBObject query = new BasicDBObject("_id", id);
    	dbVisit.remove(query);
    }
    
    /**
     * Maps visit to DBObject.
     * @param visit visit to be mapped
     * @param id visit id
     * @return DBObject with visit mapped
     */
    private DBObject mapVisitToDBObject(Visit visit, Integer id) {
    	BasicDBObject dbVisit = new BasicDBObject();
    	
    	dbVisit.append("_id", id)
    		   .append("date", visit.getDate())
    		   .append("description", visit.getDescription())
    		   .append("pet", visit.getPet().getId());
    	
    	return dbVisit;    	
    }
    
    /**
     * Maps owner to DBObject.
     * @param owner owner to be mapped
     * @param id owner id
     * @return DBObject with owner mapped
     */
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
    
    /**
     * Maps pet to DBObject
     * @param pet pet to be mapped
     * @param id pet id
     * @return DBObject with pet mapped
     */
    private DBObject mapPetToDBObject(Pet pet, Integer id) {
    	BasicDBObject dbPet = new BasicDBObject();
    	
    	dbPet.append("_id", id)
    		 .append("birthDate", pet.getBirthDate())
    		 .append("name", pet.getName())
    		 .append("owner", pet.getOwner().getId())
    		 .append("petType", pet.getType().getId());
    	
    	return dbPet;
    }
    
    /**
     * Maps DBObjet to Owner.
     * @param actual DBObjet to be mapped
     * @return owner with DBObjet mapped
     */
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
    
    /**
     * Maps DBObject to PetType
     * @param actual DBObjet to be mapped
     * @return PetType with DBObjet mapped
     */
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
