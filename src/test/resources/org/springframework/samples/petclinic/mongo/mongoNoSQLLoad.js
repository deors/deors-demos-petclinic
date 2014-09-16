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