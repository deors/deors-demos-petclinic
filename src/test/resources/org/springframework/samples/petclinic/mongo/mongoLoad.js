// SQL flavor

// counters collection
db.counters.insert([
		{ "_id" : "ownerid", "seq" : 0 },
		{ "_id" : "petid", "seq" : 0 },
		{ "_id" : "visitid", "seq" : 0 },
	]);
	
// petType collection
db.petType.insert([
		{ "_id" : 0, "name" : "dog" },
		{ "_id" : 1, "name" : "cat" },
		{ "_id" : 2, "name" : "parrot" },
		{ "_id" : 3, "name" : "snake" }
	]);

// specialty collection
db.specialty.insert([
		{ "_id" : 0, "name" : "Feline" },
		{ "_id" : 1, "name" : "Canine" },
		{ "_id" : 2, "name" : "Birds" },
		{ "_id" : 3, "name" : "Lizards" }
	]);
	
// vetSpecialty collection
db.vetSpecialty.insert([
		{ "_id" : 0, "vetId" : 0, specialtyId : 0 },
		{ "_id" : 1, "vetId" : 0, specialtyId : 2 },
		{ "_id" : 2, "vetId" : 1, specialtyId : 1 },
		{ "_id" : 3, "vetId" : 1, specialtyId : 3 }
	]);
	
// vet collection
db.vet.insert([
		{"_id" : 0, "firstName" : "John", "lastName" : "Doe"},
		{"_id" : 1, "firstName" : "Mary", "lastName" : "Smith"},
	]);