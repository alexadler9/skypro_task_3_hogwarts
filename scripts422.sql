CREATE TABLE cars (
 id bigint PRIMARY KEY,
 brand varchar(64),
 model varchar(64),
 cost money CHECK(cost > 0::money)
);

CREATE TABLE owners (
 id bigint PRIMARY KEY,
 name varchar(255) NOT NULL,
 age smallint NOT NULL CHECK(age >= 16),
 license boolean NOT NULL DEFAULT false,
 car_id bigint REFERENCES cars(id)
);