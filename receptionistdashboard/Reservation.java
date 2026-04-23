package com.mycompany.receptionistdashboard;

import java.time.LocalDate;

public class Reservation {
    private final int reservationId;
    private final int roomId;
    private final int guestId;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final String status;

    public Reservation(int reservationId, int roomId, int guestId, LocalDate checkInDate, LocalDate checkOutDate,
            String status) {
        this.reservationId = reservationId;
        this.roomId = roomId;
        this.guestId = guestId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getGuestId() {
        return guestId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Reservation{reservationId=" + reservationId + ", roomId=" + roomId + ", guestId=" + guestId
                + ", checkInDate=" + checkInDate + ", checkOutDate=" + checkOutDate + ", status='" + status
                + '\'' + '}';
    }
}