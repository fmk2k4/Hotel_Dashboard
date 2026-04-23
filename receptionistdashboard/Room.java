package com.mycompany.receptionistdashboard;

public class Room {
    private final int roomId;
    private final String roomNumber;
    private final String roomType;
    private final double pricePerNight;
    private final String status;

    public Room(int roomId, String roomNumber, String roomType, double pricePerNight, String status) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
        this.status = status;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Room{roomId=" + roomId + ", roomNumber='" + roomNumber + '\'' + ", roomType='" + roomType + '\''
                + ", pricePerNight=" + pricePerNight + ", status='" + status + '\'' + '}';
    }
}