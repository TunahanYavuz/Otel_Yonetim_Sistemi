# 🏨 Otel Yönetim Sistemi (Hotel Management System)

A comprehensive, enterprise-grade hotel management system built with JavaFX and MS SQL Server. This application provides a complete solution for managing hotel operations including reservations, room management, customer management, staff operations, and real-time analytics. The system features a modern, multilingual interface (Turkish/English support) with role-based access control and dynamic configuration management.

## ✨ Features

### 👥 User Management
- **Multi-role Support**: Three distinct user types (Admin, Staff/Personel, Customer/Müşteri)
- **User Authentication**: Secure login and registration system with password protection
- **Profile Management**: Users can view and update their personal information
- **Session Management**: Secure session handling across the application with SessionManager
- **User CRUD Operations**: Full create, read, update, and delete capabilities for user accounts
- **Active/Inactive Status**: Ability to activate or deactivate user accounts

### 🏨 Room Management
- **Room Types**: Support for multiple room types:
  - Standart (Standard rooms)
  - Deluxe (Premium rooms)
  - Suite (Luxury suites)
  - Aile (Family rooms)
  - Penthouse (Top-tier accommodations)
- **Room States**: Complete state management using State Pattern:
  - AVAILABLE (Müsait) - Ready for booking
  - RESERVED (Rezerve) - Reserved but not checked in
  - OCCUPIED (Dolu) - Currently occupied
  - MAINTENANCE (Bakımda) - Under maintenance
  - CLEANING (Temizlikte) - Being cleaned
- **Room Features**: Comprehensive amenity tracking:
  - Sea view (Deniz manzarası)
  - Balcony (Balkon)
  - Jacuzzi (Jakuzi)
  - Kitchen (Mutfak)
  - Pet-friendly (Evcil hayvan dostu)
- **Dynamic Pricing**: Automatic price calculation based on:
  - Room type base price
  - Additional feature pricing
  - Configurable through JSON files
- **Advanced Room Search**: Multi-criteria search with filters:
  - Room type
  - Guest capacity
  - Check-in/check-out dates
  - Specific features
  - Availability status
  - Floor level
- **Room CRUD Operations**: Full room management capabilities
- **Conflict Detection**: Automatic detection of overlapping reservations

### 📅 Reservation Management
- **Comprehensive Booking System**: End-to-end reservation lifecycle management
- **Check-in/Check-out**: Streamlined guest arrival and departure handling with timestamp tracking
- **Confirmation Codes**: Automatic generation of unique alphanumeric confirmation codes
- **Deposit Tracking**: Monitor deposit amounts and payment status
- **Special Requests**: Handle and track guest special requests and notes
- **Reservation States**: Complete lifecycle tracking:
  - PENDING (Beklemede) - Awaiting confirmation
  - CONFIRMED (Onaylandı) - Confirmed booking
  - CHECKED_IN (Giriş Yapıldı) - Guest has arrived
  - COMPLETED (Tamamlandı) - Stay completed
  - CANCELLED (İptal Edildi) - Cancelled reservation
- **Date Range Management**: Check-in and check-out date validation
- **Guest Count Tracking**: Monitor number of guests per reservation
- **Total Price Calculation**: Automatic pricing with date range and room feature consideration
- **My Reservations View**: Customers can view their booking history
- **Reservation Search**: Search and filter reservations by various criteria
- **Conflict Prevention**: Automatic validation to prevent double-booking

### 💰 Payment System
- **Multiple Payment Methods**: Strategy Pattern implementation supporting:
  - Cash (Nakit) payment
  - Credit Card (Kredi Kartı) payment
  - Bank Transfer (Banka Havalesi) payment
- **Payment Processing**: Flexible payment handling through PaymentProcessor
- **Payment Tracking**: Monitor payment status and methods for each reservation
- **Payment History**: Complete payment transaction records
- **Payment Status**: Track paid/unpaid status for all reservations

### 👨‍💼 Customer Management
- **Customer Profiles**: Comprehensive customer information:
  - Personal details (name, contact information)
  - National ID tracking
  - Email and phone number
  - Member since date
  - Active/inactive status
- **Reservation History**: Complete booking history for each customer
- **Customer Search**: Advanced search and filtering capabilities
- **Customer Service Integration**: Link customer profiles with reservations and services
- **Customer CRUD Operations**: Full customer lifecycle management

### 📊 Reports and Analytics
- **Real-time Dashboard**: Live statistics showing:
  - Available rooms count
  - Active reservations count
  - Total customers count
  - Welcome message with current user info
  - Current date and time display
