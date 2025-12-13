
ALTER TABLE gallery.rentals ADD COLUMN IF NOT EXISTS has_damage_reports BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE gallery.rentals ADD COLUMN IF NOT EXISTS damage_reports_count INTEGER NOT NULL DEFAULT 0;

CREATE INDEX IF NOT EXISTS idx_rentals_has_damage ON gallery.rentals(has_damage_reports) WHERE has_damage_reports = TRUE;
