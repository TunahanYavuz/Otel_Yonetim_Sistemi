# Abstract Factory Pattern Analizi

## ❓ Soru: "Abstract factory nerede var ve hepsini yaz"

## ✅ Cevap: Abstract Factory Pattern Kullanılmamaktadır

Bu projede **Abstract Factory Pattern YOKTUR**. 

Ancak projede **Factory Method Pattern** kullanılmaktadır.

---

## 🔍 Abstract Factory vs Factory Method

### Abstract Factory Pattern Nedir?
Abstract Factory, **birbirleriyle ilişkili veya bağımlı nesne ailelerini** oluşturmak için kullanılan bir tasarım desenidir.

**Örnek Senaryo:**
```java
// Abstract Factory ile farklı tema aileleri oluşturma
interface GUIFactory {
    Button createButton();
    TextField createTextField();
}

class WindowsFactory implements GUIFactory {
    Button createButton() { return new WindowsButton(); }
    TextField createTextField() { return new WindowsTextField(); }
}

class MacFactory implements GUIFactory {
    Button createButton() { return new MacButton(); }
    TextField createTextField() { return new MacTextField(); }
}
```

### Bu Projede Kullanılan: Factory Method Pattern
Factory Method, **tek bir ürün oluşturmak** için kullanılır.

---

## 📍 Projede Bulunan Tüm Factory Method'lar

### 1️⃣ PaymentProcessor.createPaymentStrategy()

**📂 Dosya:** `src/main/java/ymt_odev/Patterns/PaymentProcessor.java`

**📝 Kod:**
```java
/**
 * Factory method - Ödeme yöntemine göre strateji oluşturur
 */
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

**🎯 Ne İş Yapar:**
- Ödeme yöntemi string'ine göre uygun ödeme stratejisi nesnesi oluşturur
- `CreditCardPayment`, `CashPayment`, veya `BankTransferPayment` döndürür

**💡 Kullanım:**
```java
PaymentStrategy strategy = PaymentProcessor.createPaymentStrategy("kredi kartı");
```

---

### 2️⃣ RoomStateManager.createState()

**📂 Dosya:** `src/main/java/ymt_odev/Patterns/RoomStateManager.java`

**📝 Kod:**
```java
/**
 * Factory method - Durum adına göre uygun RoomState nesnesi döndürür
 */
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

**🎯 Ne İş Yapar:**
- Oda durumu string'ine göre uygun oda durumu nesnesi oluşturur
- `AvailableRoomState`, `ReservedRoomState`, `OccupiedRoomState`, `CleaningRoomState`, veya `MaintenanceRoomState` döndürür

**💡 Kullanım:**
```java
RoomState state = RoomStateManager.createState("OCCUPIED");
```

---

## 📊 Özet Tablo

| # | Factory Method | Dosya | Oluşturduğu Nesneler | Kullanılan Yerler |
|---|----------------|-------|---------------------|-------------------|
| 1 | `createPaymentStrategy()` | PaymentProcessor.java | CreditCardPayment<br>CashPayment<br>BankTransferPayment | RoomSearchController<br>ReservationsController |
| 2 | `createState()` | RoomStateManager.java | AvailableRoomState<br>ReservedRoomState<br>OccupiedRoomState<br>CleaningRoomState<br>MaintenanceRoomState | RoomManagementController<br>ReservationsController<br>CheckinCheckoutController |

---

## 🎨 UML Diyagramları

### Factory Method Pattern - Payment Strategy

```
┌─────────────────────┐
│  PaymentProcessor   │
├─────────────────────┤
│ + createPaymentStrategy(String): PaymentStrategy  ◄── Factory Method
└─────────────────────┘
          │
          │ creates
          ▼
┌─────────────────────┐
│  PaymentStrategy    │◄── Interface
├─────────────────────┤
│ + processPayment()  │
└─────────────────────┘
          △
          │ implements
    ┌─────┼─────┐
    │     │     │
┌───┴───┐ │ ┌──┴────┐
│Credit │ │ │ Cash  │
│Card   │ │ │Payment│
│Payment│ │ └───────┘
└───────┘ │
    ┌─────┴──────┐
    │BankTransfer│
    │Payment     │
    └────────────┘
```

### Factory Method Pattern - Room State

```
┌─────────────────────┐
│  RoomStateManager   │
├─────────────────────┤
│ + createState(String): RoomState  ◄── Factory Method
└─────────────────────┘
          │
          │ creates
          ▼
┌─────────────────────┐
│     RoomState       │◄── Interface
├─────────────────────┤
│ + getStateName()    │
│ + getDescription()  │
│ + isBookable()      │
└─────────────────────┘
          △
          │ implements
    ┌─────┼─────┬─────────┬──────────┐
    │     │     │         │          │
┌───┴───┐ │ ┌──┴───┐ ┌───┴────┐ ┌──┴────┐
│Avail- │ │ │Reser-│ │Occupied│ │Cleaning│
│able   │ │ │ved   │ │        │ │       │
└───────┘ │ └──────┘ └────────┘ └───────┘
     ┌────┴──────┐
     │Maintenance│
     └───────────┘
```

---

## ✅ Sonuç

**Projede toplam:** 2 adet Factory Method Pattern bulunmaktadır

**Abstract Factory Pattern:** ❌ Yok

**Factory Method Pattern'ler:**
1. ✅ `PaymentProcessor.createPaymentStrategy()` - Ödeme stratejisi fabrikası
2. ✅ `RoomStateManager.createState()` - Oda durumu fabrikası

**Detaylı döküman:** Daha fazla bilgi için [FACTORY_PATTERNS.md](FACTORY_PATTERNS.md) dosyasına bakın.

---

## 🤔 Neden Abstract Factory Kullanılmamış?

Abstract Factory Pattern genellikle şu durumlarda kullanılır:
- Birbirleriyle ilişkili nesne aileleri oluşturulacaksa
- Farklı platformlar veya temalar desteklenecekse
- Cross-platform GUI uygulamaları yapılacaksa

Bu otel yönetim sisteminde:
- ✅ Her factory method tek bir tür nesne oluşturur
- ✅ Platformlar arası uyumluluk gerekmez
- ✅ Factory Method Pattern yeterli ve daha basittir
- ✅ YAGNI prensibi: "You Aren't Gonna Need It" - Kullanılmayacak karmaşıklık eklenmemiş

Bu nedenle **Factory Method Pattern** kullanımı doğru ve yeterli bir seçimdir.
