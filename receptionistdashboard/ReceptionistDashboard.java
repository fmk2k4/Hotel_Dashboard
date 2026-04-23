package com.mycompany.receptionistdashboard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ReceptionistDashboard — Professional sidebar-navigated receptionist panel.
 * Sections: Rooms · Guests · Reservations
 */
public class ReceptionistDashboard extends JFrame {

    // Theme (mirrors AdminDashboardUI for visual consistency)
    private static final Color C_SIDEBAR     = AdminDashboardUI.C_SIDEBAR;
    private static final Color C_SIDEBAR_HV  = AdminDashboardUI.C_SIDEBAR_HV;
    private static final Color C_SIDEBAR_SEL = AdminDashboardUI.C_SIDEBAR_SEL;
    private static final Color C_GOLD        = AdminDashboardUI.C_GOLD;
    private static final Color C_GOLD_LT     = AdminDashboardUI.C_GOLD_LT;
    private static final Color C_BG          = AdminDashboardUI.C_BG;
    private static final Color C_WHITE       = Color.WHITE;
    private static final Color C_BORDER      = AdminDashboardUI.C_BORDER;
    private static final Color C_TEXT_DARK   = AdminDashboardUI.C_TEXT_DARK;
    private static final Color C_TEXT_MID    = AdminDashboardUI.C_TEXT_MID;
    private static final Color C_GREEN       = AdminDashboardUI.C_GREEN;
    private static final Color C_BLUE        = AdminDashboardUI.C_BLUE;

    private final CardLayout contentLayout = new CardLayout();
    private JPanel           contentPanel;
    private JButton          activeNavBtn  = null;

    // Stats labels in sidebar
    private JLabel sidebarAvailRooms;
    private JLabel sidebarTotalGuests;

    private final RoomManager        roomManager;
    private final GuestManager       guestManager;
    private final ReservationManager reservationManager;

    public ReceptionistDashboard() {
        this(new RoomManager(), new GuestManager(), null);
    }

    public ReceptionistDashboard(RoomManager rm, GuestManager gm, ReservationManager rsm) {
        this.roomManager        = rm  != null ? rm  : new RoomManager();
        this.guestManager       = gm  != null ? gm  : new GuestManager();
        this.reservationManager = rsm != null ? rsm : new ReservationManager(this.roomManager);
        buildFrame();
    }

