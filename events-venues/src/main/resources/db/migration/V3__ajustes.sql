-- V3__ajustes.sql

-- 1. Optimización de Búsquedas por Venue
-- Crea un índice en el campo 'city' de la tabla 'venues' para acelerar
-- los filtros de eventos por ciudad, lo cual mejora el rendimiento del JOIN
CREATE INDEX idx_venues_city ON venues(city);

-- 2. Optimización de Búsquedas por Evento
-- Crea un índice en el campo 'date' de la tabla 'events' para acelerar
-- las consultas y filtros por fecha
CREATE INDEX idx_events_date ON events(date);