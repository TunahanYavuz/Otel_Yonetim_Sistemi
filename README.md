# 🏨 Otel Yönetim Sistemi

JavaFX tabanlı bir otel rezervasyon ve yönetim uygulaması.

## 📋 Özellikler

### Kullanıcı Rolleri
- **Admin**: Tam yetki (kullanıcı yönetimi, oda yönetimi, raporlar)
- **Personel**: Müşteri adına rezervasyon, check-in/check-out işlemleri
- **Müşteri**: Kendi rezervasyonlarını görüntüleme ve oda arama

### Oda Yönetimi
- Oda ekleme, düzenleme, silme
- Oda durumları: Müsait, Rezerve, Dolu, Temizlikte, Bakımda, Kullanım Dışı
- Oda özellikleri: Balkon, Deniz Manzarası, Jacuzzi, Mutfak

### Rezervasyon Sistemi
- Tarih ve kişi sayısına göre oda arama
- Oda tipi ve özelliklere göre filtreleme
- Rezervasyon durumları: Beklemede, Onaylandı, Giriş Yapıldı, Çıkış Yapıldı, İptal Edildi

### Müşteri Sadakat Programı
- Bronze, Silver, Gold, Platinum seviyeleri
- Seviyeye göre indirimler

### Ödeme Seçenekleri
- Kredi Kartı, Nakit, Havale, Online Ödeme

## 🛠️ Teknolojiler

- **Java 21+** (Preview özellikler aktif)
- **JavaFX 25.0.1** (UI framework)
- **MS SQL Server** (Veritabanı)
- **Maven** (Bağımlılık yönetimi)
- **Gson** (JSON parsing)

## 🏗️ Tasarım Desenleri

- **State Pattern**: Oda durumları yönetimi
- **Strategy Pattern**: Ödeme işlemleri
- **Observer Pattern**: Bildirim sistemi
- **Factory Method Pattern**: Ödeme stratejisi ve oda durumu nesnelerinin oluşturulması
- **MVC Pattern**: Controller-View-Model ayrımı

> **Detaylı Factory Pattern Dokümantasyonu:** [FACTORY_PATTERNS.md](FACTORY_PATTERNS.md) dosyasına bakın

## ⚙️ Kurulum ve Çalıştırma

### Gereksinimler
- Java 21 veya üzeri
- Maven 3.6+
- MS SQL Server
- JavaFX 25.0.1

### Veritabanı Kurulumu
1. MS SQL Server'da `otel_db.bak` dosyasını restore edin
2. `db-config.json` dosyasını düzenleyin:
```json
{
  "server": "localhost",
  "database": "otel_db",
  "user": "sa",
  "password": "your_password"
}
```

### Maven ile Çalıştırma
```bash
mvn clean javafx:run
```

### IntelliJ IDEA ile Çalıştırma

1. **Run Configuration** oluşturun
2. **Main class**: `ymt_odev.Controllers.Main`
3. **VM Options** alanına aşağıdaki parametreleri ekleyin:

```
--module-path "$USER_HOME$\.m2\repository\org\openjfx\javafx-controls\25.0.1;$USER_HOME$\.m2\repository\org\openjfx\javafx-graphics\25.0.1;$USER_HOME$\.m2\repository\org\openjfx\javafx-base\25.0.1;$USER_HOME$\.m2\repository\org\openjfx\javafx-fxml\25.0.1" --add-modules javafx.controls,javafx.fxml
```

> **Not**: `$USER_HOME$` IntelliJ'in otomatik olarak kullanıcı dizininize (`C:\Users\<kullanici>`) çevireceği bir değişkendir.

#### Windows PowerShell için alternatif:
```powershell
--module-path "%USERPROFILE%\.m2\repository\org\openjfx\javafx-controls\25.0.1;%USERPROFILE%\.m2\repository\org\openjfx\javafx-graphics\25.0.1;%USERPROFILE%\.m2\repository\org\openjfx\javafx-base\25.0.1;%USERPROFILE%\.m2\repository\org\openjfx\javafx-fxml\25.0.1" --add-modules javafx.controls,javafx.fxml
```

### Bağımlılıkları İndirme
İlk çalıştırmadan önce JavaFX bağımlılıklarını indirin:
```bash
mvn dependency:resolve
```

## 📁 Proje Yapısı

```
src/main/java/ymt_odev/
├── Controllers/     # JavaFX Controller sınıfları
├── Database/        # Veritabanı bağlantı ve işlemleri
├── Domain/          # Veri modelleri (Room, Reservation)
├── Patterns/        # Tasarım deseni implementasyonları
├── Services/        # İş mantığı servisleri
├── Users/           # Kullanıcı sınıfları (Admin, Staff, Customer)
└── Utils/           # Yardımcı sınıflar

src/main/resources/  # FXML dosyaları ve stiller
```

## 📝 Lisans

Bu proje eğitim amaçlı geliştirilmiştir.