    private void buildFrame() {
        setTitle("Grand Azure — Receptionist Dashboard");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1050, 680);
        setMinimumSize(new Dimension(880, 560));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildContent(), BorderLayout.CENTER);
        setContentPane(root);
    }

    // ── Sidebar ───────────────────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sb = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(C_SIDEBAR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(new Color(201, 168, 76, 40));
                g2.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
                g2.dispose();
            }
        };
        sb.setPreferredSize(new Dimension(220, 0));
        sb.setLayout(new BoxLayout(sb, BoxLayout.Y_AXIS));

        sb.add(buildBrand());
        sb.add(goldRule());
        sb.add(Box.createVerticalStrut(6));
        sb.add(secLabel("NAVIGATION"));

        String[][] items = {
            { "◈  Room Status",   "ROOMS"        },
            { "◈  Guests",        "GUESTS"       },
            { "◈  Reservations",  "RESERVATIONS" },
        };
        List<JButton> btns = new ArrayList<>();
        for (String[] item : items) {
            JButton btn = navButton(item[0], item[1], btns);
            btns.add(btn);
            sb.add(btn);
        }
        if (!btns.isEmpty()) setActive(btns.get(0));

        sb.add(Box.createVerticalStrut(14));
        sb.add(goldRule());
        sb.add(Box.createVerticalStrut(10));

        // Quick stats card in sidebar
        sb.add(buildQuickStats());

        sb.add(Box.createVerticalGlue());
        sb.add(goldRule());
        sb.add(buildFooter());
        return sb;
    }

    private JPanel buildBrand() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(24, 22, 16, 22));
        p.setMaximumSize(new Dimension(220, 110));

        JLabel stars = new JLabel("★  ★  ★  ★  ★");
        stars.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        stars.setForeground(C_GOLD);

        JLabel name = new JLabel("GRAND AZURE");
        name.setFont(new Font("Georgia", Font.BOLD, 16));
        name.setForeground(C_GOLD_LT);

        JLabel sub = new JLabel("Hotel & Suites");
        sub.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        sub.setForeground(new Color(160, 185, 215));

        JLabel role = new JLabel("  FRONT DESK");
        role.setFont(new Font("Segoe UI", Font.BOLD, 9));
        role.setForeground(C_GREEN);
        role.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(16, 185, 129, 100), 1),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)));

        p.add(stars);
        p.add(Box.createVerticalStrut(4));
        p.add(name);
        p.add(sub);
        p.add(Box.createVerticalStrut(7));
        p.add(role);
        return p;
    }

    private JPanel buildQuickStats() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        p.setMaximumSize(new Dimension(220, 120));

        JLabel title = new JLabel("QUICK STATS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 8));
        title.setForeground(new Color(90, 120, 155));
        p.add(title);
        p.add(Box.createVerticalStrut(6));

        sidebarAvailRooms  = miniStatRow(p, "Available Rooms:", "—", C_GREEN);
        sidebarTotalGuests = miniStatRow(p, "Total Guests:",    "—", C_BLUE);

        JButton refreshStats = new JButton("↻ Update");
        refreshStats.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        refreshStats.setForeground(new Color(140, 170, 205));
        refreshStats.setBackground(C_SIDEBAR);
        refreshStats.setBorderPainted(false);
        refreshStats.setFocusPainted(false);
        refreshStats.setAlignmentX(LEFT_ALIGNMENT);
        refreshStats.addActionListener(e -> updateQuickStats());
        p.add(Box.createVerticalStrut(4));
        p.add(refreshStats);
        return p;
    }

    private JLabel miniStatRow(JPanel parent, String label, String value, Color accent) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(184, 22));

        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lbl.setForeground(new Color(140, 170, 205));

        JLabel val = new JLabel(value);
        val.setFont(new Font("Segoe UI", Font.BOLD, 11));
        val.setForeground(accent);

        row.add(lbl, BorderLayout.WEST);
        row.add(val, BorderLayout.EAST);
        parent.add(row);
        parent.add(Box.createVerticalStrut(3));
        return val;
    }

    private void updateQuickStats() {
        try {
            long avail = roomManager.getAllRooms().stream()
                    .filter(r -> "AVAILABLE".equals(r.getStatus())).count();
            int guests = guestManager.getAllGuests().size();
            if (sidebarAvailRooms  != null) sidebarAvailRooms.setText(String.valueOf(avail));
            if (sidebarTotalGuests != null) sidebarTotalGuests.setText(String.valueOf(guests));
        } catch (Exception ignored) {}
    }

    private Component goldRule() {
        JPanel r = new JPanel();
        r.setBackground(new Color(201, 168, 76, 55));
        r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        r.setPreferredSize(new Dimension(0, 1));
        return r;
    }

    private JLabel secLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 8));
        l.setForeground(new Color(90, 120, 155));
        l.setBorder(BorderFactory.createEmptyBorder(10, 22, 6, 22));
        l.setMaximumSize(new Dimension(220, 30));
        return l;
    }

    private JButton navButton(String text, String card, List<JButton> group) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(new Color(175, 200, 230));
        btn.setBackground(C_SIDEBAR);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(220, 44));
        btn.setPreferredSize(new Dimension(220, 44));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                if (btn != activeNavBtn) { btn.setBackground(C_SIDEBAR_HV); btn.setForeground(Color.WHITE); }
            }
            @Override public void mouseExited(MouseEvent e) {
                if (btn != activeNavBtn) { btn.setBackground(C_SIDEBAR); btn.setForeground(new Color(175, 200, 230)); }
            }
        });
        btn.addActionListener(e -> {
            setActive(btn);
            contentLayout.show(contentPanel, card);
        });
        return btn;
    }

    private void setActive(JButton btn) {
        if (activeNavBtn != null) {
            activeNavBtn.setBackground(C_SIDEBAR);
            activeNavBtn.setForeground(new Color(175, 200, 230));
        }
        activeNavBtn = btn;
        btn.setBackground(C_SIDEBAR_SEL);
        btn.setForeground(C_GOLD_LT);
    }

    private JPanel buildFooter() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(10, 22, 18, 22));
        p.setMaximumSize(new Dimension(220, 65));

        JLabel who = new JLabel("Logged in as: Receptionist");
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

    // ── Content ───────────────────────────────────────────────────────────────
    private JPanel buildContent() {
        contentPanel = new JPanel(contentLayout);
        contentPanel.setBackground(C_BG);
        contentPanel.add(new RoomPanel(roomManager),            "ROOMS");
        contentPanel.add(new GuestPanel(guestManager),          "GUESTS");
        contentPanel.add(new ReservationPanel(
                reservationManager, roomManager, guestManager),  "RESERVATIONS");
        contentLayout.show(contentPanel, "ROOMS");
        SwingUtilities.invokeLater(this::updateQuickStats);
        return contentPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReceptionistDashboard::new);
    }
}