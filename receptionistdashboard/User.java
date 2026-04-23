package com.mycompany.receptionistdashboard;

public class User {
    private final int userId;
    private final String username;
    private final String password;
    private final String role;

    public User(int userId, String username, String password, String role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username + '\'' + ", password='" + password + '\''
                + ", role='" + role + '\'' + '}';
    }
}