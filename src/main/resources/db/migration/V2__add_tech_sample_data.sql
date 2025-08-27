-- Add sample technology data for testing
-- This migration adds sample vendors and products for the tech category

-- Insert sample tech vendors
INSERT INTO vendors (business_name, description, category, subcategory, address, phone_number, business_email, status, is_accepting_orders, average_rating, review_count, minimum_order_amount, delivery_fee, estimated_delivery_time_minutes, delivery_radius_km, service_type) VALUES
('TechStore Pro', 'En yeni teknoloji ürünleri ve aksesuarlar', 'Teknoloji', 'Elektronik', 'İstanbul, Kadıköy', '+90 216 555 0101', 'info@techstore.com', 'ACTIVE', true, 4.8, 1250, 50.00, 5.99, 30, 10.0, 'TECH'),
('MobilShop İstanbul', 'Telefon ve tablet aksesuarları', 'Teknoloji', 'Telefon', 'İstanbul, Şişli', '+90 212 555 0202', 'info@mobilshop.com', 'ACTIVE', true, 4.6, 890, 25.00, 4.99, 25, 8.0, 'TECH'),
('GameCenter', 'Oyun konsolları ve aksesuarları', 'Teknoloji', 'Oyun', 'İstanbul, Beşiktaş', '+90 212 555 0303', 'info@gamecenter.com', 'ACTIVE', true, 4.7, 650, 100.00, 7.99, 35, 12.0, 'TECH'),
('AudioWorld', 'Ses sistemleri ve kulaklıklar', 'Teknoloji', 'Ses Sistemi', 'İstanbul, Üsküdar', '+90 216 555 0404', 'info@audioworld.com', 'ACTIVE', true, 4.5, 420, 75.00, 6.99, 40, 15.0, 'TECH'),
('ChargePoint', 'Şarj cihazları ve power bankler', 'Teknoloji', 'Şarj Cihazı', 'İstanbul, Fatih', '+90 212 555 0505', 'info@chargepoint.com', 'ACTIVE', true, 4.4, 380, 30.00, 3.99, 20, 6.0, 'TECH'),
('AccessoryHub', 'Teknoloji aksesuarları merkezi', 'Teknoloji', 'Aksesuar', 'İstanbul, Beyoğlu', '+90 212 555 0606', 'info@accessoryhub.com', 'ACTIVE', true, 4.3, 290, 40.00, 5.49, 28, 9.0, 'TECH');

-- Insert sample tech products
INSERT INTO products (name, description, price, category, quantity, is_available, vendor_id, is_featured, service_type) VALUES
('iPhone 15 Pro Max', 'En yeni iPhone modeli, titanyum kasa', 55000.00, 'Telefon', 15, true, 1, true, 'TECH'),
('Samsung Galaxy S24 Ultra', 'Android amiral gemisi', 48000.00, 'Telefon', 20, true, 2, true, 'TECH'),
('MacBook Pro M3', 'Yeni nesil MacBook Pro', 75000.00, 'Bilgisayar', 8, true, 1, true, 'TECH'),
('iPad Air 5', 'Yeni iPad Air modeli', 25000.00, 'Tablet', 12, true, 2, true, 'TECH'),
('PlayStation 5', 'Yeni nesil oyun konsolu', 35000.00, 'Oyun', 25, true, 3, true, 'TECH'),
('Sony WH-1000XM5', 'Kablosuz kulaklık üst modeli', 12000.00, 'Ses Sistemi', 30, true, 4, true, 'TECH'),
('Anker PowerCore 20000', 'Yüksek kapasiteli power bank', 800.00, 'Şarj Cihazı', 50, true, 5, true, 'TECH'),
('Logitech MX Master 3S', 'Kablosuz mouse', 2500.00, 'Aksesuar', 35, true, 6, true, 'TECH'),
('Samsung 49" Odyssey G9', 'Ultrawide oyun monitörü', 45000.00, 'Bilgisayar', 5, true, 1, true, 'TECH'),
('Apple AirPods Pro 2', 'Aktif gürültü engelleme', 6500.00, 'Ses Sistemi', 40, true, 4, true, 'TECH'),
('Nintendo Switch OLED', 'Taşınabilir oyun konsolu', 15000.00, 'Oyun', 18, true, 3, true, 'TECH'),
('iPhone 15', 'Yeni iPhone 15 modeli', 42000.00, 'Telefon', 22, true, 2, true, 'TECH'),
('Dell XPS 13', 'Ultra taşınabilir laptop', 45000.00, 'Bilgisayar', 10, true, 1, true, 'TECH'),
('JBL Go 3', 'Taşınabilir Bluetooth hoparlör', 1200.00, 'Ses Sistemi', 45, true, 4, true, 'TECH'),
('Razer DeathAdder V3', 'Gaming mouse', 1800.00, 'Aksesuar', 28, true, 6, true, 'TECH'),
('Belkin BoostCharge', 'Hızlı şarj kablosu', 300.00, 'Şarj Cihazı', 60, true, 5, true, 'TECH'),
('Steam Deck OLED', 'Taşınabilir oyun bilgisayarı', 28000.00, 'Oyun', 12, true, 3, true, 'TECH'),
('Samsung Galaxy Buds2 Pro', 'Kablosuz kulaklık', 4500.00, 'Ses Sistemi', 35, true, 4, true, 'TECH'),
('Logitech K380', 'Bluetooth klavye', 1200.00, 'Aksesuar', 42, true, 6, true, 'TECH'),
('Anker 5-in-1 Hub', 'USB-C hub', 2200.00, 'Aksesuar', 25, true, 6, true, 'TECH');

