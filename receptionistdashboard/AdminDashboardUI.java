package com.mycompany.receptionistdashboard;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * AdminDashboardUI — Professional hotel admin panel.
 * Sidebar navigation: Dashboard · Reports · Room Management · User Management
 */
public class AdminDashboardUI extends JFrame {

    // ── Theme ─────────────────────────────────────────────────────────────────
    static final Color C_SIDEBAR    = new Color(10, 24, 40);
    static final Color C_SIDEBAR_HV = new Color(22, 42, 68);
    static final Color C_SIDEBAR_SEL= new Color(18, 38, 60);
    static final Color C_GOLD       = new Color(201, 168, 76);
    static final Color C_GOLD_LT    = new Color(232, 197, 106);
    static final Color C_BG         = new Color(244, 247, 252);
    static final Color C_WHITE      = Color.WHITE;
    static final Color C_BORDER     = new Color(220, 228, 240);
    static final Color C_TEXT_DARK  = new Color(15, 30, 50);
    static final Color C_TEXT_MID   = new Color(100, 116, 139);
    static final Color C_GREEN      = new Color(16, 185, 129);
    static final Color C_RED        = new Color(220, 38, 38);
    static final Color C_BLUE       = new Color(37, 99, 235);
    static final Color C_ORANGE     = new Color(234, 120, 18);
    static final Color C_PURPLE     = new Color(124, 58, 237);

    // ── Data managers ─────────────────────────────────────────────────────────
    private final RoomManager        roomManager;
    private final UserManager        userManager;
    private final ReservationManager reservationManager;
    private final List<String>       activityLog = new ArrayList<>();

    // ── Navigation ────────────────────────────────────────────────────────────
    private final CardLayout contentLayout = new CardLayout();
    private JPanel           contentPanel;
    private JButton          activeNavBtn  = null;

    // ── Dashboard widgets ─────────────────────────────────────────────────────
    private JLabel            kpiRooms, kpiOccupied, kpiAvailable, kpiRevenue, kpiGuests;
    private DefaultTableModel recentResModel;

    // ── Reports widgets ───────────────────────────────────────────────────────
    private JLabel            revLabel;
    private DefaultTableModel resHistModel, occupancyModel;

    // ── Room management fields ────────────────────────────────────────────────
    private JTextField rmId, rmNumber, rmType, rmPrice, rmStatus;
    private JLabel     rmMsg;
    private DefaultTableModel roomTableModel;

    // ── User management fields ────────────────────────────────────────────────
    private JTextField umId, umUsername, umPassword, umRole;
    private JLabel     umMsg;
    private DefaultTableModel userTableModel;

    // ── Constructor ───────────────────────────────────────────────────────────
    public AdminDashboardUI() { this(new RoomManager(), new UserManager(), null); }

    public AdminDashboardUI(RoomManager rm, UserManager um, ReservationManager rsm) {
        this.roomManager        = rm  != null ? rm  : new RoomManager();
        this.userManager        = um  != null ? um  : new UserManager();
        this.reservationManager = rsm != null ? rsm : new ReservationManager(this.roomManager);
        buildFrame();
    }

