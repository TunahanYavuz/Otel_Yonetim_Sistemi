package ymt_odev.Users;

import java.util.Objects;

public abstract class User {

    private final int id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String nationalId;
    private final String role;
    private String firstName;
    private String lastName;
    private final String memberSince;
    private boolean isActive;

    protected User(int id,
                   String username,
                   String password,
                   String email,
                   String phone,
                   String nationalId,
                   String role,
                   String firstName,
                   String lastName,
                   String memberSince,
                   boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.nationalId = nationalId;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
        this.memberSince = memberSince;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getNationalId() { return nationalId; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setNationalId(String nationalId) { this.nationalId = nationalId; }

    public String getRole() { return role; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getMemberSince() { return memberSince; }

    public String getDisplayName() {
        if (firstName == null && lastName == null) {
            return username;
        }
        return (Objects.toString(firstName, "") + " " + Objects.toString(lastName, "")).trim();
    }

    public void updateProfile(String newUsername,
                              String newFirstName,
                              String newLastName,
                              String newEmail,
                              String newPhone,
                              String newNationalId) {
        this.username = newUsername;
        this.firstName = newFirstName;
        this.lastName = newLastName;
        this.email = newEmail;
        this.phone = newPhone;
        this.nationalId = newNationalId;
    }
}
