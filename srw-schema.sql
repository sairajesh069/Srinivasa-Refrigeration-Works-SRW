-- Creating the 'srinivasa_refrigeration_works' database.
CREATE DATABASE srinivasa_refrigeration_works;

-- Selecting the 'srinivasa_refrigeration_works' database for all subsequent operations.
USE srinivasa_refrigeration_works;

-- Creating the user_credentials table
CREATE TABLE user_credentials (
    user_id VARCHAR(25) NOT NULL UNIQUE PRIMARY KEY,
    phone_number VARCHAR(15) NOT NULL UNIQUE,
    username VARCHAR(45) NOT NULL UNIQUE,
    password VARCHAR(68) NOT NULL,
    enabled TINYINT NOT NULL,
    user_type ENUM('OWNER', 'EMPLOYEE', 'CUSTOMER') NOT NULL);
    
-- Creating the user_roles table
CREATE TABLE user_roles (
    user_id VARCHAR(25) NOT NULL UNIQUE,
    username VARCHAR(45) NOT NULL UNIQUE,
    roles VARCHAR(25) NOT NULL,
    PRIMARY KEY(user_id, roles),
    FOREIGN KEY(user_id) REFERENCES user_credentials(user_id));
    
-- Creating the owners table
CREATE TABLE owners (
    owner_reference BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    owner_id VARCHAR(6) UNIQUE,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(10) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE, 
    email VARCHAR(60) NOT NULL UNIQUE,
    address VARCHAR(108) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME);