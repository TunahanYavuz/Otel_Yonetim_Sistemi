# 🏭 Factory Pattern Kullanımı - Otel Yönetim Sistemi

Bu dokümanda projede kullanılan **Factory Pattern** (Fabrika Deseni) uygulamaları detaylı olarak açıklanmaktadır.

## 📊 Factory Pattern Türleri

### ❗ Önemli Not: Abstract Factory Pattern Kullanılmamaktadır

Bu projede **Abstract Factory Pattern** kullanılmamaktadır. Proje, daha basit ve işlevsel olan **Factory Method Pattern** kullanmaktadır.

**Factory Method Pattern** ve **Abstract Factory Pattern** arasındaki fark:
- **Factory Method Pattern**: Tek bir ürün ailesini oluşturmak için kullanılır
- **Abstract Factory Pattern**: Birbirleriyle ilişkili veya bağımlı ürün ailelerini oluşturmak için kullanılır

## 🛠️ Projede Kullanılan Factory Method Patterns

Projede **2 adet Factory Method Pattern** uygulaması bulunmaktadır:

### 1. Payment Strategy Factory (Ödeme Stratejisi Fabrikası)

**Konum:** `src/main/java/ymt_odev/Patterns/PaymentProcessor.java`

**Amaç:** Ödeme yöntemine göre uygun `PaymentStrategy` nesnesi oluşturmak

**Factory Method:**
```java
public static PaymentStrategy createPaymentStrategy(String paymentMethod) {
    switch (paymentMethod.toLowerCase()) {
        case "kredi kartı":
        case "credit card":
            return new CreditCardPayment();
        case "nakit":
        case "cash":
            return new CashPayment();
        case "havale":
        case "transfer":
            return new BankTransferPayment();
        default:
            return new CashPayment();
    }
}
```

**Oluşturduğu Nesneler:**
- `CreditCardPayment` - Kredi kartı ödemeleri için
- `CashPayment` - Nakit ödemeler için
- `BankTransferPayment` - Banka havalesi ödemeleri için

**Kullanım Örneği:**
```java
// Ödeme stratejisi oluştur
PaymentStrategy strategy = PaymentProcessor.createPaymentStrategy("kredi kartı");

// PaymentProcessor'a ata
PaymentProcessor processor = new PaymentProcessor();
processor.setPaymentStrategy(strategy);

// Ödeme işle
boolean success = processor.processPayment(reservationId, amount, customerInfo);
```

**Avantajları:**
- ✅ Ödeme yöntemi ekleme kolaylığı (yeni `case` eklemek yeterli)
- ✅ Strategy Pattern ile birlikte kullanılarak esneklik sağlar
- ✅ İstemci kodu, somut ödeme sınıflarından bağımsız

---

### 2. Room State Factory (Oda Durumu Fabrikası)

**Konum:** `src/main/java/ymt_odev/Patterns/RoomStateManager.java`

**Amaç:** Oda durumu adına göre uygun `RoomState` nesnesi oluşturmak

**Factory Method:**
```java
public static RoomState createState(String stateName) {
    ymt_odev.RoomState roomState = ymt_odev.RoomState.fromString(stateName);
    return switch (roomState) {
        case RESERVED -> new ReservedRoomState();
        case OCCUPIED -> new OccupiedRoomState();
        case CLEANING -> new CleaningRoomState();
        case MAINTENANCE -> new MaintenanceRoomState();
        default -> new AvailableRoomState();
    };
}
```

**Oluşturduğu Nesneler:**
- `AvailableRoomState` - Müsait oda durumu
- `ReservedRoomState` - Rezerve edilmiş oda durumu
- `OccupiedRoomState` - Dolu oda durumu
- `CleaningRoomState` - Temizlikte oda durumu
- `MaintenanceRoomState` - Bakımda oda durumu

**Kullanım Örneği:**
```java
// Oda durumu nesnesi oluştur
RoomState state = RoomStateManager.createState("OCCUPIED");

// Durum açıklamasını al
String description = state.getDescription();

// Oda durumunu değiştir ve veritabanını güncelle
boolean success = RoomStateManager.changeRoomState(roomId, "CLEANING");
```

