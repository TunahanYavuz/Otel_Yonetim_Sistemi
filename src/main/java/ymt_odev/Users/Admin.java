package ymt_odev.Users;

import ymt_odev.Access;

public class Admin extends User {

    private final String department;
    private final String accessLevel;

    public Admin(int id,
                 String username,
                 String password,
                 String email,
                 String phone,
                 String nationalId,
                 String firstName,
                 String lastName,
                 String memberSince,
                 String department,
                 String accessLevel,
                 boolean isActive) {
        super(id, username, password, email, phone, nationalId, Access.ADMIN.toString(), firstName, lastName, memberSince, isActive);
        this.department = department;
        this.accessLevel = accessLevel;
    }

    public String getDepartment() {
        return department;
    }

    public String getAccessLevel() {
        return accessLevel;
    }
}
