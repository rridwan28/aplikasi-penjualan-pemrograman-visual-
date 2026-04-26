package ui.forms;
import dao.UserDAO;
import dao.SiswaDAO;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

//DASHBOARD PANEL
//Menampilkan: stat cards (total siswa, guru, kelas, mapel)
public class DashboardAdminPanel extends JPanel{ 

    private AdminMainFrame mainFrame;
    private UserDAO  userDAO  = new UserDAO();
    private SiswaDAO siswaDAO = new SiswaDAO();
 
    public DashboardAdminPanel(AdminMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_APP);
        setBorder(new EmptyBorder(Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL));
        buildUI();
    }
 
private void buildUI() {
    // ── Main container dengan BorderLayout ──
    JPanel mainPanel = new JPanel(new BorderLayout());
    mainPanel.setOpaque(false);
    mainPanel.setBackground(Theme.BG_APP);

    // ── Header Section (NORTH) ──
    JPanel headerPanel = new JPanel();
    headerPanel.setOpaque(false);
    headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
    headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
    headerPanel.setBorder(new EmptyBorder(0, 0, Theme.GAP_XL, 0));

    String today = new SimpleDateFormat("EEEE, dd MMMM yyyy",
                     new java.util.Locale("id","ID")).format(new Date());
     
    JLabel titleLabel = new JLabel("Dashboard Administrator");
    titleLabel.setFont(Theme.FONT_TITLE);
    titleLabel.setForeground(Theme.TEXT_DARK);
    titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    JLabel dateLabel = new JLabel(today + "  |  Semester 1 - Tahun Ajaran 2025/2026");
    dateLabel.setFont(Theme.FONT_SUBTITLE);
    dateLabel.setForeground(Theme.TEXT_MUTED);
    dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
    
    headerPanel.add(titleLabel);
    headerPanel.add(Box.createVerticalStrut(4));
    headerPanel.add(dateLabel);

    // ── Content Section (CENTER) ──
    JPanel contentPanel = new JPanel();
    contentPanel.setOpaque(false);
    contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

    // Stat cards
    JPanel statCards = buildStatCards();
    statCards.setAlignmentX(Component.LEFT_ALIGNMENT);
    contentPanel.add(statCards);
    contentPanel.add(Box.createVerticalStrut(Theme.GAP_LG));

    // Bottom row
    JPanel bottomRow = new JPanel(new GridLayout(1, 2, Theme.GAP_LG, 0));
    bottomRow.setOpaque(false);
    bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
    bottomRow.setAlignmentX(Component.LEFT_ALIGNMENT);
    bottomRow.add(buildRecentActivity());
    bottomRow.add(buildAbsensiChart());
    contentPanel.add(bottomRow);

    contentPanel.add(Box.createVerticalGlue()); // Push ke atas

    // ── Wrap dalam scroll ──
    JScrollPane scroll = new JScrollPane(contentPanel);
    scroll.setBorder(BorderFactory.createEmptyBorder());
    scroll.setOpaque(false);
    scroll.getViewport().setOpaque(false);
    scroll.getViewport().setBackground(Theme.BG_APP);

    // ── Tambahkan ke mainPanel ──
    mainPanel.add(headerPanel, BorderLayout.NORTH);
    mainPanel.add(scroll, BorderLayout.CENTER);

    // ── Tambahkan ke this (DashboardPanel) ──
    add(mainPanel, BorderLayout.CENTER);
}
    // ── Recent Activity Card ──
