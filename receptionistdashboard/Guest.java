package com.mycompany.receptionistdashboard;

public class Guest {
    private final int guestId;
    private final String name;
    private final String phone;
    private final String email;

    public Guest(int guestId, String name, String phone, String email) {
        this.guestId = guestId;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public int getGuestId() {
        return guestId;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Guest{guestId=" + guestId + ", name='" + name + '\'' + ", phone='" + phone + '\'' + ", email='"
                + email + '\'' + '}';
    }
}