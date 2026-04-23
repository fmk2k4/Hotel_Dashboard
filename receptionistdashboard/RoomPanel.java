package com.mycompany.receptionistdashboard;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;

public class RoomPanel extends JPanel {
    private final RoomManager roomManager;
    private DefaultTableModel tableModel;

    public RoomPanel() {
        this(new RoomManager());
    }

    public RoomPanel(RoomManager roomManager) {
        this.roomManager = roomManager;
        setupUI();
        loadRoomsIntoTable();
    }

    private void setupUI() {
        setBackground(new Color(245, 248, 252));
        setLayout(new BorderLayout());

        add(createHeader("Room Management"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createBody() {
        JPanel body = new JPanel();
        body.setBackground(new Color(245, 248, 252));
        body.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        body.setLayout(new BorderLayout(10, 10));

        body.add(createSearchBar(), BorderLayout.NORTH);
        body.add(createTablePanel(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createHeader(String title) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(30, 90, 160));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        JLabel label = new JLabel(title);
        label.setFont(new Font("Segoe UI", Font.BOLD, 15));
        label.setForeground(Color.WHITE);

        panel.add(label);
        return panel;
    }

    private JPanel createSearchBar() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panel.setOpaque(false);

        JLabel searchLabel = new JLabel("Search:");
        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton searchButton = createBlueButton("Search");
        JButton refreshButton = createGreenButton("Refresh All");

        searchButton.addActionListener(event -> searchRooms(searchField.getText()));
        refreshButton.addActionListener(event -> loadRoomsIntoTable());

        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);
        panel.add(Box.createHorizontalStrut(10));
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 245), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        panel.add(createSectionTitle("All Rooms"), BorderLayout.NORTH);
        panel.add(new JScrollPane(createRoomTable()), BorderLayout.CENTER);

        return panel;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(30, 90, 160));
        return label;
    }

    private JTable createRoomTable() {
        tableModel = new DefaultTableModel(new String[] { "Room ID", "Room No.", "Type", "Price/Night", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(tableModel);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(24);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        styleTableHeader(table);
        styleTableRows(table);

        return table;
    }

    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 11));
        header.setBackground(new Color(30, 90, 160));
        header.setForeground(Color.WHITE);
    }

    private void styleTableRows(JTable table) {
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable currentTable, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(currentTable, value, isSelected, hasFocus, row, column);

                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 245, 250));
                }

                if (column == 4 && value != null) {
                    setForeground(getStatusColor(value.toString()));
                } else {
                    setForeground(Color.DARK_GRAY);
                }

                return this;
            }
        });
    }

    private Color getStatusColor(String status) {
        switch (status) {
            case "AVAILABLE":
                return new Color(39, 174, 96);
            case "OCCUPIED":
                return new Color(192, 57, 43);
            case "MAINTENANCE":
                return new Color(211, 84, 0);
            case "RESERVED":
                return new Color(41, 128, 185);
            default:
                return Color.DARK_GRAY;
        }
    }

    private JButton createBlueButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton createGreenButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 11));
        button.setBackground(new Color(39, 174, 96));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadRoomsIntoTable() {
        tableModel.setRowCount(0);

        ArrayList<Room> rooms = roomManager.getAllRooms();
        for (Room room : rooms) {
            addRoomRow(room);
        }
    }

    private void addRoomRow(Room room) {
        tableModel.addRow(new Object[] {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getRoomType(),
                String.format("%.2f", room.getPricePerNight()),
                room.getStatus()
        });
    }

    private void searchRooms(String keyword) {
        tableModel.setRowCount(0);
        String normalizedKeyword = keyword.toLowerCase().trim();

        if (normalizedKeyword.isEmpty()) {
            loadRoomsIntoTable();
            return;
        }

        ArrayList<Room> rooms = roomManager.getAllRooms();
        for (Room room : rooms) {
            if (matchesSearchCriteria(room, normalizedKeyword)) {
                addRoomRow(room);
            }
        }
    }

    private boolean matchesSearchCriteria(Room room, String keyword) {
        return room.getRoomNumber().toLowerCase().contains(keyword)
                || room.getRoomType().toLowerCase().contains(keyword);
    }
}