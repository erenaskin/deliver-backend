-- Add service_type to vendors and products tables
-- This allows us to separate food and tech services

-- Add service_type to vendors table
ALTER TABLE vendors ADD COLUMN service_type VARCHAR(20) DEFAULT 'FOOD';

-- Add service_type to products table
ALTER TABLE products ADD COLUMN service_type VARCHAR(20) DEFAULT 'FOOD';

-- Update existing vendors to have correct service types
UPDATE vendors SET service_type = 'TECH' WHERE category IN (
    'Teknoloji', 'Telefon', 'Bilgisayar', 'Tablet', 'Oyun', 'Elektronik',
    'Ses Sistemi', 'Aksesuar', 'Şarj Cihazı'
);

-- Update existing products to have correct service types
UPDATE products SET service_type = 'TECH' WHERE category IN (
    'Telefon', 'Bilgisayar', 'Tablet', 'Oyun', 'Ses Sistemi', 'Aksesuar', 'Şarj Cihazı'
);

-- Create indexes for better performance
CREATE INDEX idx_vendors_service_type ON vendors(service_type);
CREATE INDEX idx_products_service_type ON products(service_type);