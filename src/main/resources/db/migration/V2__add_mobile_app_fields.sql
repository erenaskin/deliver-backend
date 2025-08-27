-- Add mobile app fields to services table
ALTER TABLE services ADD COLUMN IF NOT EXISTS display_name VARCHAR(100);
ALTER TABLE services ADD COLUMN IF NOT EXISTS subtitle VARCHAR(100);
ALTER TABLE services ADD COLUMN IF NOT EXISTS color VARCHAR(50);
ALTER TABLE services ADD COLUMN IF NOT EXISTS accent_color VARCHAR(50);
ALTER TABLE services ADD COLUMN IF NOT EXISTS is_emergency_service BOOLEAN DEFAULT FALSE;
ALTER TABLE services ADD COLUMN IF NOT EXISTS emergency_text VARCHAR(100);
ALTER TABLE services ADD COLUMN IF NOT EXISTS emergency_button_text VARCHAR(100);

-- Add mobile app fields to products table
ALTER TABLE products ADD COLUMN IF NOT EXISTS water_type VARCHAR(100);

-- Add mobile app fields to vendors table
ALTER TABLE vendors ADD COLUMN IF NOT EXISTS service_type VARCHAR(100);

