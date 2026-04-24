package ui.forms;
import dao.GuruDAO;
import model.Guru;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * DashboardGuruPanel
 * Layout:
 *   - Header sambutan (nama, NIP, mapel)
 *   - 4 stat cards: Kelas Diampu, Jam Mengajar, Tugas Dinilai, Total Siswa
 *   - 2 kolom bawah: Akses Cepat | Jadwal Mengajar Hari Ini
 */
public class DashboardGuruPanel extends JPanel {
 
    private GuruMainFrame mainFrame;
    private GuruDAO guruDAO = new GuruDAO();
    private Guru guruProfil;
 
    public DashboardGuruPanel(GuruMainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setBackground(Theme.BG_APP);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL));
    }
 
    /** Dipanggil setiap kali dashboard ditampilkan — load data fresh. */
    public void init() {
        removeAll();
        guruProfil = guruDAO.findByUserId(Session.currentUser.getUserId());
        buildUI();
        revalidate();
        repaint();
    }
 
    private void buildUI() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
 
        // ── Welcome card ──
        wrapper.add(buildWelcomeCard());
        wrapper.add(Box.createVerticalStrut(Theme.GAP_LG));
 
        // ── Stat cards ──
        wrapper.add(buildStatCards());
        wrapper.add(Box.createVerticalStrut(Theme.GAP_LG));
 
        // ── Bottom row: Akses Cepat + Jadwal ──
        JPanel bottomRow = new JPanel(new GridLayout(1, 2, Theme.GAP_LG, 0));
        bottomRow.setOpaque(false);
        bottomRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        bottomRow.add(buildAksesCepat());
        bottomRow.add(buildJadwalCard());
        wrapper.add(bottomRow);
 
        JScrollPane scroll = new JScrollPane(wrapper);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }
 
    // ── Welcome card ────────────────────────────────────────────
    private JPanel buildWelcomeCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(18, 22, 18, 22)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
 
        // Left: text
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
 
        String nama  = Session.currentUser.getNamaLengkap();
        String nip   = guruProfil != null && !guruProfil.getNip().isEmpty() ? guruProfil.getNip() : "-";
        String mapel = guruProfil != null && !guruProfil.getMataPelajaran().isEmpty() ? guruProfil.getMataPelajaran() : "Guru";
 
        JLabel lblNama = new JLabel("Selamat Datang, " + nama + "! 👋");
        lblNama.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 20));
        lblNama.setForeground(Theme.TEXT_DARK);
 
        JLabel lblInfo = new JLabel("NIP: " + nip + "  |  " + mapel);
        lblInfo.setFont(Theme.FONT_REGULAR);
        lblInfo.setForeground(Theme.TEXT_MUTED);
 
        String today = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("id","ID")).format(new Date());
        JLabel lblDate = new JLabel("📅  " + today);
        lblDate.setFont(Theme.FONT_SMALL);
        lblDate.setForeground(Theme.TEXT_MUTED);
 
        left.add(lblNama);
        left.add(Box.createVerticalStrut(4));
        left.add(lblInfo);
        left.add(Box.createVerticalStrut(4));
        left.add(lblDate);
 
        // Right: semester badge
        JPanel semBadge = new JPanel(new GridBagLayout());
        semBadge.setBackground(Theme.PRIMARY_LIGHT);
        semBadge.setBorder(new CompoundBorder(
            new LineBorder(Theme.PRIMARY, 1, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        semBadge.setPreferredSize(new Dimension(160, 60));
        JPanel semText = new JPanel();
        semText.setOpaque(false);
        semText.setLayout(new BoxLayout(semText, BoxLayout.Y_AXIS));
        JLabel semLabel = new JLabel("Semester Genap", SwingConstants.CENTER);
        semLabel.setFont(Theme.FONT_SMALL); semLabel.setForeground(Theme.PRIMARY);
        JLabel semYear  = new JLabel("2025/2026", SwingConstants.CENTER);
        semYear.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 16)); semYear.setForeground(Theme.PRIMARY);
        semText.add(semLabel); semText.add(semYear);
        semBadge.add(semText);
 
        card.add(left, BorderLayout.CENTER);
        card.add(semBadge, BorderLayout.EAST);
        return card;
    }
 
    // ── Stat cards ───────────────────────────────────────────────
    private JPanel buildStatCards() {
        JPanel row = new JPanel(new GridLayout(1, 4, Theme.GAP_MD, 0));
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
 
        int userId     = Session.currentUser.getUserId();
        int totalKelas = guruDAO.countKelasByGuru(userId);
        int totalSiswa = guruDAO.countSiswaByGuru(userId);
        int jamMengajar = totalKelas * 4; // estimasi 4 jam/kelas
        int tugasDinilai = totalSiswa > 0 ? 85 : 0; // placeholder
 
        row.add(statCard("👥", String.valueOf(totalKelas),  "Kelas Diampu",   new Color(0x3B82F6), new Color(0xEFF6FF)));
        row.add(statCard("⏰", String.valueOf(jamMengajar), "Jam Mengajar",   new Color(0x10B981), new Color(0xECFDF5)));
        row.add(statCard("📝", tugasDinilai + "%",          "Tugas Dinilai",  new Color(0xF59E0B), new Color(0xFFFBEB)));
        row.add(statCard("📚", String.valueOf(totalSiswa),  "Total Siswa",    new Color(0x8B5CF6), new Color(0xF5F3FF)));
        return row;
    }
 
    private JPanel statCard(String icon, String value, String label, Color accent, Color bg) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(16, 16, 16, 16)
        ));
 
        JPanel iconWrap = new JPanel(new GridBagLayout());
        iconWrap.setBackground(bg);
        iconWrap.setPreferredSize(new Dimension(48, 48));
        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 22));
        iconWrap.add(iconLbl);
 
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(0, 14, 0, 0));
 
        JLabel valLbl = new JLabel(value);
        valLbl.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 26));
        valLbl.setForeground(Theme.TEXT_DARK);
        JLabel lblLbl = new JLabel(label);
        lblLbl.setFont(Theme.FONT_SMALL);
        lblLbl.setForeground(Theme.TEXT_MUTED);
        right.add(valLbl); right.add(lblLbl);
 
        card.add(iconWrap, BorderLayout.WEST);
        card.add(right, BorderLayout.CENTER);
        return card;
    }
 
    // ── Akses Cepat ──────────────────────────────────────────────
    private JPanel buildAksesCepat() {
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
        JLabel title = new JLabel("📋  Akses Cepat");
        title.setFont(Theme.FONT_BOLD);
        title.setForeground(Theme.TEXT_BODY);
        header.add(title);
        card.add(header, BorderLayout.NORTH);
 
        JPanel btns = new JPanel(new GridLayout(3, 1, 0, 10));
        btns.setBackground(Color.WHITE);
        btns.setBorder(new EmptyBorder(14, 14, 14, 14));
 
        btns.add(quickBtn("📋  Input Absensi Kelas",   new Color(0x3B82F6), e -> mainFrame.showPage("TRANS_ABSENSI")));
        btns.add(quickBtn("📝  Input Nilai Ulangan",    new Color(0x10B981), e -> mainFrame.showPage("TRANS_NILAI")));
        btns.add(quickBtn("📅  Lihat Seluruh Jadwal",   new Color(0xF59E0B), e -> mainFrame.showPage("JADWAL")));
 
        card.add(btns, BorderLayout.CENTER);
        return card;
    }
 
    private JButton quickBtn(String text, Color bg, java.awt.event.ActionListener al) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? bg.darker() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setFont(getFont()); g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), 16, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        btn.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 13));
        btn.setFocusPainted(false); btn.setBorderPainted(false); btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(al);
        return btn;
    }
 
    // ── Jadwal Mengajar Hari Ini ──────────────────────────────────
    private JPanel buildJadwalCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new LineBorder(Theme.BORDER, 1, true));
 
        // Header
        String today = new SimpleDateFormat("EEEE, dd MMM yyyy", new Locale("id","ID")).format(new Date());
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(10, 14, 10, 14)
        ));
        JLabel title = new JLabel("📅  Jadwal Mengajar Hari Ini");
        title.setFont(Theme.FONT_BOLD); title.setForeground(Theme.TEXT_BODY);
        JLabel dateLbl = new JLabel(today);
        dateLbl.setFont(Theme.FONT_SMALL); dateLbl.setForeground(Color.WHITE);
        dateLbl.setBackground(Theme.PRIMARY);
        dateLbl.setOpaque(true);
        dateLbl.setBorder(new EmptyBorder(3, 8, 3, 8));
        header.add(title, BorderLayout.WEST);
        header.add(dateLbl, BorderLayout.EAST);
        card.add(header, BorderLayout.NORTH);
 
        // Jadwal table from kelas yang dipegang guru
        String[] cols = {"No", "Kelas", "Tahun Ajaran", "Semester", "Total Siswa", "Status"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
 
        List<Object[]> jadwal = guruDAO.getJadwalByGuru(Session.currentUser.getUserId());
        String[] statusOpts = {"Selesai", "Sedang Berlangsung", "Belum Dimulai"};
        Color[] statusColors = {Theme.SUCCESS, Theme.PRIMARY, Theme.TEXT_MUTED};
 
        int no = 1;
        for (Object[] j : jadwal) {
            String status = no == 1 ? "Selesai" : no == 2 ? "Sedang Berlangsung" : "Belum Dimulai";
            tm.addRow(new Object[]{no++, j[1], j[2], "Sem. " + j[3], j[4] + " siswa", status});
        }
        if (jadwal.isEmpty()) {
            tm.addRow(new Object[]{"—", "Tidak ada jadwal", "—", "—", "—", "—"});
        }
 
        JTable tbl = new JTable(tm);
        tbl.setFont(Theme.FONT_REGULAR);
        tbl.setRowHeight(32);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(Theme.BORDER_LIGHT);
        tbl.getTableHeader().setFont(new Font(Theme.FONT_NAME, Font.BOLD, 11));
        tbl.getTableHeader().setBackground(new Color(0x1E293B));
        tbl.getTableHeader().setForeground(Color.WHITE);
        tbl.getTableHeader().setReorderingAllowed(false);
        tbl.getColumnModel().getColumn(0).setPreferredWidth(30);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(60);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(90);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(70);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(80);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(120);
 
        // Render status column dengan warna
        tbl.getColumnModel().getColumn(5).setCellRenderer((table, value, sel, foc, row, col) -> {
            JLabel l = new JLabel(value != null ? value.toString() : "");
            l.setFont(Theme.FONT_SMALL); l.setOpaque(true);
            l.setBorder(new EmptyBorder(0, 8, 0, 8));
            String v = value != null ? value.toString() : "";
            if (v.equals("Selesai")) { l.setBackground(Theme.SUCCESS_BG); l.setForeground(Theme.SUCCESS); }
            else if (v.equals("Sedang Berlangsung")) { l.setBackground(Theme.PRIMARY_LIGHT); l.setForeground(Theme.PRIMARY); }
            else { l.setBackground(new Color(0xF3F4F6)); l.setForeground(Theme.TEXT_MUTED); }
            return l;
        });
 
        JScrollPane sc = new JScrollPane(tbl); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);
        return card;
    }
}

