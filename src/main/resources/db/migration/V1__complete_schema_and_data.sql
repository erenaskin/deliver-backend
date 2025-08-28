-- Complete schema and sample data for DeliVer application
-- This single migration creates all tables and inserts comprehensive sample data

-- Users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN NOT NULL DEFAULT TRUE,
    last_login_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vendors table
CREATE TABLE vendors (
    id BIGSERIAL PRIMARY KEY,
    business_name VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(50),
    address VARCHAR(500),
    phone_number VARCHAR(20),
    business_email VARCHAR(255),
    website_url VARCHAR(200),
    logo_url VARCHAR(500),
    banner_image_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    is_accepting_orders BOOLEAN NOT NULL DEFAULT TRUE,
    average_rating DECIMAL(5, 2) DEFAULT 0.00,
    review_count INTEGER DEFAULT 0,
    total_orders INTEGER DEFAULT 0,
    minimum_order_amount DECIMAL(10, 2),
    delivery_fee DECIMAL(10, 2),
    estimated_delivery_time_minutes INTEGER,
    delivery_radius_km DECIMAL(5, 2),
    tax_id VARCHAR(50),
    business_license_number VARCHAR(100),
    approved_at TIMESTAMP,
    service_type VARCHAR(20) DEFAULT 'FOOD',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    rejection_reason VARCHAR(500)
);

-- Products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    category VARCHAR(50),
    quantity INTEGER NOT NULL DEFAULT 0,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    vendor_id BIGINT REFERENCES vendors(id),
    image_url VARCHAR(500),
    sku VARCHAR(50),
    weight DECIMAL(5, 2),
    weight_unit VARCHAR(10),
    ingredients TEXT,
    allergens VARCHAR(500),
    is_vegetarian BOOLEAN DEFAULT FALSE,
    is_vegan BOOLEAN DEFAULT FALSE,
    is_gluten_free BOOLEAN DEFAULT FALSE,
    is_spicy BOOLEAN DEFAULT FALSE,
    preparation_time_minutes INTEGER,
    sort_order INTEGER DEFAULT 0,
    is_featured BOOLEAN DEFAULT FALSE,
    service_type VARCHAR(20) DEFAULT 'FOOD',
    popularity_score INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    order_count INTEGER DEFAULT 0,
    average_rating DECIMAL(3,2) DEFAULT 0.00,
    rating_count INTEGER DEFAULT 0,
    last_viewed_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Orders table
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users(id),
    vendor_id BIGINT REFERENCES vendors(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    delivery_address VARCHAR(500),
    payment_method VARCHAR(50),
    total_amount DECIMAL(10, 2) NOT NULL,
    delivery_fee DECIMAL(10, 2),
    tax_amount DECIMAL(10, 2),
    special_instructions TEXT,
    estimated_delivery_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cancellation_reason VARCHAR(500),
    rejection_reason VARCHAR(500)
);

-- Order items table
CREATE TABLE order_items (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    product_id BIGINT REFERENCES products(id),
    product_name VARCHAR(255),
    quantity INTEGER NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    special_instructions VARCHAR(255)
);

-- Services table
CREATE TABLE services (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    icon_name VARCHAR(255),
    image_url VARCHAR(255),
    is_active BOOLEAN DEFAULT TRUE,
    sort_order INTEGER DEFAULT 0,
    service_type VARCHAR(50),
    display_name VARCHAR(100)
);

