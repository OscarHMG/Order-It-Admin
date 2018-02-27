package com.oscarhmg.orderit_server.Model;

/**
 * Created by OscarHMG on 26/11/2017.
 */

public class User {
    private String name,phone, password, isStaffMember;

    public User() {
    }

    public User(String name, String phone, String password, String isStaffMember) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.isStaffMember = isStaffMember;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsStaffMember() {
        return isStaffMember;
    }

    public void setIsStaffMember(String isStaffMember) {
        this.isStaffMember = isStaffMember;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", isStaffMember='" + isStaffMember + '\'' +
                '}';
    }
}
