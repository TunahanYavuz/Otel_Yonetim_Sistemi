# 🏨 Otel Yönetim Sistemi (Hotel Management System)

A comprehensive hotel management system built with JavaFX and MS SQL Server. This application provides a complete solution for managing hotel operations including reservations, room management, customer management, and staff operations.

## ✨ Features

### 👥 User Management
- **Multi-role Support**: Admin, Staff, and Customer roles
- **User Authentication**: Secure login and registration system
- **Profile Management**: Users can view and update their profiles
- **Session Management**: Secure session handling across the application

### 🏨 Room Management
- **Room Types**: Support for multiple room types (Standard, Deluxe, Suite, Family, Penthouse)
- **Room States**: Track room states (Available, Reserved, Occupied, Maintenance, Cleaning)
- **Room Features**: Sea view, balcony, jacuzzi, kitchen, pet-friendly options
- **Dynamic Pricing**: Configurable pricing based on room type and features
- **Room Search**: Advanced search with filters for room type, capacity, dates, and features

### 📅 Reservation Management
- **Booking System**: Create and manage hotel reservations
- **Check-in/Check-out**: Handle guest arrivals and departures
- **Confirmation Codes**: Automatic generation of unique confirmation codes
- **Deposit Tracking**: Track deposit amounts and payment status
- **Special Requests**: Handle guest special requests and notes
- **Reservation States**: Track reservation lifecycle (Pending, Confirmed, Checked-in, Completed, Cancelled)

### 💰 Payment System
- **Multiple Payment Methods**: Cash, Credit Card, and Bank Transfer
- **Payment Processing**: Strategy pattern implementation for flexible payment handling
- **Payment Tracking**: Monitor payment status and methods

### 👨‍💼 Customer Management
- **Customer Profiles**: Comprehensive customer information management
- **Reservation History**: Track customer booking history
- **Customer Search**: Find and filter customers easily

### 📊 Reports and Analytics
- **Occupancy Reports**: Track room occupancy rates
- **Revenue Reports**: Monitor income from reservations
- **Booking Analytics**: Analyze booking patterns and trends

### 🔔 Notifications
- **Reservation Notifications**: Alerts for new bookings and changes
- **State Changes**: Notifications for room and reservation state updates
- **Observer Pattern**: Flexible notification system using observer design pattern

## 🛠️ Technology Stack

- **Java 23**: Core programming language with preview features enabled
- **JavaFX 21.0.5**: Desktop GUI framework
- **Maven**: Build and dependency management
- **MS SQL Server**: Database management system
- **JDBC Driver**: Microsoft SQL Server JDBC 13.2.1.jre11
- **Gson 2.10.1**: JSON parsing library

## 🏗️ Architecture and Design Patterns

The application implements several design patterns for maintainable and scalable code:

### Design Patterns Used
- **MVC (Model-View-Controller)**: Separation of concerns with Controllers, Services, and Domain models
- **Singleton**: Database connection management
- **Strategy Pattern**: Payment processing system
- **State Pattern**: Room state management (Available, Occupied, Maintenance, etc.)
- **Observer Pattern**: Notification system for events
- **Command Pattern**: Reservation operations (e.g., Cancel Reservation Command)

### Project Structure
```
Otel_Yonetim_Sistemi/
├── src/main/
│   ├── java/ymt_odev/
│   │   ├── Controllers/         # UI Controllers and scene management
│   │   ├── Services/            # Business logic layer
│   │   ├── Database/            # Database operations and connection management
│   │   ├── Domain/              # Domain models (Room, Reservation)
│   │   ├── Users/               # User models (Admin, Staff, Customer)
│   │   ├── Patterns/            # Design pattern implementations
│   │   └── Utils/               # Utility classes
│   └── resources/
│       ├── *.fxml               # JavaFX UI layouts
│       ├── style.css            # Application styles
│       ├── db-config.json       # Database configuration
│       └── pricing-config.json  # Room pricing configuration
├── pom.xml                      # Maven configuration
└── pricing-config.json          # Pricing configuration (root level)
```

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

Create a database named `otel_db` in your MS SQL Server instance. The application will require appropriate tables for:
- Users (Admin, Staff, Customer)
- Rooms
- Reservations
- Payment information

### 3. Configure Database Connection

Edit `src/main/resources/db-config.json`:
```json
{
  "server": "localhost",
  "port": "1433",
  "databaseName": "otel_db",
  "username": "your_username",
  "password": "your_password"
}
```

### 4. Configure Pricing (Optional)

Edit `pricing-config.json` to customize room prices:
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
1. Launch the application
2. The login screen will appear
3. Register a new account or use existing credentials
4. Different user roles will have different access levels:
   - **Admin**: Full access to all features
   - **Staff**: Access to operational features (reservations, check-in/check-out, room management)
   - **Customer**: Limited access to make reservations and view booking history

### Main Features Access

#### For Admins:
- Dashboard with overview statistics
- User management
- Room management (add, edit, delete rooms)
- Reservation management
- Customer management
- Reports and analytics
- System settings

#### For Staff:
- Room availability search
- Check-in/check-out operations
- Reservation creation and management
- Customer information lookup

#### For Customers:
- Search available rooms
- Make reservations
- View reservation history
- Manage profile

## 🔧 Configuration Files

### Database Configuration (`db-config.json`)
Contains database connection parameters including server address, port, database name, and credentials.

### Pricing Configuration (`pricing-config.json`)
Defines base prices for room types and additional prices for room features. Prices are in the local currency unit.

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Guidelines
- Follow Java coding conventions
- Maintain the existing architecture and design patterns
- Add comments for complex logic
- Test your changes thoroughly before submitting

## 📝 License

This project is available for educational purposes. Please check with the repository owner for specific licensing terms.

## 👨‍💻 Author

**Tunahan Yavuz**

## 🙏 Acknowledgments

- Built with JavaFX for a modern desktop UI experience
- Uses MS SQL Server for reliable data management
- Implements industry-standard design patterns for maintainability

## 📞 Support

For issues, questions, or suggestions, please open an issue on the GitHub repository.

---

**Note**: This is an educational project demonstrating hotel management system implementation with Java and JavaFX.
