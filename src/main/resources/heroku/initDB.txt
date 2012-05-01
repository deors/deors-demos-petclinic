CREATE TABLE vets (
  id SERIAL NOT NULL PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30)
);

CREATE INDEX vets_last ON vets (last_name);

CREATE TABLE specialties (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(80)
);

CREATE INDEX specialties_name ON specialties (name);

CREATE TABLE vet_specialties (
  vet_id SERIAL NOT NULL,
  specialty_id SERIAL NOT NULL,
  FOREIGN KEY (vet_id) REFERENCES vets(id),
  FOREIGN KEY (specialty_id) REFERENCES specialties(id),
  UNIQUE (vet_id,specialty_id)
);

CREATE TABLE types (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(80)
);

CREATE INDEX types_name ON types (name);

CREATE TABLE owners (
  id SERIAL NOT NULL PRIMARY KEY,
  first_name VARCHAR(30),
  last_name VARCHAR(30),
  address VARCHAR(255),
  city VARCHAR(80),
  telephone VARCHAR(20)
);

CREATE INDEX owners_last ON owners (last_name);

CREATE TABLE pets (
  id SERIAL NOT NULL PRIMARY KEY,
  name VARCHAR(30),
  birth_date DATE,
  type_id SERIAL NOT NULL,
  owner_id SERIAL NOT NULL,
  FOREIGN KEY (owner_id) REFERENCES owners(id),
  FOREIGN KEY (type_id) REFERENCES types(id)
);

CREATE INDEX pets_name ON pets (name);

CREATE TABLE visits (
  id SERIAL NOT NULL PRIMARY KEY,
  pet_id SERIAL NOT NULL,
  visit_date DATE,
  description VARCHAR(255),
  FOREIGN KEY (pet_id) REFERENCES pets(id)
);
