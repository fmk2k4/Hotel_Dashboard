package com.mycompany.receptionistdashboard;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RoomManager {

    public void addRoom(Room room) {
        String sql = "INSERT INTO rooms (room_number, room_type, price_per_night, status) VALUES (?, ?, ?, ?)";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, room.getRoomNumber());
                preparedStatement.setString(2, room.getRoomType());
                preparedStatement.setDouble(3, room.getPricePerNight());
                preparedStatement.setString(4, room.getStatus());

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Room added successfully.");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public Room getRoom(int roomId) {
        String sql = "SELECT * FROM rooms WHERE room_id = ?";
        Room room = null;

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return null;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, roomId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        room = buildRoom(resultSet);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return room;
    }

    public void updateRoomStatus(int roomId, String newStatus) {
        String sql = "UPDATE rooms SET status = ? WHERE room_id = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, newStatus);
                preparedStatement.setInt(2, roomId);

                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Room status updated to " + newStatus);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public ArrayList<Room> getAllRooms() {
        String sql = "SELECT * FROM rooms";
        ArrayList<Room> rooms = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return rooms;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    rooms.add(buildRoom(resultSet));
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return rooms;
    }

    public void removeRoom(int roomId) {
        String sql = "DELETE FROM rooms WHERE room_id = ?";

        try (Connection connection = DataBaseConnection.getConnection()) {
            if (connection == null) {
                return;
            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, roomId);
                int rows = preparedStatement.executeUpdate();
                if (rows > 0) {
                    System.out.println("Room removed successfully.");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private Room buildRoom(ResultSet resultSet) throws Exception {
        return new Room(
                resultSet.getInt("room_id"),
                resultSet.getString("room_number"),
                resultSet.getString("room_type"),
                resultSet.getDouble("price_per_night"),
                resultSet.getString("status"));
    }
}