private JPanel buildRecentActivity() {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(new CompoundBorder(
        new LineBorder(Theme.BORDER, 1, true),
        new EmptyBorder(0, 0, 0, 0)
    ));

    // Header
    JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
    header.setBackground(Color.WHITE);
    header.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT));
    JLabel title = new JLabel("📋  Aktivitas Terbaru");
    title.setFont(Theme.FONT_BOLD);
    title.setForeground(Theme.TEXT_BODY);
    header.add(title);
    card.add(header, BorderLayout.NORTH);

    // Activity list
    JPanel activities = new JPanel();
    activities.setBackground(Color.WHITE);
    activities.setLayout(new BoxLayout(activities, BoxLayout.Y_AXIS));
    activities.setBorder(new EmptyBorder(12, 14, 12, 14));

    String[] activityItems = {
        "✅ Guru Ani menginput nilai UTS Matematika kelas 7A",
        "📝 Admin membuat akun guru baru (Citra Dewi)",
        "👨‍🎓 Siswa baru Budi Santoso terdaftar di Kelas 7B",
        "✅ Absensi kelas 8A diinput oleh Wali Kelas",
        "🔄 Jadwal kelas 9A diperbarui"
    };

    for (String activity : activityItems) {
        JLabel lbl = new JLabel(activity);
        lbl.setFont(Theme.FONT_REGULAR);
        lbl.setForeground(Theme.TEXT_BODY);
        lbl.setBorder(new EmptyBorder(6, 0, 6, 0));
        activities.add(lbl);
    }

    JScrollPane sc = new JScrollPane(activities);
    sc.setBorder(BorderFactory.createEmptyBorder());
    card.add(sc, BorderLayout.CENTER);

    return card;
}

