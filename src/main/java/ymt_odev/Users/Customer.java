package ymt_odev.Users;

import ymt_odev.Access;

public class Customer extends User {

    private final String loyaltyLevel;
    private final int totalBookings;

    public Customer(int id,
                    String username,
                    String password,
                    String email,
                    String phone,
                    String nationalId,
                    String firstName,
                    String lastName,
                    String memberSince,
                    String loyaltyLevel,
                    int totalBookings,
                    boolean isActive) {
        super(id, username, password, email, phone, nationalId, Access.CUSTOMER.toString(), firstName, lastName, memberSince, isActive);
        this.loyaltyLevel = loyaltyLevel;
        this.totalBookings = totalBookings;
    }

    public String getLoyaltyLevel() {
        return loyaltyLevel;
    }

    public int getTotalBookings() {
        return totalBookings;
    }
}
