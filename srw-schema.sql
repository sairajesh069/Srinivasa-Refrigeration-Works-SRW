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
    gender VARCHAR(20) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE, 
    email VARCHAR(60) NOT NULL UNIQUE,
    address VARCHAR(108) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME);
    
-- Creating the employees table
CREATE TABLE employees (
    employee_reference BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    employee_id VARCHAR(10) UNIQUE,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE, 
    email VARCHAR(45) NOT NULL UNIQUE,
    address VARCHAR(108) NOT NULL,
    national_id_number VARCHAR(20) NOT NULL UNIQUE,
    designation VARCHAR(25),
    date_of_hire DATETIME NOT NULL,
    salary BIGINT NOT NULL,
    updated_at DATETIME,
    date_of_exit DATETIME) AUTO_INCREMENT=101;
    
-- Creating the customers table
CREATE TABLE customers (
    customer_reference BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    customer_id VARCHAR(10) UNIQUE,
    first_name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    date_of_birth DATE NOT NULL,
    gender VARCHAR(20) NOT NULL,
    phone_number VARCHAR(15) NOT NULL UNIQUE, 
    email VARCHAR(60) NOT NULL UNIQUE,
    address VARCHAR(108) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME) AUTO_INCREMENT=1001;
    
-- Creating the complaints table
CREATE TABLE complaints (
    complaint_reference BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    complaint_id VARCHAR(12) UNIQUE,
    booked_by_id VARCHAR(10),
    customer_name VARCHAR(60) NOT NULL,
    contact_number VARCHAR(15) NOT NULL, 
    email VARCHAR(45) NOT NULL,
    address VARCHAR(108) NOT NULL,
    product_type VARCHAR(45) NOT NULL,
    brand VARCHAR(45) NOT NULL,
    product_model VARCHAR(45) NOT NULL,
    description VARCHAR(250) NOT NULL,
    created_at DATETIME NOT NULL,
    status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED') NOT NULL,
    updated_at DATETIME NULL,
    technician_id VARCHAR(25),
    closed_at DATETIME NULL,
    customer_feedback VARCHAR(250) NULL);
