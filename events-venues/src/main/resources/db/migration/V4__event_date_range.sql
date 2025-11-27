-- Renombro la columna date
ALTER TABLE events RENAME COLUMN date TO start_date;

-- Agrego la nueva columna end_date
ALTER TABLE events ADD COLUMN end_date DATE NOT NULL;

-- Si tu BD tiene datos viejos, usa esto temporalmente:
-- ALTER TABLE events ADD COLUMN end_date DATE;
-- UPDATE events SET end_date = start_date;
-- ALTER TABLE events ALTER COLUMN end_date SET NOT NULL;