    // ── Frame Setup ───────────────────────────────────────────────────────────
    private void buildFrame() {
        setTitle("Grand Azure — Admin Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 700);
        setMinimumSize(new Dimension(920, 580));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContentArea(), BorderLayout.CENTER);
        setContentPane(root);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SIDEBAR
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Subtle right border
                g2.setColor(new Color(201, 168, 76, 40));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
        };
        sb.setPreferredSize(new Dimension(228, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        sb.add(buildBrand());
        sb.add(buildGoldRule());
        sb.add(Box.createVerticalStrut(6));
        sb.add(sectionLabel("MAIN MENU"));

        String[][] items = {
            { "◈  Dashboard",       "DASHBOARD" },
            { "◈  Reports",         "REPORTS"   },
            { "◈  Room Management", "ROOMS"     },
            { "◈  User Management", "USERS"     },
        };
        List<JButton> navBtns = new ArrayList<>();
        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1], navBtns);
            navBtns.add(btn);
            sb.add(btn);
        }

        // Activate first button
        if (!navBtns.isEmpty()) setActiveNav(navBtns.get(0));

        sb.add(Box.createVerticalGlue());
        sb.add(buildGoldRule());
        sb.add(sidebarFooter());
        return sb;
    }

    private JPanel buildBrand() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(26, 22, 18, 22));
        p.setMaximumSize(new Dimension(228, 115));

        JLabel stars = new JLabel("★  ★  ★  ★  ★");
        stars.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        stars.setForeground(C_GOLD);

        JLabel hotelName = new JLabel("GRAND AZURE");
        hotelName.setFont(new Font("Georgia", Font.BOLD, 17));
        hotelName.setForeground(C_GOLD_LT);

        JLabel tagline = new JLabel("Hotel & Suites");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        tagline.setForeground(new Color(160, 185, 215));

        JLabel roleTag = new JLabel("  ADMIN PORTAL");
        roleTag.setFont(new Font("Segoe UI", Font.BOLD, 9));
        roleTag.setForeground(C_GOLD);
        roleTag.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(201, 168, 76, 120), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));

        p.add(stars);
        p.add(Box.createVerticalStrut(5));
        p.add(hotelName);
        p.add(tagline);
        p.add(Box.createVerticalStrut(8));
        p.add(roleTag);
        return p;
    }

    private Component buildGoldRule() {
        JPanel rule = new JPanel();
        rule.setBackground(new Color(201, 168, 76, 55));
        rule.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        rule.setPreferredSize(new Dimension(0, 1));
        return rule;
    }

    private JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 8));
        l.setForeground(new Color(90, 120, 155));
        l.setBorder(BorderFactory.createEmptyBorder(12, 22, 6, 22));
        l.setMaximumSize(new Dimension(228, 30));
        return l;
    }

    private JButton navButton(String text, String card, List<JButton> group) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(new Color(175, 200, 230));
        btn.setBackground(C_SIDEBAR);
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(228, 44));
        btn.setPreferredSize(new Dimension(228, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeNavBtn) {
                    btn.setBackground(C_SIDEBAR_HV);
                    btn.setForeground(Color.WHITE);
                }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeNavBtn) {
                    btn.setBackground(C_SIDEBAR);
                    btn.setForeground(new Color(175, 200, 230));
                }
            }
        });
        btn.addActionListener(e -> {
            setActiveNav(btn);
            contentLayout.show(contentPanel, card);
            if ("DASHBOARD".equals(card)) refreshDashboard();
            if ("REPORTS".equals(card))   refreshReports();
            if ("ROOMS".equals(card))     refreshRoomTable();
            if ("USERS".equals(card))     refreshUserTable();
        });
        return btn;
    }

    private void setActiveNav(JButton btn) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(C_SIDEBAR);
            activeNavBtn.setForeground(new Color(175, 200, 230));
        }
        activeNavBtn = btn;
        btn.setBackground(C_SIDEBAR_SEL);
        btn.setForeground(C_GOLD_LT);
    }

    private JPanel sidebarFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 18, 22));
        p.setMaximumSize(new Dimension(228, 70));

        JLabel who = new JLabel("Logged in as: Administrator");
        who.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        who.setForeground(new Color(120, 148, 180));

        JLabel ver = new JLabel("Hotel Management System v1.0");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        ver.setForeground(new Color(70, 98, 130));

        p.add(who);
        p.add(Box.createVerticalStrut(3));
        p.add(ver);
        return p;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // CONTENT AREA
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildContentArea() {
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(C_BG);
        contentPanel.add(buildDashboardPanel(), "DASHBOARD");
        contentPanel.add(buildReportsPanel(),   "REPORTS");
        contentPanel.add(buildRoomsPanel(),     "ROOMS");
        contentPanel.add(buildUsersPanel(),     "USERS");
        contentLayout.show(contentPanel, "DASHBOARD");
        SwingUtilities.invokeLater(this::refreshDashboard);
        return contentPanel;
    }

    // ── PAGE HEADER ──────────────────────────────────────────────────────────
    private JPanel pageHeader(String title, String subtitle) {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(C_WHITE);
        h.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, C_BORDER),
                BorderFactory.createEmptyBorder(18, 28, 18, 28)));

        JPanel accent = new JPanel();
        accent.setBackground(C_GOLD);
        accent.setPreferredSize(new Dimension(4, 0));

        JPanel textCol = new JPanel();
        textCol.setOpaque(false);
        textCol.setLayout(new BoxLayout(textCol, BoxLayout.Y_AXIS));
        textCol.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Georgia", Font.BOLD, 20));
        t.setForeground(C_TEXT_DARK);

        JLabel s = new JLabel(subtitle);
        s.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        s.setForeground(C_TEXT_MID);

        textCol.add(t);
        textCol.add(Box.createVerticalStrut(3));
        textCol.add(s);

        h.add(accent,  BorderLayout.WEST);
        h.add(textCol, BorderLayout.CENTER);
        return h;
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DASHBOARD PANEL
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildDashboardPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_BG);
        p.add(pageHeader("Dashboard Overview",
                "Real-time snapshot — " + LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"))),
                BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(C_BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        // KPI row
        JPanel kpiRow = new JPanel(new GridLayout(1, 5, 14, 0));
        kpiRow.setOpaque(false);
        kpiRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        kpiRooms     = kpiValue("—"); kpiOccupied = kpiValue("—");
        kpiAvailable = kpiValue("—"); kpiRevenue  = kpiValue("—");
        kpiGuests    = kpiValue("—");

        kpiRow.add(kpiCard("Total Rooms",         kpiRooms,     C_BLUE,   "🏨"));
        kpiRow.add(kpiCard("Occupied",            kpiOccupied,  C_RED,    "🔴"));
        kpiRow.add(kpiCard("Available",           kpiAvailable, C_GREEN,  "🟢"));
        kpiRow.add(kpiCard("Revenue (MYR)",       kpiRevenue,   C_GOLD,   "💰"));
        kpiRow.add(kpiCard("Total Guests",        kpiGuests,    C_PURPLE, "👥"));

        body.add(kpiRow);
        body.add(Box.createVerticalStrut(22));

        // Section title row
        JPanel hdrRow = new JPanel(new BorderLayout());
        hdrRow.setOpaque(false);
        hdrRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        hdrRow.add(sectionTitle("Recent Reservations"), BorderLayout.WEST);
        JButton refreshBtn = actionBtn("↻ Refresh", C_BLUE);
        refreshBtn.addActionListener(e -> refreshDashboard());
        hdrRow.add(refreshBtn, BorderLayout.EAST);
        body.add(hdrRow);
        body.add(Box.createVerticalStrut(8));

        recentResModel = new DefaultTableModel(
                new String[]{"ID", "Room", "Guest ID", "Check-In", "Check-Out", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JScrollPane sp = wrappedTable(styledTable(recentResModel, 5), 220);
        body.add(sp);
        body.add(Box.createVerticalStrut(18));

        // Activity log preview
        body.add(sectionTitle("Recent Activity"));
        body.add(Box.createVerticalStrut(8));
        JTextArea logArea = new JTextArea(4, 0);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        logArea.setBackground(new Color(248, 250, 255));
        logArea.setForeground(C_TEXT_MID);
        logArea.setEditable(false);
        logArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        logArea.setText("System started. All services nominal.\n");
        activityLog.add(logArea.getText().trim());
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(null);
        logScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        body.add(logScroll);

        JScrollPane outerScroll = new JScrollPane(body);
        outerScroll.setBorder(null);
        outerScroll.getVerticalScrollBar().setUnitIncrement(14);
        p.add(outerScroll, BorderLayout.CENTER);
        return p;
    }

    private JLabel kpiValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Georgia", Font.BOLD, 26));
        l.setForeground(C_TEXT_DARK);
        return l;
    }

    private JPanel kpiCard(String label, JLabel valueLabel, Color accent, String icon) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 3, 0, 0, accent),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER),
                        BorderFactory.createEmptyBorder(14, 14, 14, 14))));

        JLabel iconL = new JLabel(icon);
        iconL.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        iconL.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel lbl = new JLabel(label.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 9));
        lbl.setForeground(C_TEXT_MID);

        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(lbl);
        textPanel.add(Box.createVerticalStrut(2));
        textPanel.add(valueLabel);

        card.add(iconL,     BorderLayout.EAST);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private void refreshDashboard() {
        try {
            List<Room> rooms = roomManager.getAllRooms();
            long occ  = rooms.stream().filter(r -> "OCCUPIED".equals(r.getStatus())).count();
            long avl  = rooms.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();
            double rev = reservationManager.calculateTotalRevenue();
            int guests = new GuestManager().getAllGuests().size();

            kpiRooms.setText(String.valueOf(rooms.size()));
            kpiOccupied.setText(String.valueOf(occ));
            kpiAvailable.setText(String.valueOf(avl));
            kpiRevenue.setText(String.format("%.0f", rev));
            kpiGuests.setText(String.valueOf(guests));

            recentResModel.setRowCount(0);
            List<Reservation> all = reservationManager.getAllReservations();
            int from = Math.max(0, all.size() - 12);
            for (int i = from; i < all.size(); i++) {
                Reservation r = all.get(i);
                recentResModel.addRow(new Object[]{
                        r.getReservationId(), r.getRoomId(), r.getGuestId(),
                        r.getCheckInDate(), r.getCheckOutDate(), r.getStatus()
                });
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        log("Dashboard refreshed.");
    }

    // ══════════════════════════════════════════════════════════════════════════
    // REPORTS PANEL
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildReportsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_BG);
        p.add(pageHeader("Reports & Analytics",
                "Financial overview, reservation history, and room occupancy"), BorderLayout.NORTH);

        JPanel body = new JPanel();
        body.setBackground(C_BG);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        // Revenue banner
        body.add(revenueBanner());
        body.add(Box.createVerticalStrut(22));

        // Occupancy bar chart
        body.add(sectionTitle("Room Status Distribution"));
        body.add(Box.createVerticalStrut(8));
        OccupancyChartPanel chart = new OccupancyChartPanel(roomManager);
        chart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        chart.setPreferredSize(new Dimension(0, 110));
        body.add(chart);
        body.add(Box.createVerticalStrut(22));

        // Reservation history
        body.add(sectionTitle("Reservation History"));
        body.add(Box.createVerticalStrut(8));
        resHistModel = new DefaultTableModel(
                new String[]{"Res. ID", "Room ID", "Guest ID", "Check-In", "Check-Out", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        body.add(wrappedTable(styledTable(resHistModel, 5), 180));
        body.add(Box.createVerticalStrut(18));

        // Room occupancy table
        body.add(sectionTitle("Room Occupancy Details"));
        body.add(Box.createVerticalStrut(8));
        occupancyModel = new DefaultTableModel(
                new String[]{"Room ID", "Room No.", "Type", "Price/Night (MYR)", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        body.add(wrappedTable(styledTable(occupancyModel, 4), 180));
        body.add(Box.createVerticalStrut(14));

        JButton refreshBtn = actionBtn("↻  Refresh All Reports", C_GOLD);
        refreshBtn.addActionListener(e -> refreshReports());
        refreshBtn.setAlignmentX(LEFT_ALIGNMENT);
        body.add(refreshBtn);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }

    private JPanel revenueBanner() {
        JPanel card = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, C_SIDEBAR, getWidth(), 0, new Color(18, 45, 75)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Gold accent line at bottom
                g2.setColor(C_GOLD);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(24, getHeight() - 1, 100, getHeight() - 1);
                g2.dispose();
            }
        };
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 88));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(201, 168, 76, 70), 1),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("TOTAL REVENUE GENERATED");
        title.setFont(new Font("Segoe UI", Font.BOLD, 9));
        title.setForeground(C_GOLD);

        revLabel = new JLabel("MYR 0.00");
        revLabel.setFont(new Font("Georgia", Font.BOLD, 30));
        revLabel.setForeground(Color.WHITE);

        JLabel sub = new JLabel("Cumulative · all reservations");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        sub.setForeground(new Color(150, 180, 215));

        left.add(title);
        left.add(Box.createVerticalStrut(3));
        left.add(revLabel);
        left.add(sub);
        card.add(left, BorderLayout.CENTER);

        JLabel icon = new JLabel("💰");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        card.add(icon, BorderLayout.EAST);

        return card;
    }

    public void refreshReports() {
        try {
            if (revLabel != null)
                revLabel.setText(String.format("MYR %.2f", reservationManager.calculateTotalRevenue()));
            if (resHistModel != null) {
                resHistModel.setRowCount(0);
                for (Reservation r : reservationManager.getAllReservations())
                    resHistModel.addRow(new Object[]{r.getReservationId(), r.getRoomId(),
                            r.getGuestId(), r.getCheckInDate(), r.getCheckOutDate(), r.getStatus()});
            }
            if (occupancyModel != null) {
                occupancyModel.setRowCount(0);
                for (Room r : roomManager.getAllRooms())
                    occupancyModel.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(),
                            r.getRoomType(), String.format("%.2f", r.getPricePerNight()), r.getStatus()});
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        log("Reports refreshed.");
        repaint(); // redraw chart
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ROOM MANAGEMENT PANEL
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildRoomsPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_BG);
        p.add(pageHeader("Room Management", "Add, search, update, and remove rooms"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(C_BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        // Left: form
        JPanel form = formCard("Room Form");
        rmId     = formField(form, "Room ID (for edit / delete):"); // id field first
        rmNumber = formField(form, "Room Number:");
        rmType   = formField(form, "Room Type  (Deluxe · Suite · Standard):");
        rmPrice  = formField(form, "Price Per Night (MYR):");
        rmStatus = formField(form, "Status  (AVAILABLE · OCCUPIED · MAINTENANCE):");
        form.add(Box.createVerticalStrut(12));

        rmMsg = feedbackLabel();
        form.add(rmMsg);
        form.add(Box.createVerticalStrut(10));

        JPanel btns = new JPanel(new GridLayout(2, 2, 8, 8));
        btns.setOpaque(false);
        btns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        JButton addBtn  = actionBtn("+ Add Room",       C_GREEN);
        JButton delBtn  = actionBtn("✕ Delete",         C_RED);
        JButton findBtn = actionBtn("⌕ Find by ID",    C_BLUE);
        JButton updBtn  = actionBtn("↻ Update Status",  C_ORANGE);

        addBtn.addActionListener(e -> {
            Double price = safeDouble(rmPrice.getText(), "Price");
            if (price == null) return;
            if (rmNumber.getText().trim().isEmpty()) { rmMsg("Room number required.", false); return; }
            roomManager.addRoom(new Room(0, rmNumber.getText().trim(), rmType.getText().trim(), price, rmStatus.getText().trim().toUpperCase()));
            rmMsg("Room added successfully.", true);
            clearFields(rmId, rmNumber, rmType, rmPrice, rmStatus);
            log("Added room: " + rmNumber.getText().trim());
            refreshRoomTable();
        });
        delBtn.addActionListener(e -> {
            Integer id = safeInt(rmId.getText(), "Room ID"); if (id == null) return;
            if (confirm("Delete room #" + id + "?")) {
                roomManager.removeRoom(id);
                rmMsg("Room #" + id + " deleted.", true);
                log("Deleted room ID: " + id);
                clearFields(rmId, rmNumber, rmType, rmPrice, rmStatus);
                refreshRoomTable();
            }
        });
        findBtn.addActionListener(e -> {
            Integer id = safeInt(rmId.getText(), "Room ID"); if (id == null) return;
            Room room = roomManager.getRoom(id);
            if (room == null) { rmMsg("Room not found.", false); return; }
            rmNumber.setText(room.getRoomNumber()); rmType.setText(room.getRoomType());
            rmPrice.setText(String.valueOf(room.getPricePerNight())); rmStatus.setText(room.getStatus());
            rmMsg("Room loaded.", true);
        });
        updBtn.addActionListener(e -> {
            Integer id = safeInt(rmId.getText(), "Room ID"); if (id == null) return;
            String s = rmStatus.getText().trim().toUpperCase();
            if (s.isEmpty()) { rmMsg("Enter a status first.", false); return; }
            roomManager.updateRoomStatus(id, s);
            rmMsg("Status updated.", true);
            log("Updated room " + id + " → " + s);
            refreshRoomTable();
        });

        btns.add(addBtn); btns.add(delBtn); btns.add(findBtn); btns.add(updBtn);
        form.add(btns);
        form.add(Box.createVerticalGlue());

        // Right: table
        JPanel tableCard = tableCard("All Rooms");
        roomTableModel = new DefaultTableModel(
                new String[]{"ID", "Room No.", "Type", "Price/Night (MYR)", "Status"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = styledTable(roomTableModel, 4);
        tbl.getSelectionModel().addListSelectionListener(e2 -> {
            int row = tbl.getSelectedRow();
            if (row >= 0) {
                rmId.setText(roomTableModel.getValueAt(row, 0).toString());
                rmNumber.setText(roomTableModel.getValueAt(row, 1).toString());
                rmType.setText(roomTableModel.getValueAt(row, 2).toString());
                rmPrice.setText(roomTableModel.getValueAt(row, 3).toString().replace("MYR ", "").replace(",",""));
                rmStatus.setText(roomTableModel.getValueAt(row, 4).toString());
            }
        });
        tableCard.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JButton refBtn = actionBtn("↻ Refresh", C_BLUE);
        refBtn.addActionListener(e -> refreshRoomTable());
        JPanel tbRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        tbRow.setOpaque(false); tbRow.add(refBtn);
        tableCard.add(tbRow, BorderLayout.SOUTH);

        body.add(form,      BorderLayout.WEST);
        body.add(tableCard, BorderLayout.CENTER);
        p.add(body, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::refreshRoomTable);
        return p;
    }

    private void refreshRoomTable() {
        if (roomTableModel == null) return;
        roomTableModel.setRowCount(0);
        for (Room r : roomManager.getAllRooms())
            roomTableModel.addRow(new Object[]{r.getRoomId(), r.getRoomNumber(), r.getRoomType(),
                    String.format("%.2f", r.getPricePerNight()), r.getStatus()});
    }

    private void rmMsg(String msg, boolean ok) { feedbackSet(rmMsg, msg, ok); }

    // ══════════════════════════════════════════════════════════════════════════
    // USER MANAGEMENT PANEL
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildUsersPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_BG);
        p.add(pageHeader("User Management", "Add, search, and manage system user accounts"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 0));
        body.setBackground(C_BG);
        body.setBorder(BorderFactory.createEmptyBorder(20, 28, 20, 28));

        JPanel form = formCard("User Form");
        umId       = formField(form, "User ID (for search / delete / update):");
        umUsername = formField(form, "Username:");
        umPassword = formField(form, "Password:");
        umRole     = formField(form, "Role  (ADMIN · RECEPTIONIST):");
        form.add(Box.createVerticalStrut(12));

        umMsg = feedbackLabel();
        form.add(umMsg);
        form.add(Box.createVerticalStrut(10));

        JPanel btns = new JPanel(new GridLayout(2, 2, 8, 8));
        btns.setOpaque(false);
        btns.setMaximumSize(new Dimension(Integer.MAX_VALUE, 82));

        JButton addBtn  = actionBtn("+ Add User",       C_GREEN);
        JButton delBtn  = actionBtn("✕ Delete",         C_RED);
        JButton findBtn = actionBtn("⌕ Find by ID",    C_BLUE);
        JButton updBtn  = actionBtn("↻ Update Role",   C_ORANGE);

        addBtn.addActionListener(e -> {
            if (umUsername.getText().trim().isEmpty()) { feedbackSet(umMsg, "Username required.", false); return; }
            userManager.addUser(new User(0, umUsername.getText().trim(), umPassword.getText().trim(), umRole.getText().trim().toUpperCase()));
            feedbackSet(umMsg, "User added.", true);
            clearFields(umId, umUsername, umPassword, umRole);
            log("Added user: " + umUsername.getText().trim());
            refreshUserTable();
        });
        delBtn.addActionListener(e -> {
            Integer id = safeInt(umId.getText(), "User ID"); if (id == null) return;
            if (confirm("Delete user #" + id + "?")) {
                userManager.deleteUser(id);
                feedbackSet(umMsg, "User deleted.", true);
                log("Deleted user ID: " + id);
                clearFields(umId, umUsername, umPassword, umRole);
                refreshUserTable();
            }
        });
        findBtn.addActionListener(e -> {
            Integer id = safeInt(umId.getText(), "User ID"); if (id == null) return;
            User u = userManager.getUser(id);
            if (u == null) { feedbackSet(umMsg, "User not found.", false); return; }
            umUsername.setText(u.getUsername()); umPassword.setText(u.getPassword()); umRole.setText(u.getRole());
            feedbackSet(umMsg, "User loaded.", true);
        });
        updBtn.addActionListener(e -> {
            Integer id = safeInt(umId.getText(), "User ID"); if (id == null) return;
            String role = umRole.getText().trim().toUpperCase();
            if (role.isEmpty()) { feedbackSet(umMsg, "Enter a role.", false); return; }
            userManager.updateRole(id, role);
            feedbackSet(umMsg, "Role updated.", true);
            log("Updated role for user " + id);
            refreshUserTable();
        });

        btns.add(addBtn); btns.add(delBtn); btns.add(findBtn); btns.add(updBtn);
        form.add(btns);
        form.add(Box.createVerticalGlue());

        JPanel tableCard = tableCard("All Users");
        userTableModel = new DefaultTableModel(
                new String[]{"ID", "Username", "Password", "Role"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tbl = styledTable(userTableModel, 3);
        tbl.getSelectionModel().addListSelectionListener(e2 -> {
            int row = tbl.getSelectedRow();
            if (row >= 0) {
                umId.setText(userTableModel.getValueAt(row, 0).toString());
                umUsername.setText(userTableModel.getValueAt(row, 1).toString());
                umRole.setText(userTableModel.getValueAt(row, 3).toString());
            }
        });
        tableCard.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JButton refBtn = actionBtn("↻ Refresh", C_BLUE);
        refBtn.addActionListener(e -> refreshUserTable());
        JPanel tbRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        tbRow.setOpaque(false); tbRow.add(refBtn);
        tableCard.add(tbRow, BorderLayout.SOUTH);

        body.add(form,      BorderLayout.WEST);
        body.add(tableCard, BorderLayout.CENTER);
        p.add(body, BorderLayout.CENTER);
        SwingUtilities.invokeLater(this::refreshUserTable);
        return p;
    }

    private void refreshUserTable() {
        if (userTableModel == null) return;
        userTableModel.setRowCount(0);
        for (User u : userManager.getAllUsers())
            userTableModel.addRow(new Object[]{u.getUserId(), u.getUsername(), "••••••••", u.getRole()});
    }

    // ══════════════════════════════════════════════════════════════════════════
    // SHARED UI BUILDERS
    // ══════════════════════════════════════════════════════════════════════════
    private JLabel sectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(new Color(40, 65, 105));
        l.setAlignmentX(LEFT_ALIGNMENT);
        l.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        return l;
    }

    private JPanel formCard(String title) {
        JPanel card = new JPanel();
        card.setBackground(C_WHITE);
        card.setPreferredSize(new Dimension(275, 0));
        card.setMinimumSize(new Dimension(255, 0));
        card.setMaximumSize(new Dimension(295, Integer.MAX_VALUE));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setForeground(new Color(25, 60, 115));
        t.setAlignmentX(LEFT_ALIGNMENT);
        card.add(t);

        JPanel goldLine = new JPanel();
        goldLine.setBackground(C_GOLD);
        goldLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        goldLine.setAlignmentX(LEFT_ALIGNMENT);
        card.add(goldLine);
        card.add(Box.createVerticalStrut(14));
        return card;
    }

    private JPanel tableCard(String title) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(C_WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel t = new JLabel(title);
        t.setFont(new Font("Segoe UI", Font.BOLD, 12));
        t.setForeground(new Color(25, 60, 115));
        t.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        card.add(t, BorderLayout.NORTH);
        return card;
    }

    private JTextField formField(JPanel card, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 10));
        lbl.setForeground(new Color(65, 85, 115));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        card.add(lbl);
        card.add(Box.createVerticalStrut(3));

        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 222, 238)),
                BorderFactory.createEmptyBorder(4, 8, 4, 8)));
        field.setAlignmentX(LEFT_ALIGNMENT);
        card.add(field);
        card.add(Box.createVerticalStrut(9));
        return field;
    }

    private JLabel feedbackLabel() {
        JLabel l = new JLabel(" ");
        l.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private void feedbackSet(JLabel lbl, String msg, boolean ok) {
        lbl.setForeground(ok ? C_GREEN : C_RED);
        lbl.setText(msg);
    }

    JButton actionBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 12, 7, 12));
        return btn;
    }

    JTable styledTable(DefaultTableModel model, int statusCol) {
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        table.setRowHeight(26);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(C_TEXT_DARK);
        table.setFillsViewportHeight(true);

        JTableHeader hdr = table.getTableHeader();
        hdr.setFont(new Font("Segoe UI", Font.BOLD, 11));
        hdr.setBackground(new Color(25, 50, 85));
        hdr.setForeground(Color.WHITE);
        hdr.setReorderingAllowed(false);

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(
                    JTable t, Object val, boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? C_WHITE : new Color(247, 250, 255));
                setForeground(col == statusCol && val != null ? statusColor(val.toString()) : C_TEXT_DARK);
                if (col == statusCol) setFont(new Font("Segoe UI", Font.BOLD, 11));
                setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                return this;
            }
        });
        return table;
    }

    private JScrollPane wrappedTable(JTable table, int height) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(C_BORDER));
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        sp.setPreferredSize(new Dimension(0, height));
        sp.setAlignmentX(LEFT_ALIGNMENT);
        return sp;
    }

    Color statusColor(String s) {
        switch (s.toUpperCase()) {
            case "AVAILABLE":    return C_GREEN;
            case "OCCUPIED":     return C_RED;
            case "RESERVED":     return C_BLUE;
            case "MAINTENANCE":  return C_ORANGE;
            case "CHECKED_IN":   return C_GREEN;
            case "CHECKED_OUT":  return C_TEXT_MID;
            case "BOOKED":       return new Color(14, 116, 144);
            case "ADMIN":        return C_PURPLE;
            case "RECEPTIONIST": return C_BLUE;
            default:             return C_TEXT_DARK;
        }
    }

    private void clearFields(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    private Integer safeInt(String v, String name) {
        try { return Integer.parseInt(v.trim()); }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, name + " must be a valid number."); return null;
        }
    }

    private Double safeDouble(String v, String name) {
        try { return Double.parseDouble(v.trim()); }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, name + " must be a valid number."); return null;
        }
    }

    private boolean confirm(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirm", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION;
    }

    private void log(String msg) {
        activityLog.add("[" + LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm:ss")) + "] " + msg);
    }

    // ── Inner: Occupancy Bar Chart ────────────────────────────────────────────
    private static class OccupancyChartPanel extends JPanel {
        private final RoomManager rm;
        OccupancyChartPanel(RoomManager rm) {
            this.rm = rm;
            setBackground(Color.WHITE);
            setBorder(BorderFactory.createLineBorder(new Color(220, 228, 240)));
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            List<Room> rooms = rm.getAllRooms();
            int total = Math.max(rooms.size(), 1);
            long avl  = rooms.stream().filter(r -> "AVAILABLE".equals(r.getStatus())).count();
            long occ  = rooms.stream().filter(r -> "OCCUPIED".equals(r.getStatus())).count();
            long res  = rooms.stream().filter(r -> "RESERVED".equals(r.getStatus())).count();
            long mnt  = rooms.stream().filter(r -> "MAINTENANCE".equals(r.getStatus())).count();

            String[][] bars = {
                { "Available",   String.valueOf(avl),  "#10B981" },
                { "Occupied",    String.valueOf(occ),  "#DC2626" },
                { "Reserved",    String.valueOf(res),  "#2563EB" },
                { "Maintenance", String.valueOf(mnt),  "#EA7B12" },
            };

            int barH = 18, gap = 8, x = 20, y = 16;
            int labelW = 90, countW = 30, maxBarW = getWidth() - labelW - countW - 60;

            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            for (String[] bar : bars) {
                long count = Long.parseLong(bar[1]);
                int barW = (int) ((count / (double) total) * maxBarW);

                g2.setColor(new Color(235, 240, 250));
                g2.fillRoundRect(x + labelW, y, maxBarW, barH, 6, 6);

                Color c = Color.decode(bar[2]);
                g2.setColor(c);
                if (barW > 0) g2.fillRoundRect(x + labelW, y, barW, barH, 6, 6);

                g2.setColor(new Color(50, 70, 100));
                g2.drawString(bar[0], x, y + barH - 3);

                g2.setColor(c);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                g2.drawString(bar[1], x + labelW + maxBarW + 8, y + barH - 3);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));

                y += barH + gap;
            }
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminDashboardUI().setVisible(true));
    }
}