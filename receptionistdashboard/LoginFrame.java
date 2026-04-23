package com.mycompany.receptionistdashboard;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * LoginFrame — Luxury split-panel hotel login screen.
 * Left: Branding panel (dark navy + gold). Right: Login form (crisp white).
 */
public class LoginFrame extends JFrame {

    // ── Theme ─────────────────────────────────────────────────────────────────
    private static final Color C_NAVY      = new Color(10, 24, 40);
    private static final Color C_NAVY_DARK = new Color(7, 16, 28);
    private static final Color C_GOLD      = new Color(201, 168, 76);
    private static final Color C_GOLD_LT   = new Color(232, 197, 106);
    private static final Color C_WHITE     = Color.WHITE;
    private static final Color C_BG        = new Color(248, 250, 253);
    private static final Color C_BORDER    = new Color(218, 228, 242);
    private static final Color C_TEXT      = new Color(15, 30, 50);
    private static final Color C_MUTED     = new Color(100, 116, 139);
    private static final Color C_ERROR     = new Color(220, 38, 38);
    private static final Color C_SUCCESS   = new Color(16, 185, 129);

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JLabel         messageLabel;
    private JButton        loginBtn;

    private final UserManager userManager;

    public LoginFrame() {
        this.userManager = new UserManager();
        buildFrame();
    }

    // ── Frame ─────────────────────────────────────────────────────────────────
    private void buildFrame() {
        setTitle("Grand Azure Hotel — Login");
        setSize(820, 540);
        setMinimumSize(new Dimension(700, 480));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildBrandPanel(), buildFormPanel());
        split.setDividerSize(0);
        split.setDividerLocation(340);
        split.setBorder(null);
        split.setEnabled(false);

        setContentPane(split);
        setVisible(true);

