# DeliVer Mobile App Backend

Bu backend, DeliVer mobil uygulamasının farklı servislerini (Eczane, Pet, Food, Su, Market, Teknoloji) desteklemek için tasarlanmıştır.

## Ana Özellikler

### 1. Çoklu Servis Desteği
- **Eczane**: İlaç ve sağlık ürünleri
- **Pet**: Evcil hayvan ürünleri  
- **Food**: Yemek siparişi
- **Su**: Su siparişi
- **Market**: Market alışverişi
- **Teknoloji**: Teknoloji ürünleri

### 2. Mobil Uygulama Özellikleri
- Hızlı kategori filtreleme
- Acil durum servisleri (7/24 eczane)
- Ürün özellikleri (rating, teslimat süresi, ücreti)
- Vendor özellikleri (7/24, REÇETE, VET, TAKSİT, İNDİRİM)
- Arama ve filtreleme
- Favori işlemleri

## API Endpoints

### Servis Kategorileri
```
GET /api/v1/services/categories - Tüm servis kategorilerini listele
GET /api/v1/services/categories/{categoryName} - Belirli servis detaylarını getir
GET /api/v1/services/search - Servislerde arama yap
```

### Parametreler
- `productCategory`: Ürün kategorisi (Tümü, Ağrı Kesici, Soğuk Algınlığı)
- `petType`: Evcil hayvan türü (Kedi, Köpek, Kuş, Balık)
- `waterType`: Su türü (19L Damacana, 5L Damacana, 1.5L Şişe)
- `page`: Sayfa numarası
- `size`: Sayfa boyutu

## Veritabanı Yapısı

### Mevcut Tablolar (Güncellendi)
- `services`: Servis bilgileri + mobil uygulama alanları
- `products`: Ürün bilgileri + mobil uygulama alanları  
- `vendors`: Vendor bilgileri + mobil uygulama alanları

### Yeni Eklenen Alanlar
- `display_name`: Mobil uygulamada görünecek isim
- `subtitle`: Alt başlık
- `color`: Ana renk
- `accent_color`: Vurgu rengi
- `is_emergency_service`: Acil durum servisi mi?
- `emergency_text`: Acil durum metni
- `emergency_button_text`: Acil durum buton metni
- `water_type`: Su türü (ürünler için)
- `service_type`: Servis türü (vendor'lar için)

## Örnek Kullanım

### Eczane Servisi
```
GET /api/v1/services/categories/Eczane?productCategory=Ağrı Kesici
```

### Pet Servisi  
```
GET /api/v1/services/categories/Pet?petType=Kedi
```

### Su Servisi
```
GET /api/v1/services/categories/Su?waterType=19L Damacana
```

## Kurulum

1. Veritabanını çalıştır
2. Migration'ları çalıştır: `V2__add_mobile_app_fields.sql`
3. Uygulamayı başlat
4. API endpoint'lerini test et

## Notlar

- Mevcut yapı korundu, sadece gerekli alanlar eklendi
- Karmaşık entity'ler yerine basit alan eklemeleri tercih edildi
- Mobil uygulama ekran görüntülerindeki tüm veriler destekleniyor
