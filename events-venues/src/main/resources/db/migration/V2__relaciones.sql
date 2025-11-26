-- V2__relaciones.sql

-- 1. Añadir la restricción de llave foránea
ALTER TABLE events
ADD CONSTRAINT fk_event_venue
FOREIGN KEY (id_venue)
REFERENCES venues(id);

-- 2. Asegurar que la columna de la relación no pueda ser nula (como definiste en EventEntity)
ALTER TABLE events
ALTER COLUMN id_venue BIGINT NOT NULL;