- **Occupancy Reports**: Detailed room occupancy rate calculations and tracking
- **Revenue Reports**: Comprehensive income analysis from reservations with total revenue display
- **Booking Analytics**: Analyze booking patterns and trends over time
- **Report Types**: Multiple report categories:
  - Gelir Raporu (Revenue Report)
  - Doluluk Raporu (Occupancy Report)
  - Müşteri Raporu (Customer Report)
  - Oda Performans (Room Performance)
- **Date Range Filtering**: Generate reports for specific time periods
- **Export Capabilities**: View and analyze report results

### 🔔 Notification System
- **Observer Pattern Implementation**: Decoupled notification architecture
- **Database-backed Notifications**: Persistent notification storage in Notifications table
- **Multi-type Notifications**:
  - Reservation creation alerts
  - Reservation confirmation
  - Check-in/check-out notifications
  - State change alerts (room and reservation status updates)
  - System notifications
- **User-specific Notifications**: Targeted notifications by user ID and type
- **Notification Types**: CUSTOMER, STAFF, ADMIN specific notifications
- **Read/Unread Status**: Track notification read status
- **Real-time Updates**: Immediate notification delivery through observer pattern

### ⚙️ System Settings
- **Hotel Configuration**: Manage hotel-wide settings:
  - Hotel name
  - Hotel address
  - Hotel phone number
- **Database Configuration Management**: Runtime database settings modification:
  - Server address
  - Port number
  - Database name
  - Username and password
  - Connection testing
  - Dynamic reconnection without restart
- **Pricing Configuration**: Live pricing updates through UI:
  - Room type base prices (all 5 types)
  - Feature surcharges (4 features)
  - JSON-based configuration persistence
- **Notification Preferences**: Email and SMS notification toggles
- **Configuration Persistence**: All settings saved to JSON files:
  - `db-config.json` for database settings
  - `pricing-config.json` for room pricing

### 🔐 Security & Access Control
- **Role-based Access Control**: Different features accessible based on user role
- **Secure Authentication**: Password-protected login system
- **Session Management**: Secure session tracking with automatic logout
- **Access Enum**: Type-safe access level management (ADMIN, STAFF, CUSTOMER)

## 🛠️ Technology Stack

- **Java 23**: Core programming language with preview features enabled
- **JavaFX 21.0.5**: Desktop GUI framework
- **Maven**: Build and dependency management
- **MS SQL Server**: Database management system
- **JDBC Driver**: Microsoft SQL Server JDBC 13.2.1.jre11
- **Gson 2.10.1**: JSON parsing library

## 🏗️ Architecture and Design Patterns

The application implements several design patterns for maintainable, scalable, and testable code:

### Design Patterns Used

#### 1. **MVC (Model-View-Controller)** - Application Structure
- **Models**: Domain entities (`Room`, `Reservation`, `User`, `Customer`, `Admin`, `Staff`)
- **Views**: FXML-based UI layouts (14 FXML files for different screens)
- **Controllers**: UI logic and user interaction handling (19 controller classes)
- **Services**: Business logic layer (`RoomService`, `ReservationService`, `CustomerService`, `UserService`)

#### 2. **Singleton Pattern** - Resource Management
- **DatabaseConnection**: Single instance of database connection with connection pooling
- **NotificationManager**: Centralized notification management
- **ConfigManager**: Single instance for configuration file management
- Implementation: Thread-safe getInstance() methods with synchronized blocks

#### 3. **Strategy Pattern** - Payment Processing
- **PaymentStrategy**: Interface for payment methods
- **CashPayment**: Cash payment implementation
- **CreditCardPayment**: Credit card payment implementation
- **BankTransferPayment**: Bank transfer implementation
- **PaymentProcessor**: Context class that uses payment strategies

#### 4. **State Pattern** - Room State Management
- **RoomState**: Abstract state interface
- **AvailableRoomState**: Room is ready for booking
- **ReservedRoomState**: Room is reserved
- **OccupiedRoomState**: Room is currently occupied
- **MaintenanceRoomState**: Room is under maintenance
- **CleaningRoomState**: Room is being cleaned
- **RoomStateManager**: Manages state transitions and validates state changes

#### 5. **Observer Pattern** - Notification System
- **NotificationObserver**: Observer interface
- **NotificationManager**: Subject that manages observers
- **Features**: Real-time notifications for reservation events, room state changes
- **Persistence**: Notifications stored in database for history

#### 6. **Command Pattern** - Operation Management
- **Command**: Command interface
- **CancelReservationCommand**: Encapsulates reservation cancellation logic
- **Benefits**: Undo/redo capability, operation queuing, logging

