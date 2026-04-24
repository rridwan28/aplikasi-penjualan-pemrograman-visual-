package ui.forms;

import dao.GuruDAO;
import dao.AbsensiDAO;
import dao.KelasDAO;
import model.Kelas;
import ui.components.StyledTable;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * JadwalMengajarPanel — Halaman Jadwal Mengajar untuk Guru
 *
 * Menampilkan semua kelas yang dipegang oleh guru yang sedang login,
 * beserta informasi semester dan jumlah siswa.
 */
public class JadwalMengajarPanel extends BasePanel {

    private GuruDAO  guruDAO  = new GuruDAO();
    private KelasDAO kelasDAO = new KelasDAO();

    private StyledTable table;
    private DefaultTableModel tableModel;

    private static final String[] COLS = {
        "No", "Nama Kelas", "Tahun Ajaran", "Semester", "Total Siswa", "Status Kelas"
    };

    public JadwalMengajarPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);

        JPanel top = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Jadwal Mengajar",
            "Daftar kelas yang Anda ampu pada tahun ajaran ini"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        // Info banner
        JPanel infoBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        infoBanner.setBackground(Theme.PRIMARY_LIGHT);
        infoBanner.setBorder(new CompoundBorder(
            new LineBorder(Theme.PRIMARY, 1, true),
            new EmptyBorder(0, 4, 0, 4)
        ));
        infoBanner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel infoIcon = new JLabel("ℹ️"); infoIcon.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 16));
        JLabel infoText = new JLabel("Jadwal di bawah adalah kelas yang Anda ampu. Klik dua kali kelas untuk melihat daftar siswa.");
        infoText.setFont(Theme.FONT_REGULAR); infoText.setForeground(Theme.PRIMARY);
        infoBanner.add(infoIcon); infoBanner.add(infoText);
        top.add(infoBanner);
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        // Table card
        JPanel card = buildCard();
        card.add(buildCardHeader("📅  Daftar Kelas Diampu"), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(140);

        // Double click → tampilkan detail siswa kelas
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    int row = table.getSelectedRow();
                    String namaKelas = tableModel.getValueAt(row, 1).toString();
                    showSiswaDialog(namaKelas, row);
                }
            }
        });

        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(8, 16, 8, 16)
        ));
        JLabel hint = new JLabel("💡  Klik dua kali untuk melihat daftar siswa di kelas tersebut.");
        hint.setFont(Theme.FONT_SMALL); hint.setForeground(Theme.TEXT_MUTED);
        footer.add(hint);
        card.add(footer, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void init() { loadData(); }

    private void loadData() {
        List<Object[]> jadwal = guruDAO.getJadwalByGuru(Session.currentUser.getUserId());
        tableModel.setRowCount(0);
        int no = 1;
        for (Object[] j : jadwal) {
            String status = no <= 1 ? "Aktif" : "Aktif";
            tableModel.addRow(new Object[]{
                no++,
                j[1],                          // nama_kelas
                j[2],                          // tahun_ajaran
                "Semester " + j[3],            // semester
                j[4] + " Siswa",               // total_siswa
                status
            });
        }
        if (jadwal.isEmpty()) {
            tableModel.addRow(new Object[]{"—", "Belum ada kelas yang ditugaskan", "—", "—", "—", "—"});
        }
    }

    private void showSiswaDialog(String namaKelas, int row) {
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Daftar Siswa — Kelas " + namaKelas, true);
        dlg.setSize(500, 400);
        dlg.setLocationRelativeTo(this);

        String[] cols = {"No", "NIS", "Nama Siswa", "Jenis Kelamin"};
        DefaultTableModel tm = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        // Ambil siswa berdasarkan kelas
        List<Kelas> kelasList = kelasDAO.findAll();
        for (Kelas k : kelasList) {
            if (k.getNamaKelas().equals(namaKelas)) {
                dao.SiswaDAO siswaDAO = new dao.SiswaDAO();
                List<model.Siswa> siswaList = siswaDAO.findByKelas(k.getKelasId());
                int i = 1;
                for (model.Siswa s : siswaList) {
                    tm.addRow(new Object[]{i++, s.getNis(), s.getNamaLengkap(), s.getJenisKelamin()});
                }
                break;
            }
        }

        JTable tbl = new JTable(tm);
        tbl.setFont(Theme.FONT_REGULAR);
        tbl.setRowHeight(32);
        tbl.setShowHorizontalLines(true);
        tbl.setShowVerticalLines(false);
        tbl.setGridColor(Theme.BORDER_LIGHT);
        tbl.getTableHeader().setFont(new Font(Theme.FONT_NAME, Font.BOLD, 11));
        tbl.getTableHeader().setBackground(Theme.BG_TABLE_HEADER);
        tbl.getTableHeader().setForeground(Theme.TEXT_MUTED);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(tbl), BorderLayout.CENTER);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        foot.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
        JButton btnClose = new JButton("Tutup");
        btnClose.addActionListener(e -> dlg.dispose());
        foot.add(btnClose);

        dlg.setLayout(new BorderLayout());
        dlg.add(panel, BorderLayout.CENTER);
        dlg.add(foot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
}
