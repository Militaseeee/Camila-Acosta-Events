-- V1__init.sql

-- Creación de la tabla VENUES con todos los campos (city, capacity, location)
CREATE TABLE venues (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    capacity INT NOT NULL
);

-- Creación de la tabla EVENTS
CREATE TABLE events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    date DATE NOT NULL,
    id_venue BIGINT --  llave foranea
);

-- Creación de la tabla USERS (Requerida para la Tarea 3: Seguridad JWT)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    -- Almacenará el rol como texto (ADMIN o USER)
    role VARCHAR(50) NOT NULL
);