-- Email verification tokens table
CREATE TABLE email_verification_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create indexes for better performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_phone ON users(phone_number);
CREATE INDEX idx_vendors_status ON vendors(status);
CREATE INDEX idx_vendors_category ON vendors(category);
CREATE INDEX idx_vendors_service_type ON vendors(service_type);
CREATE INDEX idx_products_vendor ON products(vendor_id);
CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_available ON products(is_available);
CREATE INDEX idx_products_featured ON products(is_featured);
CREATE INDEX idx_products_service_type ON products(service_type);
CREATE INDEX idx_products_popularity_score ON products(popularity_score DESC);
CREATE INDEX idx_products_view_count ON products(view_count DESC);
CREATE INDEX idx_products_order_count ON products(order_count DESC);
CREATE INDEX idx_products_average_rating ON products(average_rating DESC);
CREATE INDEX idx_products_last_viewed_at ON products(last_viewed_at DESC);
CREATE INDEX idx_products_created_at ON products(created_at DESC);
CREATE INDEX idx_orders_user ON orders(user_id);
CREATE INDEX idx_orders_vendor ON orders(vendor_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_order_items_order ON order_items(order_id);
CREATE INDEX idx_order_items_product ON order_items(product_id);

-- Insert sample users
INSERT INTO users (first_name, last_name, email, password, phone_number, role, enabled, account_non_expired, account_non_locked, credentials_non_expired, created_at, updated_at) VALUES
('Ahmet', 'Yılmaz', 'ahmet.yilmaz@email.com', '$2a$10$8K3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2', '+90 555 0101', 'USER', true, true, true, true, NOW(), NOW()),
('Mehmet', 'Kaya', 'mehmet.kaya@email.com', '$2a$10$8K3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2', '+90 555 0202', 'USER', true, true, true, true, NOW(), NOW()),
('Ayşe', 'Demir', 'ayse.demir@email.com', '$2a$10$8K3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2', '+90 555 0303', 'USER', true, true, true, true, NOW(), NOW()),
('Admin', 'User', 'admin@deliver.com', '$2a$10$8K3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2', '+90 555 0000', 'ADMIN', true, true, true, true, NOW(), NOW()),
('Vendor', 'One', 'vendor1@deliver.com', '$2a$10$8K3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2mNq9P8R3tOeK3dsfJ8nL2', '+90 555 1000', 'VENDOR', true, true, true, true, NOW(), NOW());

-- Insert sample vendors for all service types
INSERT INTO vendors (business_name, description, category, subcategory, address, phone_number, business_email, status, is_accepting_orders, average_rating, review_count, minimum_order_amount, delivery_fee, estimated_delivery_time_minutes, delivery_radius_km, service_type) VALUES
-- TECH vendors
('TechStore Pro', 'En yeni teknoloji ürünleri ve aksesuarlar', 'Teknoloji', 'Elektronik', 'İstanbul, Kadıköy', '+90 216 555 0101', 'info@techstore.com', 'ACTIVE', true, 4.8, 1250, 50.00, 5.99, 30, 10.0, 'TECH'),
('MobilShop İstanbul', 'Telefon ve tablet aksesuarları', 'Teknoloji', 'Telefon', 'İstanbul, Şişli', '+90 212 555 0202', 'info@mobilshop.com', 'ACTIVE', true, 4.6, 890, 25.00, 4.99, 25, 8.0, 'TECH'),
('GameCenter', 'Oyun konsolları ve aksesuarları', 'Teknoloji', 'Oyun', 'İstanbul, Beşiktaş', '+90 212 555 0303', 'info@gamecenter.com', 'ACTIVE', true, 4.7, 650, 100.00, 7.99, 35, 12.0, 'TECH'),
('AudioWorld', 'Ses sistemleri ve kulaklıklar', 'Teknoloji', 'Ses Sistemi', 'İstanbul, Üsküdar', '+90 216 555 0404', 'info@audioworld.com', 'ACTIVE', true, 4.5, 420, 75.00, 6.99, 40, 15.0, 'TECH'),
('ChargePoint', 'Şarj cihazları ve power bankler', 'Teknoloji', 'Şarj Cihazı', 'İstanbul, Fatih', '+90 212 555 0505', 'info@chargepoint.com', 'ACTIVE', true, 4.4, 380, 30.00, 3.99, 20, 6.0, 'TECH'),
('AccessoryHub', 'Teknoloji aksesuarları merkezi', 'Teknoloji', 'Aksesuar', 'İstanbul, Beyoğlu', '+90 212 555 0606', 'info@accessoryhub.com', 'ACTIVE', true, 4.3, 290, 40.00, 5.49, 28, 9.0, 'TECH'),

-- FOOD vendors
('Mario''s Pizza', 'Orijinal İtalyan pizzaları', 'Pizza', 'İtalyan', 'İstanbul, Kadıköy', '+90 216 555 1001', 'info@mariospizza.com', 'ACTIVE', true, 4.7, 2100, 35.00, 4.99, 25, 8.0, 'FOOD'),
('Burger House', 'Lezzetli hamburgerler', 'Hamburger', 'Fast Food', 'İstanbul, Şişli', '+90 212 555 1002', 'info@burgerhouse.com', 'ACTIVE', true, 4.5, 1800, 40.00, 5.99, 20, 6.0, 'FOOD'),
('Kebapçı Ali', 'Geleneksel Türk kebapları', 'Kebap', 'Türk', 'İstanbul, Fatih', '+90 212 555 1003', 'info@kebapciali.com', 'ACTIVE', true, 4.6, 1500, 45.00, 6.99, 30, 10.0, 'FOOD'),
('Çorba Dünyası', 'Çeşitli çorbalar', 'Çorba', 'Türk', 'İstanbul, Beyoğlu', '+90 212 555 1004', 'info@corbadunyasi.com', 'ACTIVE', true, 4.4, 1200, 25.00, 3.99, 15, 5.0, 'FOOD'),
('Tatlıcı Hasan', 'Geleneksel Türk tatlıları', 'Tatlı', 'Türk', 'İstanbul, Üsküdar', '+90 216 555 1005', 'info@tatlici.com', 'ACTIVE', true, 4.8, 950, 30.00, 4.49, 20, 7.0, 'FOOD'),
('Dönerci Mehmet', 'İstanbul''un en iyi döneri', 'Döner', 'Türk', 'İstanbul, Beşiktaş', '+90 212 555 1006', 'info@donerci.com', 'ACTIVE', true, 4.3, 2200, 25.00, 3.99, 18, 9.0, 'FOOD'),

-- PET vendors
('PetShop İstanbul', 'Evcil hayvan ürünleri ve yemleri', 'Pet Shop', 'Evcil Hayvan', 'İstanbul, Kadıköy', '+90 216 555 0707', 'info@petshop.com', 'ACTIVE', true, 4.6, 750, 50.00, 5.99, 25, 8.0, 'PET'),
('Kedi Maması Plus', 'Kedi ürünleri uzmanı', 'Pet Shop', 'Kedi', 'İstanbul, Şişli', '+90 212 555 0808', 'info@kedimamasi.com', 'ACTIVE', true, 4.5, 620, 40.00, 4.99, 20, 6.0, 'PET'),
('Köpek Ürünleri', 'Köpek maması ve aksesuarları', 'Pet Shop', 'Köpek', 'İstanbul, Beşiktaş', '+90 212 555 0909', 'info@kopekurunleri.com', 'ACTIVE', true, 4.7, 890, 60.00, 6.99, 30, 10.0, 'PET'),

-- WATER vendors
('Su Dünyası', 'Doğal kaynak suları', 'İçecek', 'Su', 'İstanbul, Kadıköy', '+90 216 555 1010', 'info@sudunyasi.com', 'ACTIVE', true, 4.8, 1200, 20.00, 2.99, 15, 5.0, 'WATER'),
('Damla Su', 'Premium içme suları', 'İçecek', 'Su', 'İstanbul, Şişli', '+90 212 555 1111', 'info@damlasu.com', 'ACTIVE', true, 4.6, 950, 25.00, 3.49, 18, 7.0, 'WATER'),

-- PHARMACY vendors
('Eczane Plus', '24 saat eczane servisi', 'Eczane', 'İlaç', 'İstanbul, Kadıköy', '+90 216 555 1212', 'info@eczaneplus.com', 'ACTIVE', true, 4.9, 1800, 15.00, 4.99, 20, 6.0, 'PHARMACY'),
('Sağlık Eczanesi', 'Sağlık ürünleri ve ilaçlar', 'Eczane', 'Sağlık', 'İstanbul, Şişli', '+90 212 555 1313', 'info@saglikeezanesi.com', 'ACTIVE', true, 4.7, 1400, 20.00, 5.49, 25, 8.0, 'PHARMACY'),

-- MARKET vendors
('Market Fresh', 'Taze meyve sebze marketi', 'Market', 'Meyve-Sebze', 'İstanbul, Kadıköy', '+90 216 555 1414', 'info@marketfresh.com', 'ACTIVE', true, 4.5, 2200, 30.00, 3.99, 20, 5.0, 'MARKET'),
('SüperMarket Plus', 'Her şey dahil süpermarket', 'Market', 'Süpermarket', 'İstanbul, Şişli', '+90 212 555 1515', 'info@supermarket.com', 'ACTIVE', true, 4.6, 3100, 50.00, 4.99, 25, 8.0, 'MARKET'),
('Fırın Ekmek', 'Taze ekmek ve fırın ürünleri', 'Market', 'Fırın', 'İstanbul, Beşiktaş', '+90 212 555 1616', 'info@firinekmek.com', 'ACTIVE', true, 4.7, 1600, 15.00, 2.99, 15, 4.0, 'MARKET');

-- Insert sample products for all service types
INSERT INTO products (name, description, price, category, quantity, is_available, vendor_id, is_featured, service_type, popularity_score, view_count, order_count, average_rating, rating_count) VALUES
-- TECH products
('iPhone 15 Pro Max', 'En yeni iPhone modeli, titanyum kasa', 55000.00, 'Telefon', 15, true, 1, true, 'TECH', 95, 2500, 45, 4.8, 120),
('Samsung Galaxy S24 Ultra', 'Android amiral gemisi', 48000.00, 'Telefon', 20, true, 2, true, 'TECH', 88, 2200, 38, 4.6, 95),
('MacBook Pro M3', 'Yeni nesil MacBook Pro', 75000.00, 'Bilgisayar', 8, true, 1, true, 'TECH', 92, 1800, 25, 4.9, 85),
('iPad Air 5', 'Yeni iPad Air modeli', 25000.00, 'Tablet', 12, true, 2, true, 'TECH', 78, 1600, 22, 4.5, 70),
('PlayStation 5', 'Yeni nesil oyun konsolu', 35000.00, 'Oyun', 25, true, 3, true, 'TECH', 85, 2100, 35, 4.7, 110),
('Sony WH-1000XM5', 'Kablosuz kulaklık üst modeli', 12000.00, 'Ses Sistemi', 30, true, 4, true, 'TECH', 82, 1900, 28, 4.6, 88),
('Anker PowerCore 20000', 'Yüksek kapasiteli power bank', 800.00, 'Şarj Cihazı', 50, true, 5, true, 'TECH', 75, 1400, 20, 4.4, 65),
('Logitech MX Master 3S', 'Kablosuz mouse', 2500.00, 'Aksesuar', 35, true, 6, true, 'TECH', 70, 1200, 18, 4.3, 55),

-- FOOD products
('Margherita Pizza', 'Domates, mozzarella, fesleğen', 45.00, 'Pizza', 20, true, 7, true, 'FOOD', 90, 3000, 85, 4.7, 150),
('Pepperoni Pizza', 'Pepperoni, mozzarella, domates sosu', 55.00, 'Pizza', 18, true, 7, true, 'FOOD', 85, 2800, 75, 4.6, 130),
('Klasik Hamburger', 'Dana eti, cheddar peyniri, marul', 35.00, 'Hamburger', 25, true, 8, true, 'FOOD', 88, 2600, 70, 4.5, 120),
('Cheeseburger', 'Dana eti, çift cheddar, soğan', 42.00, 'Hamburger', 22, true, 8, true, 'FOOD', 82, 2400, 65, 4.4, 110),
('Adana Kebap', 'Acılı kıyma kebap, pilav, salata', 65.00, 'Kebap', 15, true, 9, true, 'FOOD', 87, 2200, 55, 4.6, 95),
('Urfa Kebap', 'Urfa kebap, pilav, yoğurt', 60.00, 'Kebap', 17, true, 9, true, 'FOOD', 83, 2000, 50, 4.5, 88),
('Mercimek Çorbası', 'Geleneksel mercimek çorbası', 15.00, 'Çorba', 30, true, 10, true, 'FOOD', 75, 1800, 40, 4.4, 75),
('Ezogelin Çorbası', 'Bulgur, kırmızı mercimek, baharatlar', 18.00, 'Çorba', 28, true, 10, true, 'FOOD', 72, 1600, 35, 4.3, 68),
('Baklava', 'Antep fıstıklı baklava', 25.00, 'Tatlı', 40, true, 11, true, 'FOOD', 80, 1900, 45, 4.8, 90),
('Kadayıf', 'Kızarmış kadayıf, şerbet', 22.00, 'Tatlı', 35, true, 11, true, 'FOOD', 78, 1700, 42, 4.7, 82),
('İskender Kebap', 'Döner kebap, pide, yoğurt', 70.00, 'Döner', 12, true, 12, true, 'FOOD', 86, 2100, 48, 4.6, 95),
('Tavuk Döner', 'Tavuk döner, lavaş, salata', 35.00, 'Döner', 20, true, 12, true, 'FOOD', 79, 1850, 40, 4.3, 78),

-- PET products
('Royal Canin Kitten', 'Yavru kedi maması', 450.00, 'Kedi Maması', 50, true, 13, true, 'PET', 85, 1800, 35, 4.6, 75),
('Whiskas Yetişkin Kedi', 'Yetişkin kedi kuru mama', 180.00, 'Kedi Maması', 75, true, 14, true, 'PET', 78, 1600, 28, 4.5, 65),
('Pedigree Köpek Maması', 'Yetişkin köpek kuru mama', 220.00, 'Köpek Maması', 60, true, 15, true, 'PET', 82, 1700, 32, 4.7, 70),
('Kedi Kumu Premium', 'Silika jel kedi kumu', 85.00, 'Kedi Aksesuarı', 40, true, 13, true, 'PET', 75, 1400, 25, 4.4, 55),
('Köpek Tasması', 'Deri köpek tasması', 120.00, 'Köpek Aksesuarı', 30, true, 15, true, 'PET', 72, 1300, 22, 4.3, 48),

-- WATER products
('Erikli Doğal Kaynak Suyu 5L', 'Doğal kaynak suyu', 8.50, 'Su', 100, true, 16, true, 'WATER', 88, 2500, 120, 4.8, 180),
('Hayat Su 1.5L', 'Premium içme suyu', 4.50, 'Su', 150, true, 17, true, 'WATER', 82, 2200, 95, 4.6, 150),
('Damla Maden Suyu 0.5L', 'Gazlı maden suyu', 3.00, 'Maden Suyu', 200, true, 16, true, 'WATER', 79, 1900, 85, 4.5, 130),

-- PHARMACY products
('Parol 500mg', 'Ağrı kesici tablet', 12.50, 'İlaç', 30, true, 18, true, 'PHARMACY', 90, 2800, 150, 4.9, 200),
('Vitamin C 1000mg', 'C vitamini takviyesi', 25.00, 'Vitamin', 45, true, 19, true, 'PHARMACY', 85, 2400, 120, 4.7, 160),
('Aspirin 100mg', 'Aspirin tablet', 8.50, 'İlaç', 60, true, 18, true, 'PHARMACY', 80, 2100, 95, 4.5, 140),

-- MARKET products
('Domates 1kg', 'Taze domates', 12.00, 'Sebze', 100, true, 20, true, 'MARKET', 75, 1800, 80, 4.5, 120),
('Elma 1kg', 'Kırmızı elma', 8.50, 'Meyve', 80, true, 20, true, 'MARKET', 72, 1600, 70, 4.4, 100),
('Süt 1L', 'Tam yağlı süt', 18.00, 'Süt Ürünleri', 60, true, 21, true, 'MARKET', 78, 1900, 85, 4.6, 130),
('Ekmek Tam Buğday', 'Tam buğday ekmeği', 6.50, 'Fırın', 40, true, 22, true, 'MARKET', 82, 2000, 90, 4.7, 140),
('Yoğurt 1kg', 'Yoğurt', 15.00, 'Süt Ürünleri', 50, true, 21, true, 'MARKET', 76, 1700, 75, 4.4, 110);

-- Insert services data
INSERT INTO services (name, description, icon_name, image_url, is_active, sort_order, service_type, display_name) VALUES
('FOOD', 'Yemek ve restoran siparişleri', 'restaurant-icon', '/images/services/food.jpg', true, 1, 'FOOD', 'Yemek'),
('TECH', 'Teknoloji ürünleri ve aksesuarlar', 'tech-icon', '/images/services/tech.jpg', true, 2, 'TECH', 'Teknoloji'),
('PET', 'Pet ürünleri ve hayvan bakımı', 'pet-icon', '/images/services/pet.jpg', true, 3, 'PET', 'Pet Shop'),
('WATER', 'İçme suyu ve maden suları', 'water-icon', '/images/services/water.jpg', true, 4, 'WATER', 'Su'),
('PHARMACY', 'İlaç ve sağlık ürünleri', 'pharmacy-icon', '/images/services/pharmacy.jpg', true, 5, 'PHARMACY', 'Eczane'),
('MARKET', 'Market ürünleri ve günlük ihtiyaçlar', 'market-icon', '/images/services/market.jpg', true, 6, 'MARKET', 'Market');

-- Insert sample orders
INSERT INTO orders (user_id, vendor_id, status, delivery_address, payment_method, total_amount, delivery_fee, tax_amount, special_instructions, created_at, updated_at) VALUES
(1, 1, 'DELIVERED', 'İstanbul, Kadıköy, Moda Caddesi No: 123', 'CREDIT_CARD', 125.50, 5.99, 12.55, 'Kapıyı çalınız', NOW() - INTERVAL '2 days', NOW() - INTERVAL '2 days'),
(2, 7, 'DELIVERED', 'İstanbul, Şişli, Mecidiyeköy Mahallesi', 'CASH', 89.75, 4.99, 8.98, 'Hızlı teslimat', NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'),
(3, 13, 'PENDING', 'İstanbul, Beşiktaş, Levent', 'CREDIT_CARD', 67.25, 4.49, 6.73, 'Pet maması dikkatli taşınsın', NOW(), NOW());

-- Insert sample order items
INSERT INTO order_items (order_id, product_id, product_name, quantity, unit_price, total_price, special_instructions) VALUES
(1, 1, 'iPhone 15 Pro Max', 1, 55000.00, 55000.00, 'Siyah renk tercih ederim'),
(1, 2, 'Samsung Galaxy S24 Ultra', 1, 48000.00, 48000.00, NULL),
(2, 7, 'Margherita Pizza', 2, 45.00, 90.00, 'Ekstra peynir olsun'),
(2, 8, 'Pepperoni Pizza', 1, 55.00, 55.00, NULL),
(3, 14, 'Royal Canin Kitten', 1, 450.00, 450.00, 'Küçük paketler halinde');

-- Update last_viewed_at for some products
UPDATE products SET last_viewed_at = NOW() - INTERVAL '1 day' * FLOOR(RANDOM() * 7) WHERE RANDOM() > 0.3;