#### 7. **Repository Pattern** - Data Access
- **DatabaseManager**: Abstract interface for database operations
- **DBDataSelection**: Read operations
- **DBDataInsertion**: Create operations
- **DBDataUpdater**: Update operations
- **DBDataDeleter**: Delete operations
- **IDatabaseCommands**: Common interface for all database operations

#### 8. **Factory Pattern** - Object Creation
- Used in creating different user types (Admin, Staff, Customer)
- Room state creation and management

### Layered Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Presentation Layer                    │
│  (FXML Views + Controllers + SceneController)           │
├─────────────────────────────────────────────────────────┤
│                   Business Logic Layer                   │
│     (Services + Patterns + Domain Models)               │
├─────────────────────────────────────────────────────────┤
│                    Data Access Layer                     │
│          (Database Package + DatabaseManager)            │
├─────────────────────────────────────────────────────────┤
│                   Utility & Config Layer                 │
│       (ConfigManager + AlertManager + Utils)            │
└─────────────────────────────────────────────────────────┘
```

### Project Structure
```
Otel_Yonetim_Sistemi/
├── src/main/
│   ├── java/ymt_odev/
│   │   ├── Controllers/              # UI Controllers (19 classes)
│   │   │   ├── Main.java            # Application entry point
│   │   │   ├── AuthController.java   # Login & Registration
│   │   │   ├── DashboardController.java  # Main dashboard
│   │   │   ├── RoomManagementController.java
│   │   │   ├── RoomSearchController.java
│   │   │   ├── ReservationsController.java
│   │   │   ├── MyReservationsController.java
│   │   │   ├── CheckinCheckoutController.java
│   │   │   ├── CustomerManagementController.java
│   │   │   ├── UserManagementController.java
│   │   │   ├── ReportsController.java
│   │   │   ├── SettingsController.java
│   │   │   ├── ProfileController.java
│   │   │   ├── SceneController.java  # Scene management
│   │   │   ├── SessionManager.java   # Session handling
│   │   │   ├── BaseController.java   # Base controller class
│   │   │   └── MainController.java
│   │   │
│   │   ├── Services/                 # Business Logic (4 services)
│   │   │   ├── RoomService.java     # Room search & management
│   │   │   ├── ReservationService.java  # Reservation operations
│   │   │   ├── CustomerService.java  # Customer operations
│   │   │   └── UserService.java     # User operations
│   │   │
│   │   ├── Database/                 # Data Access Layer (8 classes)
│   │   │   ├── DatabaseConnection.java    # Singleton connection
│   │   │   ├── DatabaseManager.java       # Abstract manager
│   │   │   ├── IDatabaseCommands.java     # Interface
│   │   │   ├── DBDataSelection.java       # SELECT operations
│   │   │   ├── DBDataInsertion.java       # INSERT operations
│   │   │   ├── DBDataUpdater.java         # UPDATE operations
│   │   │   ├── DBDataDeleter.java         # DELETE operations
│   │   │   └── DataSelectionExample.java  # Usage examples
│   │   │
│   │   ├── Domain/                   # Domain Models (2 models)
│   │   │   ├── Room.java            # Room entity
│   │   │   └── Reservation.java     # Reservation entity
│   │   │
│   │   ├── Users/                    # User Models (4 classes)
│   │   │   ├── User.java            # Abstract user class
│   │   │   ├── Admin.java           # Admin user
│   │   │   ├── Staff.java           # Staff user
│   │   │   └── Customer.java        # Customer user
│   │   │
│   │   ├── Patterns/                 # Design Patterns (15 classes)
│   │   │   ├── PaymentStrategy.java      # Strategy interface
│   │   │   ├── CashPayment.java          # Cash strategy
│   │   │   ├── CreditCardPayment.java    # Card strategy
│   │   │   ├── BankTransferPayment.java  # Transfer strategy
│   │   │   ├── PaymentProcessor.java     # Strategy context
│   │   │   ├── RoomState.java            # State interface
│   │   │   ├── AvailableRoomState.java
│   │   │   ├── ReservedRoomState.java
│   │   │   ├── OccupiedRoomState.java
│   │   │   ├── MaintenanceRoomState.java
│   │   │   ├── CleaningRoomState.java
│   │   │   ├── RoomStateManager.java     # State manager
│   │   │   ├── NotificationObserver.java # Observer interface
│   │   │   ├── NotificationManager.java  # Observer subject
│   │   │   ├── Command.java              # Command interface
│   │   │   └── CancelReservationCommand.java
│   │   │
│   │   ├── Utils/                    # Utilities (1 class)
│   │   │   └── ConfigManager.java   # JSON config management
│   │   │
│   │   ├── Access.java              # Role enum (ADMIN, STAFF, CUSTOMER)
│   │   └── AlertManager.java        # UI Alert helper
│   │
│   └── resources/                    # UI & Configuration
│       ├── login.fxml                # Login screen
│       ├── register.fxml             # Registration screen
│       ├── main.fxml                 # Main layout
│       ├── dashboard.fxml            # Dashboard view
│       ├── room-management.fxml      # Room management UI
│       ├── room-search.fxml          # Room search UI
│       ├── reservation-management.fxml  # Reservation management
│       ├── my-reservations.fxml      # User reservations view
│       ├── checkin-checkout.fxml     # Check-in/out UI
│       ├── customer-management.fxml  # Customer management UI
│       ├── user-management.fxml      # User management UI
│       ├── reports.fxml              # Reports & analytics
│       ├── settings.fxml             # Settings UI
│       ├── profile.fxml              # Profile management
│       ├── style.css                 # Application styles
│       ├── db-config.json            # Database configuration
│       └── pricing-config.json       # Pricing configuration
│
├── pom.xml                           # Maven configuration
└── pricing-config.json               # Root-level pricing config
```

### Key Architectural Decisions

1. **Separation of Concerns**: Clear separation between UI (Controllers), business logic (Services), and data access (Database package)
2. **Configuration Management**: Externalized configuration through JSON files with hot-reload capability
3. **Connection Pooling**: Singleton database connection with automatic reconnection
4. **Type Safety**: Enum-based access control and state management
5. **Extensibility**: Pattern-based design allows easy addition of new payment methods, room states, etc.
6. **Maintainability**: Base controller class for common functionality across all controllers

## 📋 Prerequisites

- **Java Development Kit (JDK) 23** (required for preview features and compilation settings)
- **Apache Maven 3.6+**
- **MS SQL Server** (local or remote instance)
- **JavaFX SDK 21.0.5** (automatically managed by Maven)

## 🚀 Installation and Setup

### 1. Clone the Repository
```bash
git clone https://github.com/TunahanYavuz/Otel_Yonetim_Sistemi.git
cd Otel_Yonetim_Sistemi
```

### 2. Database Setup

Create a database named `otel_db` in your MS SQL Server instance. The application requires the following tables:

#### Required Database Tables:

**Users Table**
- `id` (INT, PRIMARY KEY, IDENTITY)
- `username` (VARCHAR)
- `password` (VARCHAR)
- `email` (VARCHAR)
- `phone` (VARCHAR)
- `nationalId` (VARCHAR)
- `role` (VARCHAR) - Values: 'ADMIN', 'STAFF', 'CUSTOMER'
- `firstName` (VARCHAR)
- `lastName` (VARCHAR)
- `memberSince` (DATETIME)
- `isActive` (BIT)

**Rooms Table**
- `id` (INT, PRIMARY KEY, IDENTITY)
- `roomNumber` (VARCHAR, UNIQUE)
- `roomType` (VARCHAR) - Values: 'Standart', 'Deluxe', 'Suite', 'Aile', 'Penthouse'
- `capacity` (INT)
- `pricePerNight` (DECIMAL)
- `features` (VARCHAR)
- `floor` (INT)
- `hasBalcony` (BIT)
- `hasSeaView` (BIT)
- `hasKitchen` (BIT)
- `hasJacuzzi` (BIT)
- `isPetFriendly` (BIT)
- `state` (VARCHAR) - Values: 'AVAILABLE', 'RESERVED', 'OCCUPIED', 'MAINTENANCE', 'CLEANING'
- `description` (TEXT)

**Reservations Table**
- `id` (INT, PRIMARY KEY, IDENTITY)
- `customerId` (INT, FOREIGN KEY to Users)
- `roomId` (INT, FOREIGN KEY to Rooms)
- `checkInDate` (DATE)
- `checkOutDate` (DATE)
- `checkInTime` (DATETIME)
- `checkOutTime` (DATETIME)
- `guestCount` (INT)
- `totalPrice` (DECIMAL)
- `depositAmount` (DECIMAL)
- `isPaid` (BIT)
- `paymentMethod` (VARCHAR) - Values: 'CASH', 'CREDIT_CARD', 'BANK_TRANSFER'
- `state` (VARCHAR) - Values: 'PENDING', 'CONFIRMED', 'CHECKED_IN', 'COMPLETED', 'CANCELLED'
- `confirmationCode` (VARCHAR, UNIQUE)
- `specialRequests` (TEXT)
- `notes` (TEXT)
- `createdAt` (DATETIME)

**Notifications Table**
- `id` (INT, PRIMARY KEY, IDENTITY)
- `userId` (INT, FOREIGN KEY to Users)
- `userType` (VARCHAR) - Values: 'ADMIN', 'STAFF', 'CUSTOMER'
- `title` (VARCHAR)
- `message` (TEXT)
- `notificationType` (VARCHAR)
- `isRead` (BIT)
- `createdAt` (DATETIME, DEFAULT GETDATE())

### 3. Configure Database Connection

The application uses `ConfigManager` to load database configuration from JSON files. The config file will be automatically created if it doesn't exist.

Edit `src/main/resources/db-config.json` or create it in the project root:
```json
{
  "server": "localhost",
  "port": "1433",
  "databaseName": "otel_db",
  "username": "your_username",
  "password": "your_password"
}
```

**Note**: The application uses the following connection string format:
```
jdbc:sqlserver://server:port;databaseName=dbname;encrypt=false;trustServerCertificate=true;integratedSecurity=false;
```

You can also configure the database connection at runtime through the Settings screen in the application.

### 4. Configure Pricing (Optional)

The application supports dynamic pricing configuration through JSON files. The config file can be placed in either the project root or `src/main/resources/`.

Edit `pricing-config.json`:
```json
{
  "roomTypePrices": {
    "Standart": 500.0,
    "Deluxe": 800.0,
    "Suite": 1200.0,
    "Aile": 1000.0,
    "Penthouse": 2500.0
  },
  "featurePrices": {
    "seaView": 150.0,
    "balcony": 100.0,
    "jacuzzi": 200.0,
    "kitchen": 120.0
  }
}
```

**Pricing Calculation**: The final room price is calculated as:
```
Total Price = Base Room Type Price + Sum of Feature Prices
```

**Example**: A Deluxe room with sea view and balcony:
```
800.0 (Deluxe) + 150.0 (seaView) + 100.0 (balcony) = 1050.0 per night
```

You can also modify pricing at runtime through the Settings screen in the application.

### 5. Build the Project
```bash
mvn clean install
```

### 6. Run the Application
```bash
mvn javafx:run
```

## 📖 Usage

### First Time Setup
1. Launch the application using `mvn javafx:run`
2. The login screen (`login.fxml`) will appear
3. Click on "Kayıt Ol" (Register) to create a new account
4. Enter your details and select a role (Admin/Staff/Customer)
5. After registration, log in with your credentials
6. Different user roles will have different access levels determined by the `Access` enum

### User Roles and Capabilities

#### 🔴 Admin (Full System Access)
Admins have complete control over the hotel management system:

**Dashboard**
- View real-time statistics (available rooms, active reservations, total customers)
- Monitor system activity with current date/time display
- Quick access to key features

**User Management** (`UserManagementController`)
- Create, read, update, delete user accounts
- Assign and modify user roles (Admin, Staff, Customer)
- Activate/deactivate user accounts
- View all registered users
- Reset user passwords

**Room Management** (`RoomManagementController`)
- Add new rooms with complete details (type, capacity, features, floor)
- Edit existing room information
- Update room pricing
- Change room states (Available, Maintenance, Cleaning)
- Delete rooms from the system
- View all rooms with filtering options

**Reservation Management** (`ReservationsController`)
- View all reservations across the system
- Create reservations for customers
- Modify existing reservations
- Cancel reservations
- Check reservation status and details
- Monitor payment status

**Customer Management** (`CustomerManagementController`)
- View all customer profiles
- Edit customer information
- View customer reservation history
- Search and filter customers
- Track customer activity

**Reports and Analytics** (`ReportsController`)
- Generate revenue reports (total income from paid reservations)
- View occupancy reports (room utilization rates)
- Customer analysis reports
- Room performance metrics
- Filter reports by date range
- Export report data

**System Settings** (`SettingsController`)
- Configure hotel information (name, address, phone)
- Modify database connection settings
- Update pricing configuration for all room types
- Adjust feature pricing
- Set notification preferences
- Test database connectivity

**Check-in/Check-out** (`CheckinCheckoutController`)
- Process guest arrivals
- Handle guest departures
- Update reservation timestamps
- Manage room state transitions

#### 🟡 Staff/Personel (Operational Access)
Staff members handle day-to-day hotel operations:

**Dashboard**
- View current statistics
- Quick access to operational features

**Room Search** (`RoomSearchController`)
- Search available rooms by:
  - Check-in and check-out dates
  - Number of guests
  - Room type
  - Specific features (balcony, sea view, etc.)
- View room details and pricing
- Check room availability in real-time

**Reservation Creation**
- Create new reservations for walk-in customers
- Select rooms based on availability
- Enter guest information
- Calculate total pricing
- Process payments
- Generate confirmation codes

**Check-in/Check-out Operations**
- Check guests in upon arrival
- Process departures
- Update room states
- Handle early check-ins/late check-outs

**Customer Lookup**
- Search customer information
- View customer reservation history
- Update customer contact details

**Reservation Management**
- View and manage reservations
- Modify booking details
- Handle cancellations
- Process special requests

#### 🟢 Customer/Müşteri (Guest Access)
Customers can self-manage their bookings:

**Room Search** (`RoomSearchController`)
- Search available rooms for desired dates
- Filter by room type, capacity, and features
- View room descriptions and pricing
- Compare different room options

**Make Reservations**
- Select desired room
- Choose check-in and check-out dates
- Specify number of guests
- Add special requests
- View total price calculation
- Receive confirmation code upon booking

**My Reservations** (`MyReservationsController`)
- View all personal reservations
- Check booking status (Pending, Confirmed, Checked-in, Completed)
- View confirmation codes
- Access reservation details
- Review past stays

**Profile Management** (`ProfileController`)
- Update personal information
- Change password
- Modify contact details
- View account information

### Navigation
The application uses `SceneController` for navigation between different views:
- Automatic scene switching based on user actions
- Context-aware navigation menus
- Role-based view access
- Smooth transitions between screens

### Window Configuration
- Initial size: 900x650 pixels
- Minimum size: 800x600 pixels
- Resizable window
- Centered on screen at startup
- Title: "🏨 Otel Yönetim Sistemi - [Screen Name]"

## 🔧 Configuration Files

### Database Configuration (`db-config.json`)
Located in `src/main/resources/` or project root. Managed by `ConfigManager` utility class.

**Structure**:
```json
{
  "server": "localhost",
  "port": "1433",
  "databaseName": "otel_db",
  "username": "otel",
  "password": "123456"
}
```

**Features**:
- Automatic creation with default values if not found
- Hot-reload capability (changes apply without restart when modified through Settings UI)
- Cached in memory for performance
- Thread-safe access through `ConfigManager.loadDbConfig()`
- Connection string generation: `ConfigManager.DbConfig.getConnectionUrl()`
- Runtime modification through Settings screen
- Automatic file copy from resources to user directory if needed

### Pricing Configuration (`pricing-config.json`)
Located in `src/main/resources/` or project root. Managed by `ConfigManager` utility class.

**Structure**:
```json
{
  "roomTypePrices": {
    "Standart": 500.0,
    "Deluxe": 800.0,
    "Suite": 1200.0,
    "Aile": 1000.0,
    "Penthouse": 2500.0
  },
  "featurePrices": {
    "seaView": 150.0,
    "balcony": 100.0,
    "jacuzzi": 200.0,
    "kitchen": 120.0
  }
}
```

**Features**:
- Automatic price calculation through `PricingConfig.calculatePrice()`
- Runtime modification through Settings UI
- Cached in memory for performance
- Default values automatically created if file doesn't exist
- Supports dynamic pricing strategies
- All prices in local currency (₺ - Turkish Lira by default)

**Price Calculation Example**:
```java
ConfigManager.PricingConfig config = ConfigManager.loadPricingConfig();
double price = config.calculatePrice("Deluxe", true, true, false, false);
// Returns: 1050.0 (800 + 150 for sea view + 100 for balcony)
```

### Configuration Management Best Practices
1. **Backup**: Always backup configuration files before modification
2. **Version Control**: Add `db-config.json` to `.gitignore` to protect credentials
3. **Testing**: Test database connectivity through Settings UI before full deployment
4. **Caching**: Use `ConfigManager.clearCache()` to force config reload if needed
5. **Validation**: Application validates configuration on startup and provides error messages

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Guidelines
- Follow Java coding conventions and naming standards
- Maintain the existing architecture and design patterns
- Add JavaDoc comments for public methods and classes
- Keep controllers thin - business logic belongs in Services
- Use appropriate design patterns for new features
- Test your changes thoroughly before submitting
- Update this README if adding new features or changing architecture
- Ensure database operations use the DatabaseManager abstraction
- Handle exceptions appropriately with user-friendly error messages via AlertManager

### Adding New Features

**Adding a New Payment Method**:
1. Create a new class implementing `PaymentStrategy`
2. Implement the `processPayment()` method
3. Register the strategy with `PaymentProcessor`

**Adding a New Room State**:
1. Create a new class extending `RoomState`
2. Implement state-specific behavior
3. Update `RoomStateManager` to handle the new state
4. Add state to database schema

**Adding a New Report Type**:
1. Add report type to `ReportsController` combo box
2. Implement data collection logic in appropriate Service
3. Format and display results in the report area

### Code Structure Guidelines
```
Controllers/     → Handle UI interactions, delegate to Services
Services/        → Business logic, data validation, coordinate between layers
Database/        → Data access only, no business logic
Domain/          → Pure data models, immutable where possible
Patterns/        → Design pattern implementations, reusable components
Utils/           → Helper classes, utilities, configuration management
```

## 🔧 Troubleshooting

### Common Issues and Solutions

**Database Connection Failed**
- Verify MS SQL Server is running
- Check `db-config.json` credentials
- Ensure SQL Server allows TCP/IP connections
- Verify SQL Server port (default: 1433)
- Check Windows Firewall settings
- Test connection through Settings → Database Configuration → Test Connection

**Application Won't Start**
- Ensure JDK 23 is installed and JAVA_HOME is set
- Run `mvn clean install` to rebuild
- Check for port conflicts if database is local
- Verify all dependencies are downloaded: `mvn dependency:resolve`
- Check console output for specific error messages

**Pricing Not Updating**
- Verify `pricing-config.json` syntax is valid JSON
- Clear pricing cache: restart application or use Settings UI
- Check file permissions (read/write access required)
- Ensure prices are positive numbers

**Login Issues**
- Verify database connection is working
- Check Users table exists and has records
- Ensure role values match enum: 'ADMIN', 'STAFF', or 'CUSTOMER'
- Password comparison is case-sensitive

**Reservation Conflicts**
- System automatically prevents double-booking
- Check-in date must be before check-out date
- Room must be in AVAILABLE state
- Verify date formats match: yyyy-MM-dd

**Performance Issues**
- DatabaseConnection uses connection pooling (Singleton pattern)
- ConfigManager caches configurations in memory
- Consider adding indexes on frequently queried columns
- Monitor database query performance

### Debug Mode
Enable detailed logging by checking console output. The application uses emoji-prefixed logging:
- ✅ Success operations
- ❌ Error conditions
- 🔄 Ongoing operations
- 🔒 Security-related events
- 📝 Configuration changes

## 🧪 Testing

### Manual Testing Checklist
- [ ] User registration and login for all three roles
- [ ] Room creation with all features enabled
- [ ] Room search with various filter combinations
- [ ] Reservation creation and confirmation code generation
- [ ] Check-in and check-out operations
- [ ] Payment processing with all three methods
- [ ] Room state transitions (Available → Reserved → Occupied → Cleaning → Available)
- [ ] Notification creation and delivery
- [ ] Report generation for all report types
- [ ] Settings modification (database and pricing)
- [ ] Configuration file persistence
- [ ] Multi-user session handling

### Test Data Setup
1. Create admin user: role='ADMIN'
2. Create staff user: role='STAFF'
3. Create customer user: role='CUSTOMER'
4. Add rooms of each type (Standart, Deluxe, Suite, Aile, Penthouse)
5. Create test reservations in different states
6. Verify all room features work (balcony, sea view, etc.)

## 📊 Database Schema Diagram

```
┌──────────────────┐         ┌──────────────────┐
│     Users        │         │      Rooms       │
├──────────────────┤         ├──────────────────┤
│ id (PK)          │         │ id (PK)          │
│ username         │         │ roomNumber       │
│ password         │         │ roomType         │
│ email            │         │ capacity         │
│ phone            │         │ pricePerNight    │
│ nationalId       │         │ features         │
│ role             │         │ floor            │
│ firstName        │         │ hasBalcony       │
│ lastName         │         │ hasSeaView       │
│ memberSince      │         │ hasKitchen       │
│ isActive         │         │ hasJacuzzi       │
└──────────────────┘         │ isPetFriendly    │
        │                    │ state            │
        │                    │ description      │
        │                    └──────────────────┘
        │                            │
        │                            │
        └────────┬───────────────────┘
                 │
                 ▼
        ┌──────────────────┐         ┌──────────────────┐
        │  Reservations    │         │  Notifications   │
        ├──────────────────┤         ├──────────────────┤
        │ id (PK)          │         │ id (PK)          │
        │ customerId (FK)  │◄────────┤ userId (FK)      │
        │ roomId (FK)      │         │ userType         │
        │ checkInDate      │         │ title            │
        │ checkOutDate     │         │ message          │
        │ checkInTime      │         │ notificationType │
        │ checkOutTime     │         │ isRead           │
        │ guestCount       │         │ createdAt        │
        │ totalPrice       │         └──────────────────┘
        │ depositAmount    │
        │ isPaid           │
        │ paymentMethod    │
        │ state            │
        │ confirmationCode │
        │ specialRequests  │
        │ notes            │
        │ createdAt        │
        └──────────────────┘
