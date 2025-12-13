package ymt_odev.Domain;

import ymt_odev.Database.DatabaseConnection;
import ymt_odev.Database.DatabaseManager;
import ymt_odev.ReservationState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rezervasyon domain modeli
 */
public class Reservation {
    private final int id;
    private final int customerId;
    private final int roomId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private final int guestCount;
    private final double totalPrice;
    private double depositAmount;
    private boolean isPaid;
    private String paymentMethod;
    private String state;
    private final String confirmationCode;
    private String specialRequests;
    private String notes;
    private final LocalDateTime createdAt;

    // Constructor
    public Reservation(int id, int customerId, int roomId,
                       LocalDate checkInDate, LocalDate checkOutDate,
                       LocalDateTime checkInTime, LocalDateTime checkOutTime,
                       int guestCount, double totalPrice, double depositAmount,
                       boolean isPaid, String paymentMethod, String state,
                       String confirmationCode, String specialRequests,
                       String notes, LocalDateTime createdAt) {
        this.id = id;
        this.customerId = customerId;
        this.roomId = roomId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.guestCount = guestCount;
        this.totalPrice = totalPrice;
        this.depositAmount = depositAmount;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
        this.state = state;
        this.confirmationCode = confirmationCode;
        this.specialRequests = specialRequests;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    // Getters
    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getRoomId() { return roomId; }
    public LocalDate getCheckInDate() { return checkInDate; }
    public LocalDate getCheckOutDate() { return checkOutDate; }
    public LocalDateTime getCheckInTime() { return checkInTime; }
    public LocalDateTime getCheckOutTime() { return checkOutTime; }
    public int getGuestCount() { return guestCount; }
    public double getTotalPrice() { return totalPrice; }
    public double getDepositAmount() { return depositAmount; }
    public boolean isPaid() { return isPaid; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getState() { return state; }
    public String getConfirmationCode() { return confirmationCode; }
    public String getSpecialRequests() { return specialRequests; }
    public String getNotes() { return notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters for mutable fields
    public void setCheckInTime(LocalDateTime checkInTime) { this.checkInTime = checkInTime; }
    public void setCheckOutTime(LocalDateTime checkOutTime) { this.checkOutTime = checkOutTime; }
    public void setDepositAmount(double depositAmount) { this.depositAmount = depositAmount; }
    public void setPaid(boolean paid) { isPaid = paid; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setState(String state) { this.state = state; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
    public void setNotes(String notes) { this.notes = notes; }

    // Helper methods
    public long getNights() {
        return java.time.temporal.ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }

    public double getRemainingAmount() {
        return totalPrice - depositAmount;
    }

    public boolean isPending() {
        return ReservationState.PENDING.toString().equals(state);
    }

    public boolean isConfirmed() {
        return ReservationState.CONFIRMED.toString().equals(state);
    }

    public boolean isCheckedIn() {
        return ReservationState.CHECKED_IN.toString().equals(state);
    }

    public boolean isCheckedOut() {
        return ReservationState.CHECKED_OUT.toString().equals(state);
    }

    public boolean isCancelled() {
        return ReservationState.CANCELLED.toString().equals(state);
    }

    public String getCustomerName() {
        // Bu bilgi join query ile gelmeli, şimdilik placeholder
        Connection connection;
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        connection = dbConnection.getConnection();
        String query = "SELECT name FROM Customers WHERE id = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return "N: "+rs.getString("name")+ "\nId: "+customerId;
        }catch (Exception e) {
            System.out.println("Hata"+ e.getMessage());
        }

        return "Müşteri #" + customerId;
    }

    public String getRoomNumber() {
        // Bu bilgi join query ile gelmeli, şimdilik placeholder
        return "Oda #" + roomId;
    }

    public String getPaymentStatus() {
        return isPaid ? "Ödendi" : "Ödenmedi";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Rezervasyon %s - %s to %s (%d gece)",
                confirmationCode, checkInDate, checkOutDate, getNights());
    }
}
