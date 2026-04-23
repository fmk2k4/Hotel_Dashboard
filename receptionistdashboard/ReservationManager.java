package com.mycompany.receptionistdashboard;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;

public class ReservationManager {
    private final RoomManager roomManager;

    public ReservationManager() {
        this(new RoomManager());
    }

    public ReservationManager(RoomManager roomManager) {
        this.roomManager = roomManager;
    }

    public void createReservation(Reservation reservation) {
        boolean available = isRoomAvailable(
                reservation.getRoomId(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate());

        if (!available) {
            System.out.println("Reservation failed. Room is already booked for these dates.");
            return;
        }

        String sql = "INSERT INTO reservations (room_id, guest_id, check_in_date, check_out_date, status) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, reservation.getRoomId());
                preparedStatement.setInt(2, reservation.getGuestId());
                preparedStatement.setDate(3, Date.valueOf(reservation.getCheckInDate()));
                preparedStatement.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
                preparedStatement.setString(5, reservation.getStatus());

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Reservation created successfully.");
                    roomManager.updateRoomStatus(reservation.getRoomId(), "RESERVED");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public boolean isRoomAvailable(int roomId, LocalDate newCheckIn, LocalDate newCheckOut) {
        String sql = "SELECT COUNT(*) FROM reservations "
                + "WHERE room_id = ? "
                + "AND status IN ('BOOKED', 'CHECKED_IN') "
                + "AND (? < check_out_date AND ? > check_in_date)";

        boolean available = true;

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return true;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, roomId);
                preparedStatement.setDate(2, Date.valueOf(newCheckOut));
                preparedStatement.setDate(3, Date.valueOf(newCheckIn));

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next() && resultSet.getInt(1) > 0) {
                        available = false;
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return available;
    }

    public void checkIn(int reservationId) {
        String getSql = "SELECT room_id FROM reservations WHERE reservation_id = ?";
        String updateReservationSql = "UPDATE reservations SET status = 'CHECKED_IN' WHERE reservation_id = ?";
        int roomId = 0;

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement getStatement = connection.prepareStatement(getSql);
                    PreparedStatement updateStatement = connection.prepareStatement(updateReservationSql)) {
                getStatement.setInt(1, reservationId);

                try (ResultSet resultSet = getStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roomId = resultSet.getInt("room_id");
                    } else {
                        System.out.println("Reservation not found.");
                        return;
                    }
                }

                updateStatement.setInt(1, reservationId);
                int rows = updateStatement.executeUpdate();

                if (rows > 0) {
                    System.out.println("Guest checked in successfully.");
                    roomManager.updateRoomStatus(roomId, "OCCUPIED");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void checkOut(int reservationId) {
        String getSql = "SELECT room_id FROM reservations WHERE reservation_id = ?";
        String updateReservationSql = "UPDATE reservations SET status = 'CHECKED_OUT' WHERE reservation_id = ?";
        int roomId = 0;

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement getStatement = connection.prepareStatement(getSql);
                    PreparedStatement updateStatement = connection.prepareStatement(updateReservationSql)) {
                getStatement.setInt(1, reservationId);

                try (ResultSet resultSet = getStatement.executeQuery()) {
                    if (resultSet.next()) {
                        roomId = resultSet.getInt("room_id");
                    } else {
                        System.out.println("Reservation not found.");
                        return;
                    }
                }

                updateStatement.setInt(1, reservationId);
                int rows = updateStatement.executeUpdate();

                if (rows > 0) {
                    System.out.println("Guest checked out successfully.");
                    roomManager.updateRoomStatus(roomId, "AVAILABLE");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<Reservation> getAllReservations() {
        String sql = "SELECT * FROM reservations ORDER BY reservation_id";
        ArrayList<Reservation> reservations = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return reservations;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    reservations.add(buildReservation(resultSet));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return reservations;
    }

    public double calculateTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(DATEDIFF(r.check_out_date, r.check_in_date) * rm.price_per_night), 0) AS total_revenue "
                + "FROM reservations r "
                + "JOIN rooms rm ON r.room_id = rm.room_id";
        double totalRevenue = 0.0;

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return totalRevenue;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalRevenue = resultSet.getDouble("total_revenue");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return totalRevenue;
    }

    private Reservation buildReservation(ResultSet resultSet) throws Exception {
        return new Reservation(
                resultSet.getInt("reservation_id"),
                resultSet.getInt("room_id"),
                resultSet.getInt("guest_id"),
                resultSet.getDate("check_in_date").toLocalDate(),
                resultSet.getDate("check_out_date").toLocalDate(),
                resultSet.getString("status"));
    }
}