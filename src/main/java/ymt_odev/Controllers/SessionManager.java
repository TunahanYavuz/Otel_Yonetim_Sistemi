package ymt_odev.Controllers;

import ymt_odev.Users.Admin;
import ymt_odev.Users.Customer;
import ymt_odev.Users.Staff;
import ymt_odev.Users.User;

public class SessionManager {
    private static User user;

    public static void setUser(User user) {
        SessionManager.user = user;
    }

    public static User getUser() {
        return user;
    }

    public static Customer getCustomer() {
        return user instanceof Customer ? (Customer) user : null;
    }

    public static Staff getStaff() {
        return user instanceof Staff ? (Staff) user : null;
    }

    public static Admin getAdmin() {
        return user instanceof Admin ? (Admin) user : null;
    }

    public static void clearUser() {
        user = null;
    }

    public static boolean isUserLoggedIn() {
        return user != null;
    }

    public static String getCurrentRole() {
        return isUserLoggedIn() ? user.getRole() : "";
    }
}