**Avantajları:**
- ✅ State Pattern ile birlikte kullanılarak durum yönetimini kolaylaştırır
- ✅ Yeni oda durumu ekleme kolaylığı
- ✅ Veritabanı güncellemesi ile entegre
- ✅ Observer Pattern ile bildirim sistemi entegrasyonu

---

## 🎯 Factory Pattern'lerin Ortak Özellikleri

### 1. **Static Factory Method**
Her iki factory method da `static` olarak tanımlanmıştır, bu sayede:
- Nesne oluşturmak için sınıfın bir instance'ına ihtiyaç yoktur
- Doğrudan sınıf adı üzerinden çağrılabilir
- `PaymentProcessor.createPaymentStrategy(...)`
- `RoomStateManager.createState(...)`

### 2. **String-Based Creation**
Her iki factory method da `String` parametre alır:
- Kullanıcı arayüzünden veya veritabanından gelen değerlerle kolay entegrasyon
- Type-safe enum'lar ile birlikte kullanım
- Hata durumunda varsayılan değer döndürme

### 3. **Encapsulation (Kapsülleme)**
Factory method'lar nesne oluşturma mantığını gizler:
- İstemci kod, hangi somut sınıfın oluşturulduğunu bilmek zorunda değil
- Sadece interface/abstract class ile çalışır
- Değişiklikler factory method içinde yapılır

---

## 📚 Diğer Tasarım Desenleri ile Entegrasyon

### Factory Method + Strategy Pattern
`PaymentProcessor`, hem Factory Method hem de Strategy Pattern kullanır:
```java
// Factory Method ile strateji oluştur
PaymentStrategy strategy = PaymentProcessor.createPaymentStrategy("kredi kartı");

// Strategy Pattern ile işlem yap
PaymentProcessor processor = new PaymentProcessor();
processor.setPaymentStrategy(strategy);
processor.processPayment(reservationId, amount, customerInfo);
```

### Factory Method + State Pattern
`RoomStateManager`, hem Factory Method hem de State Pattern kullanır:
```java
// Factory Method ile durum nesnesi oluştur
RoomState state = RoomStateManager.createState("OCCUPIED");

// State Pattern ile davranış değişir
String description = state.getDescription();
boolean canBook = state.isBookable();
```

### Factory Method + Observer Pattern
`RoomStateManager`, durum değişikliklerinde Observer Pattern kullanır:
```java
// Durum değiştiğinde otomatik bildirim gönderilir
RoomStateManager.changeRoomState(roomId, "CLEANING");
// NotificationManager.getInstance().notifyRoomStateChanged(...) çağrılır
```

---

## 🚀 Yeni Factory Method Ekleme Rehberi

Eğer yeni bir factory method eklemek isterseniz:

### Adım 1: Interface veya Abstract Class Oluşturun
```java
public interface NotificationStrategy {
    void sendNotification(String message);
}
```

### Adım 2: Concrete Implementation'ları Oluşturun
```java
public class EmailNotification implements NotificationStrategy {
    public void sendNotification(String message) {
        // Email gönderme kodu
    }
}

public class SmsNotification implements NotificationStrategy {
    public void sendNotification(String message) {
        // SMS gönderme kodu
    }
}
```

### Adım 3: Factory Method Oluşturun
```java
public class NotificationFactory {
    public static NotificationStrategy createNotification(String type) {
        return switch (type.toLowerCase()) {
            case "email" -> new EmailNotification();
            case "sms" -> new SmsNotification();
            default -> new EmailNotification();
        };
    }
}
```

---

## 📖 Sonuç

Bu projede kullanılan Factory Pattern uygulamaları:

| Factory Method | Yer | Amaç | Pattern Entegrasyonu |
|----------------|-----|------|---------------------|
| `createPaymentStrategy()` | PaymentProcessor | Ödeme stratejisi oluşturma | Strategy Pattern |
| `createState()` | RoomStateManager | Oda durumu oluşturma | State Pattern + Observer Pattern |

**Toplam:** 2 adet Factory Method Pattern

**Abstract Factory Pattern:** ❌ Kullanılmamaktadır

Factory Pattern'lerin bu projede kullanılması:
- ✅ Kod tekrarını azaltır
- ✅ Yeni özellik eklemeyi kolaylaştırır
- ✅ Test edilebilirliği artırır
- ✅ SOLID prensiplerini destekler (Open/Closed Principle)
- ✅ Diğer design pattern'lerle uyumlu çalışır
