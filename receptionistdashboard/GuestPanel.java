package com.mycompany.receptionistdashboard;




import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import java.awt.Font;
import java.util.ArrayList;

public class GuestPanel extends JPanel {
    private final GuestManager guestManager;
    private DefaultTableModel tableModel;

    public GuestPanel() {
        this(new GuestManager());
    }

    public GuestPanel(GuestManager guestManager) {
        this.guestManager = guestManager;
        setupUI();
        loadGuestsIntoTable();
    }

    private void setupUI() {
        setBackground(new Color(245, 248, 252));
        setLayout(new BorderLayout());

        add(createHeader("Guest Management"), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
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

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(15, 0));
        body.setBackground(new Color(245, 248, 252));
        body.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        body.add(createFormPanel(), BorderLayout.WEST);
        body.add(createTablePanel(), BorderLayout.CENTER);

        return body;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 245), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        panel.setPreferredSize(new Dimension(280, 0));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(createSectionTitle("Add New Guest"));
        panel.add(Box.createVerticalStrut(15));

        JTextField nameField = addInputField(panel, "Name:");
        JTextField phoneField = addInputField(panel, "Phone:");
        JTextField emailField = addInputField(panel, "Email:");

        panel.add(Box.createVerticalStrut(15));

        JLabel messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        JButton addButton = createGreenButton("Add Guest");
        addButton.addActionListener(event -> handleAddGuest(nameField, phoneField, emailField, messageLabel));

        panel.add(addButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(messageLabel);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 245), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        panel.add(createSectionTitle("All Guests"), BorderLayout.NORTH);
        panel.add(new JScrollPane(createGuestTable()), BorderLayout.CENTER);

        JButton refreshButton = createBlueButton("Refresh");
        refreshButton.addActionListener(event -> loadGuestsIntoTable());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JTextField addInputField(JPanel parent, String labelText) {
        parent.add(new JLabel(labelText));
        parent.add(Box.createVerticalStrut(3));

        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        parent.add(field);
        parent.add(Box.createVerticalStrut(10));

        return field;
    }

    private JLabel createSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(30, 90, 160));
        return label;
    }

    private JButton createGreenButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(39, 174, 96));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return button;
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

    private JTable createGuestTable() {
        tableModel = new DefaultTableModel(new String[] { "Guest ID", "Name", "Phone", "Email" }, 0) {
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

                return this;
            }
        });
    }

    private void handleAddGuest(JTextField nameField, JTextField phoneField, JTextField emailField,
            JLabel messageLabel) {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String email = emailField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            showErrorMessage(messageLabel, "Name & phone required!");
            return;
        }

        Guest guest = new Guest(0, name, phone, email.isEmpty() ? null : email);
        guestManager.addGuest(guest);

        showSuccessMessage(messageLabel, "Guest added!");
        clearAllFields(nameField, phoneField, emailField);
        loadGuestsIntoTable();
    }

    private void loadGuestsIntoTable() {
        tableModel.setRowCount(0);

        ArrayList<Guest> guests = guestManager.getAllGuests();
        for (Guest guest : guests) {
            tableModel.addRow(new Object[] {
                    guest.getGuestId(),
                    guest.getName(),
                    guest.getPhone(),
                    guest.getEmail()
            });
        }
    }

    private void showErrorMessage(JLabel label, String text) {
        label.setForeground(new Color(192, 57, 43));
        label.setText(text);
    }

    private void showSuccessMessage(JLabel label, String text) {
        label.setForeground(new Color(39, 174, 96));
        label.setText(text);
    }

    private void clearAllFields(JTextField... fields) {
        for (JTextField field : fields) {
            field.setText("");
        }
    }
}