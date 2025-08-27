# DeliVer Backend - Services API

Bu dokümantasyon, DeliVer backend'deki servis yapısını ve API endpoint'lerini açıklar.

## Genel Bakış

DeliVer backend'de 5 ana servis bulunmaktadır:

1. **DeliVerTech** - Teknoloji ürünleri ve elektronik cihazlar
2. **DeliverPet** - Evcil hayvan ürünleri ve mama
3. **DeliVerFood** - Taze yemek ve restoran teslimatı
4. **DeliVerHealth** - Sağlık ve ilaç teslimatı
5. **DeliVerFashion** - Moda ve giyim ürünleri

## Veritabanı Yapısı

### Services Tablosu
- `id` - Servis ID'si
- `name` - Servis adı (örn: DeliVerTech)
- `description` - Detaylı açıklama
- `icon_name` - İkon adı
- `image_url` - Ana görsel URL'i
- `banner_image_url` - Banner görsel URL'i
- `is_active` - Aktif durumu
- `order` - Sıralama
- `type` - Servis tipi
- `category` - Kategori
- `short_description` - Kısa açıklama
- `average_rating` - Ortalama puan
- `review_count` - Değerlendirme sayısı

### Products Tablosu
- `service_id` - Hangi servise ait olduğu
- `vendor_id` - Hangi satıcıya ait olduğu
- Diğer ürün bilgileri...

### Vendors Tablosu
- `service_id` - Hangi servise ait olduğu
- Diğer satıcı bilgileri...

## API Endpoint'leri

### 1. Tüm Servisleri Getir
```
GET /api/services
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "DeliVerTech",
    "description": "Teknoloji ürünleri ve elektronik cihazlar için premium teslimat hizmeti",
    "iconName": "tech-icon",
    "imageUrl": "/images/services/tech-main.jpg",
    "bannerImageUrl": "/images/services/tech-banner.jpg",
    "isActive": true,
    "order": 1,
    "type": "TECHNOLOGY",
    "category": "Electronics",
    "shortDescription": "Teknoloji ürünleri için hızlı ve güvenli teslimat",
    "averageRating": 4.8,
    "reviewCount": 1250
  }
]
```

### 2. Servis Detayını Getir
```
GET /api/services/{id}
```

### 3. Servisteki Ürünleri Getir
```
GET /api/services/{id}/products?category=Smartphones&search=iPhone&featuredOnly=false
```

**Query Parameters:**
- `category` (opsiyonel) - Ürün kategorisi
- `search` (opsiyonel) - Arama terimi
- `featuredOnly` (opsiyonel) - Sadece öne çıkan ürünler

### 4. Servisteki Satıcıları Getir
```
GET /api/services/{id}/vendors
```

### 5. Kategoriye Göre Servisleri Getir
```
GET /api/services/category/{category}
```

### 6. Öne Çıkan Servisleri Getir
```
GET /api/services/featured?limit=5
```

### 7. Servisteki Ürünleri Getir (ProductController üzerinden)
```
GET /api/products/service/{serviceId}?category=Smartphones&search=iPhone
```

## Örnek Kullanım

### DeliVerTech Servisindeki Ürünleri Getir
```bash
curl -X GET "http://localhost:8080/api/services/1/products"
```

### DeliverPet Servisindeki Kedi Maması Ürünlerini Ara
```bash
curl -X GET "http://localhost:8080/api/services/2/products?category=Cat%20Food&search=kitten"
```

### Teknoloji Kategorisindeki Servisleri Getir
```bash
curl -X GET "http://localhost:8080/api/services/category/Electronics"
```

## Örnek Veriler

### DeliVerTech Ürünleri
- iPhone 15 Pro (89,999 TL)
- MacBook Air M2 (44,999 TL)
- AirPods Pro (8,999 TL)
- Samsung Galaxy S24 (34,999 TL)
- Sony WH-1000XM5 (12,999 TL)
- Apple Watch Series 9 (19,999 TL)
- iPad Air (24,999 TL)

### DeliverPet Ürünleri
- Royal Canin Kitten (189 TL)
- Pedigree Adult Dog (299 TL)
- Cat Litter Premium (89 TL)
- Dog Toy Set (149 TL)
- Cat Scratching Post (399 TL)

## Kurulum ve Çalıştırma

1. **Veritabanı Migration'larını Çalıştır:**
   ```bash
   mvn flyway:migrate
   ```

2. **Uygulamayı Başlat:**
   ```bash
   mvn spring-boot:run
   ```

3. **Test Et:**
   ```bash
   mvn test
   ```

## Mobil Uygulama Entegrasyonu

Mobil uygulamada bu API'leri kullanarak:

1. **Ana Sayfa:** `/api/services` endpoint'i ile servisleri listele
2. **Servis Detayı:** `/api/services/{id}` ile servis bilgilerini getir
3. **Ürün Listesi:** `/api/services/{id}/products` ile servisteki ürünleri getir
4. **Ürün Arama:** Query parameter'lar ile filtreleme yap
5. **Satıcı Bilgileri:** `/api/services/{id}/vendors` ile satıcıları getir

## Geliştirme Notları

- Tüm servisler aktif olarak gelir
- Ürünler servis ID'si ile ilişkilendirilir
- Satıcılar da servis ID'si ile ilişkilendirilir
- Ürün arama ve filtreleme desteklenir
- Pagination desteklenir
- Swagger dokümantasyonu mevcuttur

## Gelecek Geliştirmeler

- Servis bazlı bildirimler
- Servis bazlı kampanyalar
- Servis bazlı raporlama
- Servis bazlı analitik
- Servis bazlı ödeme entegrasyonu
