package ymt_odev.Users;

import ymt_odev.Access;

public class Staff extends User {

    private final String department;
    private final String shift;
    private final String accessLevel;

    public Staff(int id,
                 String username,
                 String password,
                 String email,
                 String phone,
                 String nationalId,
                 String firstName,
                 String lastName,
                 String memberSince,
                 String department,
                 String shift,
                 String accessLevel,
                 boolean isActive) {
        super(id, username, password, email, phone, nationalId, Access.STAFF.toString(), firstName, lastName, memberSince, isActive);
        this.department = department;
        this.shift = shift;
        this.accessLevel = accessLevel;
    }

    public String getDepartment() {
        return department;
    }

    public String getShift() {
        return shift;
    }

    public String getAccessLevel() {
        return accessLevel;
    }
}