        // Default focus on username
        SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
    }

    // ── Left: Branding Panel ──────────────────────────────────────────────────
    private JPanel buildBrandPanel() {
        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Gradient background
                GradientPaint gp = new GradientPaint(0, 0, C_NAVY_DARK, 0, getHeight(), new Color(18, 40, 68));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // Decorative diagonal stripe (subtle)
                g2.setColor(new Color(255, 255, 255, 8));
                g2.setStroke(new BasicStroke(60));
                g2.drawLine(-20, getHeight(), getWidth() + 20, 0);
                // Gold bottom accent line
                g2.setColor(C_GOLD);
                g2.setStroke(new BasicStroke(2));
                g2.drawLine(40, getHeight() - 40, getWidth() - 40, getHeight() - 40);
                g2.dispose();
            }
        };
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));

        p.add(Box.createVerticalGlue());

        // Stars
        JLabel stars = new JLabel("★  ★  ★  ★  ★");
        stars.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        stars.setForeground(C_GOLD);
        stars.setAlignmentX(LEFT_ALIGNMENT);
        p.add(stars);
        p.add(Box.createVerticalStrut(14));

        // Hotel name
        JLabel name = new JLabel("<html><span style='font-size:22px'>GRAND AZURE</span></html>");
        name.setFont(new Font("Georgia", Font.BOLD, 26));
        name.setForeground(C_GOLD_LT);
        name.setAlignmentX(LEFT_ALIGNMENT);
        p.add(name);

        JLabel tagline = new JLabel("Hotel & Suites");
        tagline.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        tagline.setForeground(new Color(180, 200, 228));
        tagline.setAlignmentX(LEFT_ALIGNMENT);
        p.add(tagline);

        p.add(Box.createVerticalStrut(28));

        // Decorative divider
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(201, 168, 76, 80));
        sep.setMaximumSize(new Dimension(200, 1));
        sep.setAlignmentX(LEFT_ALIGNMENT);
        p.add(sep);
        p.add(Box.createVerticalStrut(20));

        // Quote
        JLabel quote = new JLabel("<html><i>\"Where every guest is<br>treated as royalty.\"</i></html>");
        quote.setFont(new Font("Georgia", Font.ITALIC, 13));
        quote.setForeground(new Color(160, 185, 215));
        quote.setAlignmentX(LEFT_ALIGNMENT);
        p.add(quote);

        p.add(Box.createVerticalStrut(32));

        // Feature bullets
        String[] features = { "24/7 Concierge Service", "Premium Room Management", "Secure Access Control" };
        for (String f : features) {
            JLabel bullet = new JLabel("◆  " + f);
            bullet.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            bullet.setForeground(new Color(140, 165, 200));
            bullet.setAlignmentX(LEFT_ALIGNMENT);
            p.add(bullet);
            p.add(Box.createVerticalStrut(6));
        }

        p.add(Box.createVerticalGlue());

        // Version footer
        JLabel version = new JLabel("v1.0 — Hotel Management System");
        version.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        version.setForeground(new Color(80, 105, 140));
        version.setAlignmentX(LEFT_ALIGNMENT);
        p.add(version);
        p.add(Box.createVerticalStrut(30));

        return p;
    }

    // ── Right: Login Form Panel ───────────────────────────────────────────────
    private JPanel buildFormPanel() {
        JPanel outer = new JPanel(new GridBagLayout());
        outer.setBackground(C_BG);

        JPanel card = new JPanel();
        card.setBackground(C_WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1),
                BorderFactory.createEmptyBorder(36, 36, 36, 36)));
        card.setPreferredSize(new Dimension(340, 380));
        card.setMaximumSize(new Dimension(380, 420));

        // Title
        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Georgia", Font.BOLD, 22));
        title.setForeground(C_TEXT);
        title.setAlignmentX(LEFT_ALIGNMENT);
        card.add(title);
        card.add(Box.createVerticalStrut(4));

        JLabel sub = new JLabel("Access the management portal");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(C_MUTED);
        sub.setAlignmentX(LEFT_ALIGNMENT);
        card.add(sub);
        card.add(Box.createVerticalStrut(6));

        // Gold accent line
        JPanel accentLine = new JPanel();
        accentLine.setBackground(C_GOLD);
        accentLine.setMaximumSize(new Dimension(50, 2));
        accentLine.setPreferredSize(new Dimension(50, 2));
        accentLine.setMinimumSize(new Dimension(50, 2));
        accentLine.setAlignmentX(LEFT_ALIGNMENT);
        card.add(accentLine);
        card.add(Box.createVerticalStrut(22));

        // Username
        card.add(makeFieldLabel("Username"));
        card.add(Box.createVerticalStrut(5));
        usernameField = makeTextField(false);
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));

        // Password
        card.add(makeFieldLabel("Password"));
        card.add(Box.createVerticalStrut(5));
        passwordField = (JPasswordField) makeTextField(true);
        card.add(passwordField);
        card.add(Box.createVerticalStrut(22));

        // Login button
        loginBtn = new JButton("Sign In  →");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        loginBtn.setBackground(C_NAVY);
        loginBtn.setForeground(C_WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);
        loginBtn.addActionListener(e -> handleLogin());
        loginBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { loginBtn.setBackground(new Color(20, 45, 75)); }
            @Override public void mouseExited(MouseEvent e)  { loginBtn.setBackground(C_NAVY); }
        });
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(12));

        // Message label
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        messageLabel.setAlignmentX(LEFT_ALIGNMENT);
        card.add(messageLabel);
        card.add(Box.createVerticalGlue());

        // Enter key support
        KeyAdapter enterKey = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) handleLogin();
            }
        };
        usernameField.addKeyListener(enterKey);
        passwordField.addKeyListener(enterKey);

        outer.add(card);
        return outer;
    }

    // ── Role Picker Screen ────────────────────────────────────────────────────
    private void showRoleScreen() {
        getContentPane().removeAll();
        setSize(460, 340);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(C_BG);

        JPanel card = new JPanel();
        card.setBackground(C_WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER),
                BorderFactory.createEmptyBorder(32, 32, 32, 32)));
        card.setPreferredSize(new Dimension(340, 260));

        JLabel authIcon = new JLabel("✓  Identity Verified");
        authIcon.setFont(new Font("Segoe UI", Font.BOLD, 13));
        authIcon.setForeground(C_SUCCESS);
        authIcon.setAlignmentX(LEFT_ALIGNMENT);
        card.add(authIcon);
        card.add(Box.createVerticalStrut(6));

        JLabel prompt = new JLabel("Select your access role to continue:");
        prompt.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prompt.setForeground(C_MUTED);
        prompt.setAlignmentX(LEFT_ALIGNMENT);
        card.add(prompt);
        card.add(Box.createVerticalStrut(22));

        JButton adminBtn = makeRoleButton("  ◈  Admin Dashboard", C_NAVY);
        adminBtn.addActionListener(e -> openDashboard("ADMIN"));
        card.add(adminBtn);
        card.add(Box.createVerticalStrut(12));

        JButton receptionBtn = makeRoleButton("  ◈  Receptionist Dashboard", new Color(16, 100, 80));
        receptionBtn.addActionListener(e -> openDashboard("RECEPTIONIST"));
        card.add(receptionBtn);

        root.add(card);
        setContentPane(root);
        revalidate();
        repaint();
    }

    // ── Logic ─────────────────────────────────────────────────────────────────
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Username and password are required.", false);
            return;
        }

        loginBtn.setEnabled(false);
        loginBtn.setText("Signing in…");

        User user = userManager.getUserByCredentials(username, password);
        loginBtn.setEnabled(true);
        loginBtn.setText("Sign In  →");

        if (user == null) {
            showMessage("Invalid username or password.", false);
            passwordField.setText("");
            usernameField.selectAll();
            usernameField.requestFocusInWindow();
            return;
        }

        showRoleScreen();
    }

    private void openDashboard(String role) {
        dispose();
        RoomManager rm = new RoomManager();
        GuestManager gm = new GuestManager();
        ReservationManager rsm = new ReservationManager(rm);

        if ("ADMIN".equals(role)) {
            SwingUtilities.invokeLater(() -> new AdminDashboardUI(rm, new UserManager(), rsm).setVisible(true));
        } else {
            SwingUtilities.invokeLater(() -> new ReceptionistDashboard(rm, gm, rsm).setVisible(true));
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────
    private JLabel makeFieldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(50, 70, 100));
        lbl.setAlignmentX(LEFT_ALIGNMENT);
        return lbl;
    }

    private JTextField makeTextField(boolean isPassword) {
        JTextField field = isPassword ? new JPasswordField() : new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(C_BORDER, 1),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(LEFT_ALIGNMENT);
        // Gold border on focus
        field.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_GOLD, 1),
                        BorderFactory.createEmptyBorder(9, 12, 9, 12)));
            }
            @Override public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(C_BORDER, 1),
                        BorderFactory.createEmptyBorder(9, 12, 9, 12)));
            }
        });
        return field;
    }

    private JButton makeRoleButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setBackground(bg);
        btn.setForeground(C_WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.setAlignmentX(LEFT_ALIGNMENT);
        return btn;
    }

    private void showMessage(String msg, boolean success) {
        messageLabel.setForeground(success ? C_SUCCESS : C_ERROR);
        messageLabel.setText(msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginFrame::new);
    }
}