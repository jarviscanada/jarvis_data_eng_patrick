-- create a database
DROP DATABASE IF EXISTS host_agent;
CREATE DATABASE host_agent;

--connect to the new database;
\c host_agent;

DROP TABLE IF EXISTS PUBLIC.host_info CASCADE;
DROP TABLE IF EXISTS PUBLIC.host_usage CASCADE;

--create table
CREATE TABLE PUBLIC.host_info
	(
		id			SERIAL NOT NULL,
		hostname		VARCHAR NOT NULL UNIQUE,
		cpu_number		INT NOT NULL,
		cpu_architecture	VARCHAR NOT NULL,
		cpu_model		VARCHAR NOT NULL,
		cpu_mhz			FLOAT NOT NULL,
		L2_cache		INT NOT NULL,
		total_mem		INT NOT NULL,
		"timestamp"		TIMESTAMP NOT NULL,
		PRIMARY KEY (id)
	);

CREATE TABLE PUBLIC.host_usage
	(
		"timestamp"		TIMESTAMP NOT NULL,
		host_id			SERIAL NOT NULL,
		memory_free		INT NOT NULL,
		cpu_idel		INT NOT NULL,
		cpu_kernel		INT NOT NULL,
		disk_io			INT NOT NULL,
		disk_available		INT NOT NULL,
		FOREIGN KEY (host_id) REFERENCES host_info (id)
	);