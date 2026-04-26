package ui.forms;
 
import ui.components.TopBarPanel;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

/**
 * MAIN FRAME
 * Layout: BorderLayout
 *   NORTH  → TopBarPanel (48px)
 *   WEST   → SidebarPanel (260px)
 *   CENTER → CardLayout content area
 * PreferredSize: 1200 x 700
 */
public class AdminMainFrame extends JFrame {
 
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton activeMenuBtn = null;
 
    // Pages (lazy-loaded)
    private DashboardAdminPanel   dashboardPanel;
    private MasterUserPanel  masterUserPanel;
    private MasterSiswaPanel masterSiswaPanel;
    private MasterGuruPanel  masterGuruPanel;
    private MasterMapelPanel masterMapelPanel;
    private TransKelasPanel  transKelasPanel;
    private LapAbsensiPanel  lapAbsensiPanel;
    private RapotPanel       rapotPanel;
    private RekapPanel       rekapPanel;
    private DaftarNilaiPanel daftarNilaiPanel;
 
    public AdminMainFrame() {
        initUI();
    }
 
    private void initUI() {
        setTitle("Sistem Informasi Sekolah");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1280, 720));
        setMinimumSize(new Dimension(1024, 600));
 
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_APP);
 
        // ─ TopBar ─────────────────────────────────────────────
        root.add(new TopBarPanel(), BorderLayout.NORTH);
 
        // ─ Body (sidebar + content) ───────────────────────────
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.BG_APP);
        body.add(buildSidebar(), BorderLayout.WEST);
 
        // ─ Content CardLayout ─────────────────────────────────
        cardLayout  = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_APP);
        body.add(contentPanel, BorderLayout.CENTER);
 
        // Instantiate & register pages
        dashboardPanel    = new DashboardAdminPanel(this);
        masterUserPanel   = new MasterUserPanel();
        masterSiswaPanel  = new MasterSiswaPanel();
        masterGuruPanel   = new MasterGuruPanel();
        masterMapelPanel  = new MasterMapelPanel();
        transKelasPanel   = new TransKelasPanel();
        lapAbsensiPanel   = new LapAbsensiPanel();
        rapotPanel        = new RapotPanel();
        rekapPanel        = new RekapPanel();
        daftarNilaiPanel  = new DaftarNilaiPanel();
 
        contentPanel.add(dashboardPanel,   "DASHBOARD");
        contentPanel.add(masterUserPanel,  "MASTER_USER");
        contentPanel.add(masterSiswaPanel, "MASTER_SISWA");
        contentPanel.add(masterGuruPanel,  "MASTER_GURU");
        contentPanel.add(masterMapelPanel, "MASTER_MAPEL");
        contentPanel.add(transKelasPanel,  "TRANS_KELAS");
        contentPanel.add(lapAbsensiPanel,  "LAP_ABSENSI");
        contentPanel.add(rapotPanel,       "RAPOT");
        contentPanel.add(rekapPanel,       "REKAP");
        contentPanel.add(daftarNilaiPanel, "DAFTAR_NILAI");
 
        root.add(body, BorderLayout.CENTER);
        setContentPane(root);
 
        pack();
        setLocationRelativeTo(null);
        showPage("DASHBOARD");
    }
 
    // ── Sidebar ───────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(Theme.SIDEBAR_WIDTH, 0));
        sidebar.setBackground(Theme.BG_SIDEBAR);
        sidebar.setLayout(new BorderLayout());
 
        // Header
        JPanel header = new JPanel();
        header.setBackground(Theme.BG_SIDEBAR);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 1, Theme.BORDER),
            new EmptyBorder(16, 20, 14, 20)
        ));
 
        JLabel schoolName = new JLabel("Menu Utama");
        schoolName.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 17));
        schoolName.setForeground(Theme.TEXT_DARK);
 
        String role = Session.isAdmin() ? "Administrator" : "Guru";
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(Theme.FONT_SMALL);
        roleLabel.setForeground(Theme.TEXT_MUTED);
 
        header.add(schoolName);
        header.add(Box.createVerticalStrut(2));
        header.add(roleLabel);
        sidebar.add(header, BorderLayout.NORTH);
 
        // Menu list in scroll pane
        JPanel menuPanel = new JPanel();
        menuPanel.setBackground(Theme.BG_SIDEBAR);
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(new MatteBorder(0, 0, 0, 1, Theme.BORDER));
        menuPanel.add(Box.createVerticalStrut(8));
 
        // ─ Dashboard ─
        addMenuBtnWithIcon(menuPanel, loadIcon("dashboard.png"), "Dashboard", "DASHBOARD");
        addSectionLabel(menuPanel, "MASTER DATA");
        addMenuBtnWithIcon(menuPanel, loadIcon("user.png"), "Master User", "MASTER_USER");
        addMenuBtnWithIcon(menuPanel, loadIcon("siswa.png"), "Master Siswa", "MASTER_SISWA");
        addMenuBtnWithIcon(menuPanel, loadIcon("guru.png"), "Master Guru", "MASTER_GURU");
        addMenuBtnWithIcon(menuPanel, loadIcon("mapel.png"), "Master Mata Pelajaran", "MASTER_MAPEL");
        addSectionLabel(menuPanel, "TRANSAKSI");
        addMenuBtnWithIcon(menuPanel, loadIcon("kelas.png"), "Transaksi Kelas", "TRANS_KELAS");
        addSectionLabel(menuPanel, "LAPORAN");
        addMenuBtnWithIcon(menuPanel, loadIcon("absensi.png"), "Laporan Absensi", "LAP_ABSENSI");
        addMenuBtnWithIcon(menuPanel, loadIcon("rapot.png"), "Rapot Siswa", "RAPOT");
        addMenuBtnWithIcon(menuPanel, loadIcon("rekap.png"), "Rekap Kehadiran", "REKAP");
        addMenuBtnWithIcon(menuPanel, loadIcon("nilai.png"), "Daftar Nilai", "DAFTAR_NILAI");
 
        // Spacer + logout
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.add(Box.createVerticalStrut(10));
 
        JButton btnLogout = createSidebarBtn("Logout", null);
        btnLogout.setForeground(Theme.DANGER);
        btnLogout.addActionListener(e -> doLogout());
        btnLogout.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        menuPanel.add(btnLogout);
        menuPanel.add(Box.createVerticalStrut(8));
 
        JScrollPane scroll = new JScrollPane(menuPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        sidebar.add(scroll, BorderLayout.CENTER);
 
        return sidebar;
    }
    
        private ImageIcon loadIcon(String filename) {
            try {
                java.net.URL imgURL = getClass().getResource("/icons/" + filename);

                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    Image img = icon.getImage().getScaledInstance(18, 18, Image.SCALE_SMOOTH);
                    return new ImageIcon(img);
                }
            } catch (Exception e) {
                System.err.println("Error loading icon: " + filename);
                e.printStackTrace();
            }
            return null;
        }
        
    private void addSectionLabel(JPanel parent, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 10));
        label.setForeground(Theme.TEXT_MUTED);
        label.setBorder(new EmptyBorder(12, 20, 4, 20));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
    }
 
    private void addMenuBtnWithIcon(JPanel parent, ImageIcon icon, String text, String pageKey) {
        JButton btn = createSidebarBtn(text, icon);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.setPreferredSize(new Dimension(Integer.MAX_VALUE, 38));
        btn.addActionListener(e -> {
            showPage(pageKey);
            setActiveMenu(btn);
        });
        parent.add(btn);
 
        if ("DASHBOARD".equals(pageKey)) {
            activeMenuBtn = btn;
            setActive(btn, true);
        }
    }
 
    private JButton createSidebarBtn(String text, ImageIcon icon) {
        JButton btn = new JButton(text) {
            boolean active = false;
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
                boolean hov = getModel().isRollover();
                if (active) {
                    g2.setColor(Theme.PRIMARY_LIGHT);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Theme.PRIMARY);
                    g2.fillRect(0, 0, 3, getHeight()); // left accent bar
                } else if (hov) {
                    g2.setColor(new Color(0xF5F3FF));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                g2.dispose();
                super.paintComponent(g);
            }
            public void setActive(boolean a) {
                this.active = a;
                setFont(a ? Theme.FONT_MENU_ACTIVE : Theme.FONT_MENU);
                setForeground(a ? Theme.PRIMARY : Theme.TEXT_BODY);
                repaint();
            }
        };
        btn.setIcon(icon);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setHorizontalTextPosition(SwingConstants.RIGHT);
        btn.setIconTextGap(10);        
        btn.setFont(Theme.FONT_MENU);
        btn.setForeground(Theme.TEXT_BODY);
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
 
    private void setActive(JButton btn, boolean active) {
        try {
            btn.getClass().getMethod("setActive", boolean.class).invoke(btn, active);
        } catch (Exception ignored) {}
    }
 
    private void setActiveMenu(JButton btn) {
        if (activeMenuBtn != null) setActive(activeMenuBtn, false);
        activeMenuBtn = btn;
        setActive(btn, true);
    }
 
    /** Navigate to a page by key and refresh its data. */
    public void showPage(String key) {
        cardLayout.show(contentPanel, key);
        switch (key) {
            case "MASTER_USER":  masterUserPanel.loadData();  break;
            case "MASTER_SISWA": masterSiswaPanel.loadData(); break;
            case "MASTER_GURU":  masterGuruPanel.loadData();  break;
            case "MASTER_MAPEL": masterMapelPanel.loadData(); break;
            case "TRANS_KELAS":  transKelasPanel.loadData();  break;
            case "LAP_ABSENSI":  lapAbsensiPanel.init();      break;
            case "RAPOT":        rapotPanel.init();           break;
            case "REKAP":        rekapPanel.init();           break;
            case "DAFTAR_NILAI": daftarNilaiPanel.init();     break;
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
    
