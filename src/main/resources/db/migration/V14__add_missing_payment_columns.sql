ALTER TABLE gallery.payments
    ADD COLUMN IF NOT EXISTS user_email VARCHAR(255),
    ADD COLUMN IF NOT EXISTS car_license_plate VARCHAR(20);