```

## 📝 License

This project is available for educational purposes. Please check with the repository owner for specific licensing terms.

## 👨‍💻 Author

**Tunahan Yavuz**
- GitHub: [@TunahanYavuz](https://github.com/TunahanYavuz)

## 📈 Project Statistics

- **Total Java Classes**: 54
- **FXML Views**: 14
- **Design Patterns**: 8 (MVC, Singleton, Strategy, State, Observer, Command, Repository, Factory)
- **Controllers**: 19
- **Services**: 4
- **Database Operations**: 5 (Select, Insert, Update, Delete, Interface)
- **User Roles**: 3 (Admin, Staff, Customer)
- **Room Types**: 5
- **Room States**: 5
- **Payment Methods**: 3
- **Report Types**: 4

## 🎯 Technical Highlights

### Performance Optimizations
- **Connection Pooling**: Singleton pattern for database connection reuse
- **Configuration Caching**: In-memory caching of JSON configurations
- **Lazy Loading**: On-demand data fetching for improved startup time
- **Stream API**: Java Streams for efficient data processing and filtering

### Security Features
- **Password Protection**: Secure password storage for user accounts
- **Role-based Access**: Enum-based access control (ADMIN, STAFF, CUSTOMER)
- **Session Management**: `SessionManager` tracks current user context
- **SQL Injection Prevention**: Prepared statements through JDBC
- **Connection Encryption**: Option for encrypted database connections

### Error Handling
- **AlertManager**: Centralized alert system for user notifications
- **Try-Catch Blocks**: Comprehensive exception handling throughout
- **Null Safety**: Null checks before operations
- **Database Validation**: Connection validation before queries
- **User-friendly Messages**: Clear error messages in Turkish

### Data Integrity
- **Foreign Keys**: Referential integrity between tables
- **Unique Constraints**: Prevent duplicate room numbers and confirmation codes
- **Date Validation**: Ensures check-in before check-out
- **State Validation**: State pattern ensures valid state transitions
- **Conflict Detection**: Prevents double-booking of rooms

## 🌐 Internationalization

Currently, the application interface is in **Turkish** with the following key terms:

| English | Turkish |
|---------|---------|
| Available | Müsait |
| Occupied | Dolu |
| Cleaning | Temizlikte |
| Maintenance | Bakımda |
| Standard | Standart |
| Family | Aile |
| Admin | Admin |
| Staff | Personel |
| Customer | Müşteri |
| Revenue Report | Gelir Raporu |
| Occupancy Report | Doluluk Raporu |

The architecture supports easy internationalization by externalizing strings to resource bundles in future versions.

## 🙏 Acknowledgments

- **JavaFX**: Modern desktop UI framework enabling rich user interfaces
- **MS SQL Server**: Reliable enterprise database management system
- **Microsoft JDBC Driver**: Robust database connectivity
- **Gson Library**: Efficient JSON parsing and configuration management
- **Maven**: Simplified build and dependency management
- **Design Patterns**: Gang of Four patterns for maintainable architecture
- **MVC Architecture**: Clear separation of concerns for scalability

## 🚀 Future Enhancements

Potential features for future development:
- [ ] Email notifications for reservation confirmations
- [ ] SMS integration for booking reminders
- [ ] Online payment gateway integration
- [ ] Multi-language support (English, German, Arabic)
- [ ] Mobile application (iOS/Android)
- [ ] Web-based admin panel
- [ ] Advanced analytics dashboard with charts
- [ ] Room service management
- [ ] Housekeeping task management
- [ ] Inventory management system
- [ ] Integration with booking.com and other OTAs
- [ ] QR code-based check-in
- [ ] Guest feedback system
- [ ] Loyalty program management
- [ ] Revenue management system
- [ ] Automated pricing optimization

## 📞 Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

### Getting Help
- Check this README first for setup and usage instructions
- Review the [Troubleshooting](#-troubleshooting) section
- Search existing GitHub issues
- Create a new issue with:
  - Detailed description of the problem
  - Steps to reproduce
  - Expected vs actual behavior
  - System information (OS, Java version, SQL Server version)
  - Relevant error messages or logs

---

**Note**: This is an educational project demonstrating hotel management system implementation with Java and JavaFX, showcasing modern software design patterns and best practices for enterprise application development.
