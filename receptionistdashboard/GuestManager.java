package com.mycompany.receptionistdashboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class GuestManager {

    public void addGuest(Guest guest) {
        String sql = "INSERT INTO guests (name, phone, email) VALUES (?, ?, ?)";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, guest.getName());
                ps.setString(2, guest.getPhone());
                ps.setString(3, guest.getEmail());
                if (ps.executeUpdate() > 0) System.out.println("Guest added successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateGuest(int guestId, String name, String phone, String email) {
        String sql = "UPDATE guests SET name = ?, phone = ?, email = ? WHERE guest_id = ?";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setInt(4, guestId);
                if (ps.executeUpdate() > 0) System.out.println("Guest updated successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteGuest(int guestId) {
        String sql = "DELETE FROM guests WHERE guest_id = ?";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, guestId);
                if (ps.executeUpdate() > 0) System.out.println("Guest deleted successfully.");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Guest getGuest(int guestId) {
        String sql = "SELECT * FROM guests WHERE guest_id = ?";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return null;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, guestId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) return buildGuest(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public ArrayList<Guest> getAllGuests() {
        ArrayList<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests ORDER BY guest_id";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return guests;
            try (PreparedStatement ps = connection.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) guests.add(buildGuest(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return guests;
    }

    public ArrayList<Guest> searchGuests(String keyword) {
        ArrayList<Guest> guests = new ArrayList<>();
        String sql = "SELECT * FROM guests WHERE LOWER(name) LIKE ? OR phone LIKE ? OR LOWER(email) LIKE ? ORDER BY name";
        String pattern = "%" + keyword.toLowerCase() + "%";
        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) return guests;
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, pattern);
                ps.setString(2, pattern);
                ps.setString(3, pattern);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) guests.add(buildGuest(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return guests;
    }

    private Guest buildGuest(ResultSet rs) throws Exception {
        return new Guest(
                rs.getInt("guest_id"),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getString("email"));
    }
}