package ui.forms;

import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class GuruMainFrame extends JFrame {
    
    // Warna sidebar guru (dark navy seperti referensi foto)
    private static final Color SIDEBAR_BG       = new Color(0x1E293B);
    private static final Color SIDEBAR_ACTIVE_BG = new Color(0x3B82F6);
    private static final Color SIDEBAR_HOVER_BG  = new Color(0x334155);
    private static final Color SIDEBAR_TEXT      = new Color(0xCBD5E1);
    private static final Color SIDEBAR_TEXT_ACT  = Color.WHITE;
    private static final Color SIDEBAR_SECTION   = new Color(0x64748B);
 
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton activeMenuBtn = null;
 
    // Pages
    private DashboardGuruPanel   dashboardPanel;
    private JadwalMengajarPanel  jadwalPanel;
    private TransAbsensiPanel    absensiPanel;
    private TransNilaiPanel      nilaiPanel;
    
    // TopBar info label (untuk update nama)
    private JLabel topUserLabel;
 
    public GuruMainFrame() {
        initUI();
    }
 
    private void initUI() {
        setTitle("Panel Guru — Sistem Informasi Sekolah");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1024, 600));
 
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_APP);
 
        // ── TopBar guru (gelap dengan info nama) ──
        root.add(buildTopBar(), BorderLayout.NORTH);
 
        // ── Body ──
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.BG_APP);
        body.add(buildSidebar(), BorderLayout.WEST);
 
        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_APP);
        body.add(contentPanel, BorderLayout.CENTER);
 
        // Instantiate pages
        dashboardPanel = new DashboardGuruPanel(this);
        jadwalPanel    = new JadwalMengajarPanel();
        absensiPanel   = new TransAbsensiPanel();
        nilaiPanel     = new TransNilaiPanel();
 
        contentPanel.add(dashboardPanel, "DASHBOARD");
        contentPanel.add(jadwalPanel,    "JADWAL");
        contentPanel.add(absensiPanel,   "TRANS_ABSENSI");
        contentPanel.add(nilaiPanel,     "TRANS_NILAI");
 
        root.add(body, BorderLayout.CENTER);
        setContentPane(root);
 
        pack();
        setLocationRelativeTo(null);
        showPage("DASHBOARD");
    }
 
    // ── TopBar ───────────────────────────────────────────────────
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0x0F172A)); // sangat gelap
        bar.setPreferredSize(new Dimension(0, Theme.TOPBAR_HEIGHT));
        bar.setBorder(new EmptyBorder(0, 16, 0, 16));
 
        // Left: logo + title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        JLabel logoIcon = new JLabel("🎓");
        logoIcon.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 20));
        JLabel appName = new JLabel("Panel Guru");
        appName.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 16));
        appName.setForeground(Color.WHITE);
        left.add(logoIcon); left.add(appName);
 
        // Right: tahun ajaran + nama user + initial avatar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        right.setOpaque(false);
 
        JLabel taLabel = new JLabel("Tahun Ajaran: 2025/2026 - Genap");
        taLabel.setFont(Theme.FONT_SMALL);
        taLabel.setForeground(new Color(0x94A3B8));
 
        // Avatar circle dengan inisial
        String nama   = Session.currentUser.getNamaLengkap();
        String[] parts = nama.split(" ");
        String inisial = parts.length >= 2
            ? ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase()
            : nama.substring(0, Math.min(2, nama.length())).toUpperCase();
 
        JPanel avatar = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.PRIMARY);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setOpaque(false);
        avatar.setPreferredSize(new Dimension(32, 32));
        JLabel initLabel = new JLabel(inisial);
        initLabel.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 12));
        initLabel.setForeground(Color.WHITE);
        avatar.add(initLabel);
 
        right.add(taLabel); right.add(avatar);
        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── Sidebar gelap ─────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 0));
        sidebar.setBackground(SIDEBAR_BG);
 
        // ── Profile section ──
        JPanel profile = new JPanel();
        profile.setBackground(SIDEBAR_BG);
        profile.setLayout(new BoxLayout(profile, BoxLayout.Y_AXIS));
        profile.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(0x334155)),
            new EmptyBorder(20, 0, 16, 0)
        ));
 
        // Avatar besar
        JPanel avatarBig = new JPanel(new GridBagLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0x475569));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatarBig.setOpaque(false);
        avatarBig.setPreferredSize(new Dimension(56, 56));
        avatarBig.setMaximumSize(new Dimension(56, 56));
        String nama   = Session.currentUser.getNamaLengkap();
        String[] parts = nama.split(" ");
        String inisial = parts.length >= 2
            ? ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase()
            : nama.substring(0, Math.min(2, nama.length())).toUpperCase();
        JLabel initBig = new JLabel(inisial);
        initBig.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 20));
        initBig.setForeground(Color.WHITE);
        avatarBig.add(initBig);
        avatarBig.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel nameLabel = new JLabel(nama, SwingConstants.CENTER);
        nameLabel.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 13));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        String roleLabel = "NIP. " + (Session.currentUser.getUsername());
        JLabel nipLabel = new JLabel(roleLabel, SwingConstants.CENTER);
        nipLabel.setFont(Theme.FONT_SMALL);
        nipLabel.setForeground(SIDEBAR_SECTION);
        nipLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        profile.add(avatarBig);
        profile.add(Box.createVerticalStrut(10));
        profile.add(nameLabel);
        profile.add(Box.createVerticalStrut(3));
        profile.add(nipLabel);
        sidebar.add(profile, BorderLayout.NORTH);
        
        // ── Menu items ──
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(SIDEBAR_BG);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new EmptyBorder(12, 0, 0, 0));
 
        addGuruMenu(menuPanel, "🏠", "Dashboard Guru",     "DASHBOARD");
        addGuruMenu(menuPanel, "📅", "Jadwal Mengajar",    "JADWAL");
        addGuruMenu(menuPanel, "✅", "Input Absensi",      "TRANS_ABSENSI");
        addGuruMenu(menuPanel, "📝", "Input Nilai",        "TRANS_NILAI");
 
        menuPanel.add(Box.createVerticalGlue());
 
        // Logout button
        JButton btnLogout = createGuruMenuBtn("🚪", "Logout");
        btnLogout.setForeground(new Color(0xFF6B6B));
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btnLogout.addActionListener(e -> doLogout());
        menuPanel.add(btnLogout);
        menuPanel.add(Box.createVerticalStrut(8));
 
        JScrollPane scroll = new JScrollPane(menuPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        scroll.setBackground(SIDEBAR_BG);
        scroll.getViewport().setBackground(SIDEBAR_BG);
        sidebar.add(scroll, BorderLayout.CENTER);
 
        return sidebar;
    }

    private void addGuruMenu(JPanel parent, String icon, String label, String pageKey) {
        JButton btn = createGuruMenuBtn(icon, label);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        btn.addActionListener(e -> {
            showPage(pageKey);
            setActiveGuruMenu(btn);
        });
        parent.add(btn);
 
        if ("DASHBOARD".equals(pageKey)) {
            activeMenuBtn = btn;
            setGuruActive(btn, true);
        }
    }
    
    private JButton createGuruMenuBtn(String icon, String label) {
        JButton btn = new JButton(icon + "  " + label) {
            boolean active = false;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (active) {
                    g2.setColor(SIDEBAR_ACTIVE_BG);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    // left bar accent
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, 4, getHeight());
                } else if (getModel().isRollover()) {
                    g2.setColor(SIDEBAR_HOVER_BG);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
            public void setActive(boolean a) {
                this.active = a;
                setForeground(a ? SIDEBAR_TEXT_ACT : SIDEBAR_TEXT);
                setFont(a ? Theme.FONT_MENU_ACTIVE : Theme.FONT_MENU);
                repaint();
            }
        };
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(Theme.FONT_MENU);
        btn.setForeground(SIDEBAR_TEXT);
        btn.setBackground(SIDEBAR_BG);
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }

    private void setGuruActive(JButton btn, boolean active) {
        try {
            btn.getClass().getMethod("setActive", boolean.class).invoke(btn, active);
        } catch (Exception ignored) {}
    }
 
    private void setActiveGuruMenu(JButton btn) {
        if (activeMenuBtn != null) setGuruActive(activeMenuBtn, false);
        activeMenuBtn = btn;
        setGuruActive(btn, true);
    }
 
    public void showPage(String key) {
        cardLayout.show(contentPanel, key);
        switch (key) {
            case "DASHBOARD":     dashboardPanel.init(); break;
            case "JADWAL":        jadwalPanel.init(); break;
            case "TRANS_ABSENSI": absensiPanel.init(); break;
            case "TRANS_NILAI":   nilaiPanel.init(); break;
        }
    }
 
    private void doLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin logout?", "Konfirmasi Logout",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            Session.logout();
            dispose();
            new LoginForm().setVisible(true);
        }
    }           
}
