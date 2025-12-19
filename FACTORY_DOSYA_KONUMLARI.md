# Factory Pattern Dosya Konumları

Bu dokümanda Factory Method Pattern ile ilgili **tüm dosyaların konumları** listelenmiştir.

## 📋 Factory Method Pattern Implementasyonları

### 1. PaymentProcessor (Ödeme İşlemcisi)

**Ana Dosya:**
- `src/main/java/ymt_odev/Patterns/PaymentProcessor.java` ⭐ **Factory Method içerir**

**İlgili Interface:**
- `src/main/java/ymt_odev/Patterns/PaymentStrategy.java`

**Concrete Implementations:**
- `src/main/java/ymt_odev/Patterns/CreditCardPayment.java`
- `src/main/java/ymt_odev/Patterns/CashPayment.java`
- `src/main/java/ymt_odev/Patterns/BankTransferPayment.java`

**Kullanıldığı Yerler:**
- `src/main/java/ymt_odev/Controllers/RoomSearchController.java` (satır 12, 86-100)
- `src/main/java/ymt_odev/Controllers/ReservationsController.java`

---

### 2. RoomStateManager (Oda Durumu Yöneticisi)

**Ana Dosya:**
- `src/main/java/ymt_odev/Patterns/RoomStateManager.java` ⭐ **Factory Method içerir**

**İlgili Interface:**
- `src/main/java/ymt_odev/Patterns/RoomState.java`

**Concrete Implementations:**
- `src/main/java/ymt_odev/Patterns/AvailableRoomState.java`
- `src/main/java/ymt_odev/Patterns/ReservedRoomState.java`
- `src/main/java/ymt_odev/Patterns/OccupiedRoomState.java`
- `src/main/java/ymt_odev/Patterns/CleaningRoomState.java`
- `src/main/java/ymt_odev/Patterns/MaintenanceRoomState.java`

**İlgili Enum:**
- `src/main/java/ymt_odev/RoomState.java` (enum tanımı)

**Kullanıldığı Yerler:**
- `src/main/java/ymt_odev/Controllers/RoomManagementController.java`
- `src/main/java/ymt_odev/Controllers/ReservationsController.java`
- `src/main/java/ymt_odev/Controllers/CheckinCheckoutController.java`

---

## 📂 Patterns Klasör Yapısı

```
src/main/java/ymt_odev/Patterns/
├── PaymentProcessor.java          ⭐ Factory Method #1
├── PaymentStrategy.java           (Interface)
├── CreditCardPayment.java         (Implementation)
├── CashPayment.java               (Implementation)
├── BankTransferPayment.java       (Implementation)
│
├── RoomStateManager.java          ⭐ Factory Method #2
├── RoomState.java                 (Interface)
├── AvailableRoomState.java        (Implementation)
├── ReservedRoomState.java         (Implementation)
├── OccupiedRoomState.java         (Implementation)
├── CleaningRoomState.java         (Implementation)
├── MaintenanceRoomState.java      (Implementation)
│
└── NotificationManager.java       (Observer Pattern için)
```

---

## 🎯 Factory Method'ları Bulmak İçin Grep Komutları

```bash
# Tüm factory method'ları bul
grep -r "createPaymentStrategy\|createState" --include="*.java" src/

# PaymentProcessor factory kullanımlarını bul
grep -r "createPaymentStrategy" --include="*.java" src/

# RoomStateManager factory kullanımlarını bul
grep -r "createState" --include="*.java" src/

# Factory pattern yorumlarını bul
grep -r "Factory" --include="*.java" src/ | grep -i "pattern\|method"
```

---

## 📖 Dokümantasyon Dosyaları

Bu repoyu indirdikten sonra şu dokümantasyon dosyalarını okuyabilirsiniz:

1. **ABSTRACT_FACTORY_ANALIZI.md** (bu dosya)
   - Abstract Factory vs Factory Method farkı
   - Projede bulunan factory method'ların listesi
   - UML diyagramları
   - Kullanım örnekleri

2. **FACTORY_PATTERNS.md**
   - Detaylı Factory Pattern açıklamaları
   - Kod örnekleri ve avantajları
   - Diğer pattern'lerle entegrasyon
   - Yeni factory ekleme rehberi

3. **README.md**
   - Proje genel bilgileri
   - Güncellendi: Factory Method Pattern eklendi

---

## 🔍 Hızlı Referans

| Factory Method | Dosya | Satır | Factory Method İmzası |
|----------------|-------|-------|----------------------|
| createPaymentStrategy | PaymentProcessor.java | 87-101 | `public static PaymentStrategy createPaymentStrategy(String)` |
| createState | RoomStateManager.java | 12-21 | `public static RoomState createState(String)` |

---

## ✅ Kontrol Listesi

Proje içinde Factory Pattern'leri analiz etmek için:

- [x] PaymentProcessor.java incelendi
- [x] RoomStateManager.java incelendi
- [x] Tüm concrete implementation'lar listelendi
- [x] Kullanıldığı controller'lar tespit edildi
- [x] Interface/abstract class'lar belirlendi
- [x] Dokümantasyon oluşturuldu
- [x] README.md güncellendi

---

## 📚 Ek Kaynaklar

### Design Pattern Kitapları
- "Design Patterns: Elements of Reusable Object-Oriented Software" - Gang of Four
- "Head First Design Patterns" - Freeman & Freeman

### Online Kaynaklar
- [Refactoring.Guru - Factory Method](https://refactoring.guru/design-patterns/factory-method)
- [Refactoring.Guru - Abstract Factory](https://refactoring.guru/design-patterns/abstract-factory)

---

## 📝 Notlar

- Bu projede **Abstract Factory Pattern kullanılmamaktadır**
- Sadece **Factory Method Pattern** kullanılmaktadır (2 adet)
- Her iki factory method da `static` olarak tanımlanmıştır
- Factory method'lar String parametre alır ve ilgili nesneyi döndürür
- Strategy Pattern ve State Pattern ile birlikte kullanılır

---

**Son Güncelleme:** 2025-12-19
**Analiz Eden:** GitHub Copilot
**Proje:** Otel Yönetim Sistemi
