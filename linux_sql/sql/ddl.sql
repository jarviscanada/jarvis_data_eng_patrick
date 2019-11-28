CREATE DATABASE test;

\c test

CREATE TABLE PUBLIC.test_info
	(
		id	SERIAL NOT NULL,
		hostname	VARCHAR NOT NULL
	);

CREATE TABLE PUBLIC.test_usage
	(
		"timestamp"	TIMESTAMP NOT NULL,
		host_id		SERIAL NOT NULL
	);