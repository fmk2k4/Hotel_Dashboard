package com.mycompany.receptionistdashboard;



import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservationPanel extends JPanel {
    private final ReservationManager reservationManager;
    private final RoomManager roomManager;
    private final GuestManager guestManager;

    public ReservationPanel() {
        this(new ReservationManager(), new RoomManager(), new GuestManager());
    }

    public ReservationPanel(ReservationManager reservationManager, RoomManager roomManager, GuestManager guestManager) {
        this.reservationManager = reservationManager;
        this.roomManager = roomManager;
        this.guestManager = guestManager;
        setupUI();
    }

    private void setupUI() {
        setBackground(new Color(245, 248, 252));
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(30, 90, 160));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel title = new JLabel("Reservation Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 15));
        title.setForeground(Color.WHITE);

        JLabel hint = new JLabel("Date format: YYYY-MM-DD");
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        hint.setForeground(new Color(180, 210, 240));

        panel.add(title, BorderLayout.WEST);
        panel.add(hint, BorderLayout.EAST);

        return panel;
    }

    private JPanel createBody() {
        JPanel body = new JPanel();
        body.setBackground(new Color(245, 248, 252));
        body.setLayout(new javax.swing.BoxLayout(body, javax.swing.BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        body.add(createReservationCard());
        body.add(Box.createVerticalStrut(10));
        body.add(createActionCardsRow());
        body.add(Box.createVerticalGlue());

        JScrollPane scrollPane = new JScrollPane(body);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(scrollPane, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createReservationCard() {
        JPanel card = createCard("Create New Reservation", new Color(39, 174, 96));

        JTextField guestIdField = new JTextField(10);
        JTextField roomIdField = new JTextField(10);
        JTextField checkInField = new JTextField(10);
        JTextField checkOutField = new JTextField(10);
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        fieldsPanel.setOpaque(false);
        fieldsPanel.add(new JLabel("Guest ID:"));
        fieldsPanel.add(guestIdField);
        fieldsPanel.add(new JLabel("Room ID:"));
        fieldsPanel.add(roomIdField);
        fieldsPanel.add(new JLabel("Check-In (YYYY-MM-DD):"));
        fieldsPanel.add(checkInField);
        fieldsPanel.add(new JLabel("Check-Out (YYYY-MM-DD):"));
        fieldsPanel.add(checkOutField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        JButton createButton = createActionButton("Create Reservation", new Color(39, 174, 96));
        createButton.addActionListener(event -> handleCreateReservation(
                guestIdField, roomIdField, checkInField, checkOutField, messageLabel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(createButton);
        buttonPanel.add(messageLabel);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createActionCardsRow() {
        JPanel row = new JPanel(new GridLayout(1, 2, 10, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        row.add(createCheckInCard());
        row.add(createCheckOutCard());

        return row;
    }

    private JPanel createCheckInCard() {
        JPanel card = createCard("Check-In", new Color(41, 128, 185));

        JTextField reservationIdField = new JTextField(10);
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JPanel fieldsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        fieldsPanel.setOpaque(false);
        fieldsPanel.add(new JLabel("Reservation ID:"));
        fieldsPanel.add(reservationIdField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        JButton checkInButton = createActionButton("Check-In", new Color(41, 128, 185));
        checkInButton.addActionListener(event -> handleCheckIn(reservationIdField, messageLabel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(checkInButton);
        buttonPanel.add(messageLabel);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCheckOutCard() {
        JPanel card = createCard("Check-Out", new Color(192, 57, 43));

        JTextField reservationIdField = new JTextField(10);
        JLabel messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JPanel fieldsPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        fieldsPanel.setOpaque(false);
        fieldsPanel.add(new JLabel("Reservation ID:"));
        fieldsPanel.add(reservationIdField);

        card.add(fieldsPanel, BorderLayout.CENTER);

        JButton checkOutButton = createActionButton("Check-Out", new Color(192, 57, 43));
        checkOutButton.addActionListener(event -> handleCheckOut(reservationIdField, messageLabel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(messageLabel);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JPanel createCard(String title, Color accentColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(3, 0, 0, 0, accentColor),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 220, 245), 1),
                        BorderFactory.createEmptyBorder(12, 12, 12, 12))));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel cardTitle = new JLabel(title);
        cardTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        cardTitle.setForeground(accentColor);
        card.add(cardTitle, BorderLayout.NORTH);

        return card;
    }

    private JButton createActionButton(String text, Color background) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleCreateReservation(JTextField guestIdField, JTextField roomIdField, JTextField checkInField,
            JTextField checkOutField, JLabel messageLabel) {
        try {
            int guestId = Integer.parseInt(guestIdField.getText().trim());
            int roomId = Integer.parseInt(roomIdField.getText().trim());
            LocalDate checkIn = LocalDate.parse(checkInField.getText().trim());
            LocalDate checkOut = LocalDate.parse(checkOutField.getText().trim());

            if (!checkOut.isAfter(checkIn)) {
                showError(messageLabel, "Check-out must be after check-in!");
                return;
            }

            if (guestManager.getGuest(guestId) == null) {
                showError(messageLabel, "Guest not found!");
                return;
            }

            if (roomManager.getRoom(roomId) == null) {
                showError(messageLabel, "Room not found!");
                return;
            }

            if (!reservationManager.isRoomAvailable(roomId, checkIn, checkOut)) {
                showError(messageLabel, "Room not available for these dates!");
                return;
            }

            Reservation reservation = new Reservation(0, roomId, guestId, checkIn, checkOut, "BOOKED");
            reservationManager.createReservation(reservation);

            showSuccess(messageLabel, "Reservation created successfully!");
            clearAllFields(guestIdField, roomIdField, checkInField, checkOutField);
        } catch (NumberFormatException exception) {
            showError(messageLabel, "Invalid ID format!");
        } catch (DateTimeParseException exception) {
            showError(messageLabel, "Invalid date format (use YYYY-MM-DD)!");
        }
    }

    private void handleCheckIn(JTextField reservationIdField, JLabel messageLabel) {
        try {
            int reservationId = Integer.parseInt(reservationIdField.getText().trim());
            int confirmed = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm check-in?",
                    "Check-In",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed != JOptionPane.YES_OPTION) {
                return;
            }

            reservationManager.checkIn(reservationId);
            showSuccess(messageLabel, "Checked in!");
            reservationIdField.setText("");
        } catch (NumberFormatException exception) {
            showError(messageLabel, "Invalid ID!");
        }
    }

    private void handleCheckOut(JTextField reservationIdField, JLabel messageLabel) {
        try {
            int reservationId = Integer.parseInt(reservationIdField.getText().trim());
            int confirmed = JOptionPane.showConfirmDialog(
                    this,
                    "Confirm check-out?",
                    "Check-Out",
                    JOptionPane.YES_NO_OPTION);

            if (confirmed != JOptionPane.YES_OPTION) {
                return;
            }

            reservationManager.checkOut(reservationId);
            showSuccess(messageLabel, "Checked out!");
            reservationIdField.setText("");
        } catch (NumberFormatException exception) {
            showError(messageLabel, "Invalid ID!");
        }
    }

    private void showError(JLabel label, String message) {
        label.setForeground(new Color(192, 57, 43));
        label.setText(message);
    }

    private void showSuccess(JLabel label, String message) {
        label.setForeground(new Color(39, 174, 96));
        label.setText(message);
    }

    private void clearAllFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}