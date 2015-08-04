package org.springframework.samples.petclinic.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class SimpleMongoClinic
    implements Clinic {

    private static final Logger logger = LoggerFactory.getLogger(SimpleMongoClinic.class);

    // Collections
    private final static String VETS_COLLECTION = "vet";

    private final static String VETS_SPECIALTY_COLLECTION = "vetSpecialty";

    private final static String SPECIALTY_COLLECTION = "specialty";

    private final static String PET_TYPE_COLLECTION = "petType";

    private final static String OWNER_COLLECTION = "owner";

    private final static String PETS_COLLECTION = "pet";

    private final static String VISITS_COLLECTION = "visit";

    private final static String COUNTERS_COLLECTION = "counters";

    // Fields
    private final static String _ID_FIELD = "_id";

    private final static String FIRST_NAME = "firstName";

    private final static String LAST_NAME = "lastName";

    private final static String VET_ID = "vetId";

    private final static String NAME = "name";

    private final static String DATE = "date";

    private final static String DESCRIPTION = "description";

    private final static String PET = "pet";

    private final static String ADDRESS = "address";

    private final static String CITY = "city";

    private final static String TELEPHONE = "telephone";

    private final static String BIRTH_DATE = "birthDate";

    private final static String OWNER = "owner";

    private final static String PET_TYPE = "petType";

    private final static String SEQ = "seq";

    // Sequences
    private final static String OWNER_SEQUENCE = "ownerid";

    private final static String PET_SEQUENCE = "petid";

    private final static String VISIT_SEQUENCE = "visitid";

    /** Database connection. */
    DB database;

    @Value("${mongo.connection.uri}")
    String mongoURI;

    @Value("${mongo.db.name}")
    String dbName;

    /**
     * Init method.
     */
    @Autowired
    public void init() {

        logger.info("Mongo URI: " + mongoURI);

        try {
            MongoClient mongoClient = new MongoClient(new MongoClientURI(mongoURI));
            database = mongoClient.getDB(dbName);
            logger.info("MongoDB connection initialized.");
        } catch (UnknownHostException uhe) {
            uhe.printStackTrace();
        }

        Set<String> collectionNames = database.getCollectionNames();
        Iterator<String> it = collectionNames.iterator();

        if (logger.isInfoEnabled()) {
            logger.info("Collections found:");
            while (it.hasNext()) {
                logger.info(" - " + it.next() + ".");
            }
        }
    }

    /**
     * Get all the vets information in the system.
     *
     * @return vets information
     * @throws DataAccessException
     */
    public Collection<Vet> getVets() throws DataAccessException {

        List<Vet> returnVets = new ArrayList<Vet>();

        DBCollection dbVets = database.getCollection(VETS_COLLECTION);
        DBCollection dbVetSpecialties = database.getCollection(VETS_SPECIALTY_COLLECTION);
        DBCollection dbSpecialties = database.getCollection(SPECIALTY_COLLECTION);
        DBCursor cursor = dbVets.find();
        DBObject actual;
        Vet actualVet;
        while (cursor.hasNext()) {
            actual = cursor.next();
            actualVet = new Vet();
            actualVet.setId(((Double) actual.get(_ID_FIELD)).intValue());
            actualVet.setFirstName((String) actual.get(FIRST_NAME));
            actualVet.setLastName((String) actual.get(LAST_NAME));

            BasicDBObject query = new BasicDBObject(VET_ID, actualVet.getId());
            DBCursor specCursor = dbVetSpecialties.find(query);
            while (specCursor.hasNext()) {
                Specialty specialty = new Specialty();
                DBObject actualSpec = specCursor.next();
                Double specialtyId = (Double) actualSpec.get(_ID_FIELD);
                BasicDBObject query2 = new BasicDBObject(_ID_FIELD, specialtyId);
                DBCursor specDefCursor = dbSpecialties.find(query2);
                while (specDefCursor.hasNext()) {
                    DBObject actualSpec2 = specDefCursor.next();
                    specialty.setId(((Double) actualSpec2.get(_ID_FIELD)).intValue());
                    specialty.setName((String) actualSpec2.get(NAME));
                    actualVet.addSpecialty(specialty);
                }
            }

            returnVets.add(actualVet);
        }

        return returnVets;
    }

    /**
     * Get all the pet types in the system
     *
     * @return pet types
     * @throws DataAccessException
     */
    public Collection<PetType> getPetTypes() throws DataAccessException {

        List<PetType> returnPetType = new ArrayList<PetType>();

        DBCollection dbPetType = database.getCollection(PET_TYPE_COLLECTION);
        DBCursor cursor = dbPetType.find();
        while (cursor.hasNext()) {
            returnPetType.add(mapToPetType(cursor.next()));
        }

        return returnPetType;
    }

    /**
     * Find owners based in their last name.
     *
     * @param lastName last name
     * @return owners that match with last name
     * @throws DataAccessException
     */
    public Collection<Owner> findOwners(String lastName) throws DataAccessException {

        List<Owner> returnOwner = new ArrayList<Owner>();

        DBCollection dbOwner = database.getCollection(OWNER_COLLECTION);
        DBCursor cursor;
        if (lastName == null || lastName.length() == 0)
            cursor = dbOwner.find();
        else {
            BasicDBObject query = new BasicDBObject(LAST_NAME, lastName);
            cursor = dbOwner.find(query);
        }

        while (cursor.hasNext()) {
            returnOwner.add(mapToOwner(cursor.next()));
        }

        return returnOwner;
    }

    /**
     * Return owner based in their id.
     *
     * @param id owner id
     * @return owner retrieved
     * @throws DataAccessException
     */
    public Owner loadOwner(int id) throws DataAccessException {

        DBCollection dbOwner = database.getCollection(OWNER_COLLECTION);
        BasicDBObject query = new BasicDBObject(_ID_FIELD, id);
        Owner owner = mapToOwner(dbOwner.findOne(query));

        return owner;
    }

    /**
     * Return pet based on its id.
     *
     * @param id pet id
     * @return pet retrieved
     * @throws DataAccessException
     */
    public Pet loadPet(int id) throws DataAccessException {

        DBCollection dbPet = database.getCollection(PETS_COLLECTION);
        BasicDBObject query = new BasicDBObject(_ID_FIELD, id);
        Pet pet = mapToPet(dbPet.findOne(query), null);

        return pet;
    }

    /**
     * Store owner information into database.
     *
     * @param owner owner to be stored
     * @throws DataAccessException
     */
    public void storeOwner(Owner owner) throws DataAccessException {

        DBCollection collOwner = database.getCollection(OWNER_COLLECTION);
        if (owner.getId() == null) {
            Integer nextId = getNextIdValue(OWNER_SEQUENCE);
            DBObject dbOwner = mapOwnerToDBObject(owner, nextId);
            collOwner.insert(dbOwner);
            owner.setId(nextId);
        } else {
            DBObject dbOwner = mapOwnerToDBObject(owner, owner.getId());
            DBObject dbOwnerId = new BasicDBObject(_ID_FIELD, owner.getId());
            collOwner.update(dbOwnerId, dbOwner);
        }
    }

    /**
     * Store pet information into database.
     *
     * @param pet pet to be stored
     * @throws DataAccessException
     */
    public void storePet(Pet pet) throws DataAccessException {

        DBCollection collPet = database.getCollection(PETS_COLLECTION);
        if (pet.getId() == null) {
            DBObject dbPet = mapPetToDBObject(pet, getNextIdValue(PET_SEQUENCE));
            collPet.insert(dbPet);
        } else {
            DBObject dbPet = mapPetToDBObject(pet, pet.getId());
            DBObject dbPetId = new BasicDBObject(_ID_FIELD, pet.getId());
            collPet.update(dbPetId, dbPet);
        }
    }

    /**
     * Store visit into database
     *
     * @param visit visit to be stored
     * @throws DataAccessException
     */
    public void storeVisit(Visit visit) throws DataAccessException {

        DBObject dbVisit = mapVisitToDBObject(visit, getNextIdValue(VISIT_SEQUENCE));
        DBCollection collVisit = database.getCollection(VISITS_COLLECTION);
        collVisit.insert(dbVisit);
    }

    /**
     * Removes pet from database.
     *
     * @param id pet id to be removed
     * @throws DataAccessException
     */
    public void deletePet(int id) throws DataAccessException {

        BasicDBObject dbPet = new BasicDBObject(_ID_FIELD, id);
        DBCollection collPet = database.getCollection(PETS_COLLECTION);
        collPet.remove(dbPet);
    }

    /**
     * Removes visit from database.
     *
     * @param id visit id to be removed
     * @throws DataAccessException
     */
    public void deleteVisit(int id) throws DataAccessException {

        DBCollection dbVisit = database.getCollection(VISITS_COLLECTION);
        BasicDBObject query = new BasicDBObject(_ID_FIELD, id);
        dbVisit.remove(query);
    }

    /**
     * Maps visit to DBObject.
     *
     * @param visit visit to be mapped
     * @param id visit id
     * @return DBObject with visit mapped
     */
    private DBObject mapVisitToDBObject(Visit visit, Integer id) {

        BasicDBObject dbVisit = new BasicDBObject();

        dbVisit.append(_ID_FIELD, id).append(DATE, visit.getDate())
            .append(DESCRIPTION, visit.getDescription()).append(PET, visit.getPet().getId());

        return dbVisit;
    }

    /**
     * Maps owner to DBObject.
     *
     * @param owner owner to be mapped
     * @param id owner id
     * @return DBObject with owner mapped
     */
    private DBObject mapOwnerToDBObject(Owner owner, Integer id) {

        BasicDBObject dbOwner = new BasicDBObject();

        dbOwner.append(_ID_FIELD, id).append(ADDRESS, owner.getAddress())
            .append(CITY, owner.getCity()).append(FIRST_NAME, owner.getFirstName())
            .append(LAST_NAME, owner.getLastName()).append(TELEPHONE, owner.getTelephone());

        return dbOwner;
    }

    /**
     * Maps pet to DBObject
     *
     * @param pet pet to be mapped
     * @param id pet id
     * @return DBObject with pet mapped
     */
    private DBObject mapPetToDBObject(Pet pet, Integer id) {

        BasicDBObject dbPet = new BasicDBObject();

        dbPet.append(_ID_FIELD, id).append(BIRTH_DATE, pet.getBirthDate())
            .append(NAME, pet.getName()).append(OWNER, pet.getOwner().getId())
            .append(PET_TYPE, pet.getType().getId());

        return dbPet;
    }

    /**
     * Maps DBObjet to Owner.
     *
     * @param actual DBObjet to be mapped
     * @return owner with DBObjet mapped
     */
    private Owner mapToOwner(DBObject actual) {

        Owner actualOwner = new Owner();
        actualOwner.setId(((Integer) actual.get(_ID_FIELD)));
        actualOwner.setAddress((String) actual.get(ADDRESS));
        actualOwner.setCity((String) actual.get(CITY));
        actualOwner.setFirstName((String) actual.get(FIRST_NAME));
        actualOwner.setLastName((String) actual.get(LAST_NAME));
        actualOwner.setTelephone((String) actual.get(TELEPHONE));

        DBCollection collPet = database.getCollection(PETS_COLLECTION);
        BasicDBObject filter = new BasicDBObject(OWNER, actualOwner.getId());
        DBCursor curr = collPet.find(filter);
        while (curr.hasNext()) {
            actualOwner.addPet(mapToPet(curr.next(), actualOwner));
        }

        return actualOwner;
    }

    /**
     * Maps DBObject to PetType
     *
     * @param actual DBObjet to be mapped
     * @return PetType with DBObjet mapped
     */
    private PetType mapToPetType(DBObject actual) {

        PetType actualPetType = new PetType();
        actualPetType.setId(((Double) actual.get(_ID_FIELD)).intValue());
        actualPetType.setName((String) actual.get(NAME));

        return actualPetType;
    }

    /**
     * Maps DBObject to Pet
     *
     * @param actual DBObject retrieved from database
     * @param owner Pet's owner
     * @return Pet info.
     */
    private Pet mapToPet(DBObject actual, Owner owner) {

        Pet actualPet = new Pet();
        actualPet.setId((Integer) actual.get(_ID_FIELD));
        actualPet.setBirthDate((Date) actual.get(BIRTH_DATE));
        actualPet.setName((String) actual.get(NAME));
        actualPet.setType(getPetTypeById((Integer) actual.get(PET_TYPE)));
        if (owner != null) {
            actualPet.setOwner(owner);
        } else {
            actualPet.setOwner(findPetsOwner(actualPet.getId()));
        }

        DBCollection collVisit = database.getCollection(VISITS_COLLECTION);
        BasicDBObject filter = new BasicDBObject(PET, actualPet.getId());
        DBCursor curr = collVisit.find(filter);
        while (curr.hasNext()) {
            actualPet.addVisit(mapToVisit(curr.next(), actualPet));
        }

        return actualPet;
    }

    /**
     * Get pet's owner from database-
     *
     * @param petId Pet id
     * @return pet's owner
     */
    private Owner findPetsOwner(Integer petId) {

        Owner owner = null;

        DBCollection dbOwner = database.getCollection(OWNER_COLLECTION);
        DBCursor cursor = dbOwner.find();

        Owner tempOwner;
        while (cursor.hasNext() && (owner == null)) {
            tempOwner = mapToOwner(cursor.next());
            for (Pet pet : tempOwner.getPets()) {
                if (pet.getId().equals(petId)) {
                    owner = tempOwner;
                    break;
                }
            }
        }

        return owner;
    }

    /**
     * Maps DBObjet to visit.
     *
     * @param actual DBObject retrieved from database
     * @param pet visit's pet
     * @return Visit info
     */
    private Visit mapToVisit(DBObject actual, Pet pet) {

        Visit actualVisit = new Visit();
        actualVisit.setId((Integer) actual.get(_ID_FIELD));
        actualVisit.setDescription((String) actual.get(DESCRIPTION));
        actualVisit.setDate((Date) actual.get(DATE));
        actualVisit.setPet(pet);

        return actualVisit;
    }

    /**
     * Find PetType by id.
     *
     * @param id pet's id
     * @return PetType
     */
    private PetType getPetTypeById(Integer id) {

        DBCollection dbPetType = database.getCollection(PET_TYPE_COLLECTION);
        BasicDBObject query = new BasicDBObject(_ID_FIELD, id);
        PetType petType = mapToPetType(dbPetType.findOne(query));

        return petType;
    }

    /**
     * Retrieve next id value.
     *
     * @param collection Collection to be inserted
     * @return next id value.
     */
    private Integer getNextIdValue(String collection) {

        DBCollection dbCounters = database.getCollection(COUNTERS_COLLECTION);
        Double counterValue;
        synchronized (dbCounters) {
            BasicDBObject filter = new BasicDBObject(_ID_FIELD, collection);
            DBObject counter = dbCounters.findOne(filter);
            counterValue = (Double) counter.get(SEQ);
            counter.put(SEQ, counterValue + 1);
            dbCounters.update(filter, counter);
        }

        return counterValue.intValue();
    }
}
