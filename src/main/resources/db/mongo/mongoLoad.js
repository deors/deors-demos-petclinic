// counters

db.counters.insert({ "_id" : "ownerid", "seq" : 5 });
db.counters.insert({ "_id" : "petid", "seq" : 8 });
db.counters.insert({ "_id" : "visitid", "seq" : 7 });

// petType

db.petType.insert({ "_id" : 0, "name" : "dog" });
db.petType.insert({ "_id" : 1, "name" : "cat" });
db.petType.insert({ "_id" : 3, "name" : "parrot" });

// specialty

db.specialty.insert({ "_id" : 1, "name" : "Animal behavior" });
db.specialty.insert({ "_id" : 2, "name" : "Animal welfare" });
db.specialty.insert({ "_id" : 3, "name" : "Birds" });
db.specialty.insert({ "_id" : 4, "name" : "Bovine" });
db.specialty.insert({ "_id" : 5, "name" : "Canine" });
db.specialty.insert({ "_id" : 6, "name" : "Cardiology" });
db.specialty.insert({ "_id" : 7, "name" : "Equine" });
db.specialty.insert({ "_id" : 8, "name" : "Emergency and critical care" });
db.specialty.insert({ "_id" : 9, "name" : "Feline" });
db.specialty.insert({ "_id" : 10, "name" : "Reptile and amphibian" });

//vet

db.vet.insert({ "_id" : 1, "firstName" : "John", "lastName" : "Doe" });
db.vet.insert({ "_id" : 2, "firstName" : "Mary", "lastName" : "Smith" });

// vetSpecialty

db.vetSpecialty.insert({ "_id" : 1, "vetId" : 1, "specialtyId" : 1 });
db.vetSpecialty.insert({ "_id" : 2, "vetId" : 1, "specialtyId" : 9 });
db.vetSpecialty.insert({ "_id" : 3, "vetId" : 2, "specialtyId" : 10 });
db.vetSpecialty.insert({ "_id" : 4, "vetId" : 2, "specialtyId" : 3 });
db.vetSpecialty.insert({ "_id" : 5, "vetId" : 2, "specialtyId" : 5 });
