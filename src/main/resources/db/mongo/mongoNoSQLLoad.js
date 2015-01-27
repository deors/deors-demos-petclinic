// counters

db.counters.insert({ "_id" : "ownerid", "seq" : 0});
db.counters.insert({ "_id" : "petid", "seq" : 0 });
db.counters.insert({ "_id" : "visitid", "seq" : 0 });

// petType

db.petType.insert({ "_id" : 0, "name" : "dog" });
db.petType.insert({ "_id" : 1, "name" : "cat" });
db.petType.insert({ "_id" : 3, "name" : "parrot" });

// vet

db.vet.insert({ "_id" : 1, "firstName" : "John", "lastName" : "Doe",  specialties: [{ "_id" : 5, "name" : "Canine" }, { "_id" : 9, "name" : "Feline" } ] });
db.vet.insert({ "_id" : 2, "firstName" : "Mary", "lastName" : "Smith", specialties: [{ "_id" : 3, "name" : "Birds" },{ "_id" : 10, "name" : "Reptile and amphibian" } ] });
