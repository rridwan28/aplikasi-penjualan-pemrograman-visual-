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
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
 
        // ─ Page header ─
        String nama  = Session.currentUser.getNamaLengkap();
        String today = new SimpleDateFormat("EEEE, dd MMMM yyyy",
                         new java.util.Locale("id","ID")).format(new Date());
 
        JLabel titleLabel = new JLabel("Selamat Datang, " + nama + "! 👋");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);
 
        JLabel dateLabel = new JLabel(today + "  |  Semester 1 - Tahun Ajaran 2025/2026");
        dateLabel.setFont(Theme.FONT_SUBTITLE);
        dateLabel.setForeground(Theme.TEXT_MUTED);
 
        wrapper.add(titleLabel);
        wrapper.add(Box.createVerticalStrut(4));
        wrapper.add(dateLabel);
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XL));
 
        // ─ Stat cards ─
        wrapper.add(buildStatCards());
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XL));
 
        // ─ Quick actions ─
        wrapper.add(buildSectionTitle("⚡ Aksi Cepat"));
        wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));
        wrapper.add(buildQuickActions());
 
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }
 
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, Theme.GAP_MD, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
 
        int totalSiswa = siswaDAO.findAll().size();
        int totalGuru  = userDAO.findAllGuru().size();
 
        row.add(statCard("👨‍🎓", String.valueOf(totalSiswa), "Total Siswa",    Theme.INFO_BG,    Theme.INFO));
        row.add(statCard("👨‍🏫", String.valueOf(totalGuru),  "Guru Aktif",     Theme.SUCCESS_BG, Theme.SUCCESS));
        row.add(statCard("🏫", "12",                        "Kelas Aktif",    Theme.WARNING_BG, Theme.WARNING));
        row.add(statCard("📚", "7",                         "Mata Pelajaran", new Color(0xEDE9FE), Theme.PRIMARY));
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
