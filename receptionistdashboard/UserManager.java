package com.mycompany.receptionistdashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class UserManager {

    public void addUser(User user) {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getRole());
                if (ps.executeUpdate() > 0) System.out.println("User added successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE user_id = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                if (ps.executeUpdate() > 0) System.out.println("User deleted successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public User getUser(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return buildUser(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Verifies username + password only (no role check).
     * Used by LoginFrame to authenticate before showing the role picker.
     */
    public User getUserByCredentials(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return buildUser(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /**
     * Verifies username + password + role.
     * Kept for any place that needs strict role-aware authentication.
     */
    public User getUserByCredentials(String username, String password, String role) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND UPPER(role) = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, password);
                ps.setString(3, role.toUpperCase());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return buildUser(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public void updateRole(int userId, String role) {
        String sql = "UPDATE users SET role = ? WHERE user_id = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, role);
                ps.setInt(2, userId);
                if (ps.executeUpdate() > 0) System.out.println("Role updated successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return users;
            try (Statement st = connection.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) users.add(buildUser(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return users;
    }

    private User buildUser(ResultSet rs) throws Exception {
        return new User(
                rs.getInt("user_id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"));
    }
}