// ── Absensi Chart Card ──
private JPanel buildAbsensiChart() {
    JPanel card = new JPanel(new BorderLayout());
    card.setBackground(Color.WHITE);
    card.setBorder(new CompoundBorder(
        new LineBorder(Theme.BORDER, 1, true),
        new EmptyBorder(0, 0, 0, 0)
    ));

    // Header
    JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 12));
    header.setBackground(Color.WHITE);
    header.setBorder(new MatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT));
    JLabel title = new JLabel("📊  Rekapitulasi Kehadiran Bulan Ini");
    title.setFont(Theme.FONT_BOLD);
    title.setForeground(Theme.TEXT_BODY);
    header.add(title);
    card.add(header, BorderLayout.NORTH);

    // Summary stats
    JPanel summary = new JPanel(new GridLayout(2, 2, 10, 10));
    summary.setBackground(Color.WHITE);
    summary.setBorder(new EmptyBorder(16, 16, 16, 16));

    summary.add(buildSummaryBox("Hadir",      "1,245", Theme.SUCCESS));
    summary.add(buildSummaryBox("Izin",       "89",    Theme.INFO));
    summary.add(buildSummaryBox("Sakit",      "156",   Theme.WARNING));
    summary.add(buildSummaryBox("Alpha",      "34",    Theme.DANGER));

    card.add(summary, BorderLayout.CENTER);

    // Footer
    JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    footer.setBackground(Color.WHITE);
    footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER_LIGHT));
    JLabel hint = new JLabel("Data bulan April 2026");
    hint.setFont(Theme.FONT_SMALL);
    hint.setForeground(Theme.TEXT_MUTED);
    footer.add(hint);
    card.add(footer, BorderLayout.SOUTH);

    return card;
}

    // Helper: summary box dengan angka besar
    private JPanel buildSummaryBox(String label, String value, Color color) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
        p.setBorder(new CompoundBorder(new LineBorder(color, 1, true), new EmptyBorder(12, 12, 12, 12)));

        JLabel valLbl = new JLabel(value, SwingConstants.CENTER);
        valLbl.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 22));
        valLbl.setForeground(color);

        JLabel lblLbl = new JLabel(label, SwingConstants.CENTER);
        lblLbl.setFont(Theme.FONT_SMALL);
        lblLbl.setForeground(color);

        JPanel north = new JPanel();
        north.setOpaque(false);
        north.add(valLbl);
        p.add(north, BorderLayout.CENTER);
        p.add(lblLbl, BorderLayout.SOUTH);

        return p;

    }

    private JPanel buildStatCards() {
    JPanel row = new JPanel(new GridLayout(1, 6, Theme.GAP_MD, 0));
    row.setOpaque(false);
    row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
    row.setAlignmentX(Component.LEFT_ALIGNMENT);

    int totalUser  = userDAO.findAll().size();
    int totalSiswa = siswaDAO.findAll().size();
    int totalGuru  = userDAO.findAllGuru().size();
    int totalKelas = 12;  // placeholder, bisa diambil dari DB
    int totalAbsensiHariIni = 45; // placeholder
    int totalNilaiDiinput = 120;  // placeholder

    row.add(statCard("👥", String.valueOf(totalUser),         "Total User",       Theme.INFO, Theme.INFO_BG));
    row.add(statCard("👨‍🎓", String.valueOf(totalSiswa),        "Total Siswa",      Theme.SUCCESS, Theme.SUCCESS_BG));
    row.add(statCard("👨‍🏫", String.valueOf(totalGuru),         "Total Guru",       Theme.PRIMARY, Theme.PRIMARY_LIGHT));
    row.add(statCard("🏫", String.valueOf(totalKelas),        "Total Kelas",      Theme.WARNING, Theme.WARNING_BG));
    row.add(statCard("✅", String.valueOf(totalAbsensiHariIni),"Absensi Hari Ini", Theme.SUCCESS, Theme.SUCCESS_BG));
    row.add(statCard("📝", String.valueOf(totalNilaiDiinput), "Nilai Diinput",    new Color(0x8B5CF6), new Color(0xF5F3FF)));
    return row;  
    }
 
    private JPanel statCard(String icon, String value, String label, Color bg, Color accent) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
 
        JPanel left = new JPanel(new GridBagLayout());
        left.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 26));
        JPanel iconWrap = new JPanel(new GridBagLayout());
        iconWrap.setBackground(bg);
        iconWrap.setPreferredSize(new Dimension(48, 48));
        iconWrap.add(iconLabel);
        left.add(iconWrap);
 
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(0, 14, 0, 0));
 
        JLabel valLabel = new JLabel(value);
        valLabel.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 26));
        valLabel.setForeground(Theme.TEXT_DARK);
 
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(Theme.FONT_SMALL);
        lblLabel.setForeground(Theme.TEXT_MUTED);
 
        right.add(valLabel);
        right.add(lblLabel);
 
        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        return card;
    }
 
    private JPanel buildQuickActions() {
        JPanel row = new JPanel(new GridLayout(1, 4, Theme.GAP_MD, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
 
        row.add(quickCard("👨‍🎓", "Tambah Siswa",  "Daftarkan siswa baru",     "MASTER_SISWA"));
        row.add(quickCard("🏫", "Kelola Kelas",   "Pembagian kelas & guru",   "TRANS_KELAS"));
        row.add(quickCard("✅", "Input Absensi",  "Catat kehadiran hari ini", "TRANS_ABSENSI"));
        row.add(quickCard("📄", "Cetak Rapot",    "Generate rapot siswa",     "RAPOT"));
        return row;
    }
 
    private JPanel quickCard(String icon, String title, String desc, String pageKey) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(14, 16, 14, 16)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
 
        JLabel iconLbl = new JLabel(icon, SwingConstants.CENTER);
        iconLbl.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 28));
 
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(Theme.FONT_BOLD);
        titleLbl.setForeground(Theme.TEXT_DARK);
 
        JLabel descLbl = new JLabel(desc);
        descLbl.setFont(Theme.FONT_SMALL);
        descLbl.setForeground(Theme.TEXT_MUTED);
 
        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        text.setBorder(new EmptyBorder(0, 10, 0, 0));
        text.add(titleLbl);
        text.add(Box.createVerticalStrut(2));
        text.add(descLbl);
 
        card.add(iconLbl, BorderLayout.WEST);
        card.add(text, BorderLayout.CENTER);
 
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                mainFrame.showPage(pageKey);
            }
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(Theme.PRIMARY_LIGHT);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(Color.WHITE);
            }
        });
 
        return card;
    }
 
    private JLabel buildSectionTitle(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 15));
        l.setForeground(Theme.TEXT_DARK);
        return l;
    }    
}