-- Insert sample food vendors
INSERT INTO vendors (business_name, description, category, subcategory, address, phone_number, business_email, status, is_accepting_orders, average_rating, review_count, minimum_order_amount, delivery_fee, estimated_delivery_time_minutes, delivery_radius_km, service_type) VALUES
('Mario''s Pizza', 'Orijinal İtalyan pizzaları', 'Pizza', 'İtalyan', 'İstanbul, Kadıköy', '+90 216 555 1001', 'info@mariospizza.com', 'ACTIVE', true, 4.7, 2100, 35.00, 4.99, 25, 8.0, 'FOOD'),
('Burger House', 'Lezzetli hamburgerler', 'Hamburger', 'Fast Food', 'İstanbul, Şişli', '+90 212 555 1002', 'info@burgerhouse.com', 'ACTIVE', true, 4.5, 1800, 40.00, 5.99, 20, 6.0, 'FOOD'),
('Kebapçı Ali', 'Geleneksel Türk kebapları', 'Kebap', 'Türk', 'İstanbul, Fatih', '+90 212 555 1003', 'info@kebapciali.com', 'ACTIVE', true, 4.6, 1500, 45.00, 6.99, 30, 10.0, 'FOOD'),
('Çorba Dünyası', 'Çeşitli çorbalar', 'Çorba', 'Türk', 'İstanbul, Beyoğlu', '+90 212 555 1004', 'info@corbadunyasi.com', 'ACTIVE', true, 4.4, 1200, 25.00, 3.99, 15, 5.0, 'FOOD'),
('Tatlıcı Hasan', 'Geleneksel Türk tatlıları', 'Tatlı', 'Türk', 'İstanbul, Üsküdar', '+90 216 555 1005', 'info@tatlici.com', 'ACTIVE', true, 4.8, 950, 30.00, 4.49, 20, 7.0, 'FOOD'),
('Dönerci Mehmet', 'İstanbul''un en iyi döneri', 'Döner', 'Türk', 'İstanbul, Beşiktaş', '+90 212 555 1006', 'info@donerci.com', 'ACTIVE', true, 4.3, 2200, 25.00, 3.99, 18, 9.0, 'FOOD');

-- Insert sample food products
INSERT INTO products (name, description, price, category, quantity, is_available, vendor_id, is_featured, service_type) VALUES
('Margherita Pizza', 'Domates, mozzarella, fesleğen', 45.00, 'Pizza', 20, true, 7, true, 'FOOD'),
('Pepperoni Pizza', 'Pepperoni, mozzarella, domates sosu', 55.00, 'Pizza', 18, true, 7, true, 'FOOD'),
('Klasik Hamburger', 'Dana eti, cheddar peyniri, marul', 35.00, 'Hamburger', 25, true, 8, true, 'FOOD'),
('Cheeseburger', 'Dana eti, çift cheddar, soğan', 42.00, 'Hamburger', 22, true, 8, true, 'FOOD'),
('Adana Kebap', 'Acılı kıyma kebap, pilav, salata', 65.00, 'Kebap', 15, true, 9, true, 'FOOD'),
('Urfa Kebap', 'Urfa kebap, pilav, yoğurt', 60.00, 'Kebap', 17, true, 9, true, 'FOOD'),
('Mercimek Çorbası', 'Geleneksel mercimek çorbası', 15.00, 'Çorba', 30, true, 10, true, 'FOOD'),
('Ezogelin Çorbası', 'Bulgur, kırmızı mercimek, baharatlar', 18.00, 'Çorba', 28, true, 10, true, 'FOOD'),
('Baklava', 'Antep fıstıklı baklava', 25.00, 'Tatlı', 40, true, 11, true, 'FOOD'),
('Kadayıf', 'Kızarmış kadayıf, şerbet', 22.00, 'Tatlı', 35, true, 11, true, 'FOOD'),
('İskender Kebap', 'Döner kebap, pide, yoğurt', 70.00, 'Döner', 12, true, 12, true, 'FOOD'),
('Tavuk Döner', 'Tavuk döner, lavaş, salata', 35.00, 'Döner', 20, true, 12, true, 'FOOD'),
('Karışık Pizza', 'Sucuk, sosis, mantar, biber', 62.00, 'Pizza', 16, true, 7, false, 'FOOD'),
('Veggie Burger', 'Sebzeli burger, avokado', 38.00, 'Hamburger', 19, true, 8, false, 'FOOD'),
('Tavuk Şiş', 'Marine edilmiş tavuk şiş', 55.00, 'Kebap', 14, true, 9, false, 'FOOD'),
('Yayla Çorbası', 'Yoğurtlu yayla çorbası', 16.00, 'Çorba', 32, true, 10, false, 'FOOD'),
('Künefe', 'Kızarmış kadayıf, peynir, şerbet', 30.00, 'Tatlı', 25, true, 11, false, 'FOOD'),
('Et Döner', 'Et döner, lavaş, salata', 40.00, 'Döner', 18, true, 12, false, 'FOOD');