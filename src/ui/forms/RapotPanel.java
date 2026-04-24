package ui.forms;

import dao.AbsensiDAO;
import dao.KelasDAO;
import dao.NilaiDAO;
import dao.SiswaDAO;
import model.Kelas;
import model.Siswa;
import ui.components.StyledTable;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * RapotPanel - Rapot Siswa
 */
public class RapotPanel extends BasePanel {

    private KelasDAO  kelasDao  = new KelasDAO();
    private SiswaDAO  siswaDao  = new SiswaDAO();
    private NilaiDAO  nilaiDao  = new NilaiDAO();
    private AbsensiDAO absDao   = new AbsensiDAO();

    private JComboBox<Kelas> cmbKelas;
    private JComboBox<Siswa> cmbSiswa;
    private JPanel previewPanel;

    public RapotPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);
        JPanel top  = new JPanel(); top.setOpaque(false); top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Rapot Siswa","Generate dan cetak rapot per semester"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        JPanel filterCard = buildCard(); filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JPanel fi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10)); fi.setBackground(Color.WHITE);
        cmbKelas = new JComboBox<>(); cmbKelas.setFont(Theme.FONT_REGULAR); cmbKelas.setPreferredSize(new Dimension(200,Theme.INPUT_HEIGHT));
        cmbSiswa = new JComboBox<>(); cmbSiswa.setFont(Theme.FONT_REGULAR); cmbSiswa.setPreferredSize(new Dimension(220,Theme.INPUT_HEIGHT));
        cmbKelas.addActionListener(e -> {
            Kelas k = (Kelas) cmbKelas.getSelectedItem();
            if (k != null) {
                cmbSiswa.removeAllItems();
                for (Siswa s : siswaDao.findByKelas(k.getKelasId())) cmbSiswa.addItem(s);
            }
        });
        JButton btnPreview = new JButton("👁 Preview Rapot");
        btnPreview.setBackground(Theme.PRIMARY); btnPreview.setForeground(Color.WHITE);
        btnPreview.setFont(Theme.FONT_BOLD); btnPreview.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnPreview.addActionListener(e -> generateRapot());
        fi.add(new JLabel("Kelas:")); fi.add(cmbKelas);
        fi.add(new JLabel("  Siswa:")); fi.add(cmbSiswa);
        fi.add(btnPreview);
        filterCard.add(fi, BorderLayout.CENTER);
        top.add(filterCard);
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        previewPanel = new JPanel(new BorderLayout()); previewPanel.setBackground(Color.WHITE);
        previewPanel.setBorder(new LineBorder(Theme.BORDER,1,true));
        JLabel placeholder = new JLabel("Pilih kelas dan siswa, lalu klik 'Preview Rapot'", SwingConstants.CENTER);
        placeholder.setFont(Theme.FONT_SUBTITLE); placeholder.setForeground(Theme.TEXT_MUTED);
        previewPanel.add(placeholder, BorderLayout.CENTER);

        JScrollPane sc = new JScrollPane(previewPanel); sc.setBorder(BorderFactory.createEmptyBorder());
        main.add(top, BorderLayout.NORTH); main.add(sc, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void init() {
        cmbKelas.removeAllItems();
        for (Kelas k : kelasDao.findAll()) cmbKelas.addItem(k);
    }

    private void generateRapot() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        Siswa siswa = (Siswa) cmbSiswa.getSelectedItem();
        if (kelas==null||siswa==null) { showError("Pilih kelas dan siswa."); return; }

        List<Object[]> nilaiList = nilaiDao.getRapotBySiswaKelas(siswa.getSiswaId(), kelas.getKelasId());
        List<Object[]> rekapAbs  = absDao.getRekapByKelas(kelas.getKelasId(), "2025-07-01", "2026-06-30");

        // Find absensi for this siswa
        int hadir=0, izin=0, sakit=0, alpha=0;
        for (Object[] r : rekapAbs)
            if (r[1].equals(siswa.getNis())) { hadir=(int)r[2]; izin=(int)r[3]; sakit=(int)r[4]; alpha=(int)r[5]; break; }

        previewPanel.removeAll();
        previewPanel.setLayout(new BorderLayout());
        previewPanel.add(buildRapotView(kelas, siswa, nilaiList, hadir, izin, sakit, alpha), BorderLayout.CENTER);
        previewPanel.revalidate();
        previewPanel.repaint();
    }

    private JPanel buildRapotView(Kelas kelas, Siswa siswa, List<Object[]> nilaiList,
                                   int hadir, int izin, int sakit, int alpha) {
        JPanel p = new JPanel(); p.setBackground(Color.WHITE);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(new EmptyBorder(24, 32, 24, 32));

        // ── Header ──
        JLabel sekolah = new JLabel("SMP NEGERI 1 JAKARTA", SwingConstants.CENTER);
        sekolah.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 18)); sekolah.setAlignmentX(0.5f);
        JLabel sub = new JLabel("RAPOR PESERTA DIDIK — Semester 1 — Tahun 2025/2026", SwingConstants.CENTER);
        sub.setFont(Theme.FONT_REGULAR); sub.setForeground(Theme.TEXT_MUTED); sub.setAlignmentX(0.5f);
        p.add(sekolah); p.add(Box.createVerticalStrut(2)); p.add(sub);
        p.add(Box.createVerticalStrut(4));
        JSeparator sep = new JSeparator(); sep.setAlignmentX(0.5f); sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        p.add(sep); p.add(Box.createVerticalStrut(12));

        // ── Identitas ──
        JPanel idGrid = new JPanel(new GridLayout(3,4,8,4)); idGrid.setOpaque(false); idGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        idGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        addIdRow(idGrid, "Nama Siswa", siswa.getNamaLengkap(), "Kelas", kelas.getNamaKelas());
        addIdRow(idGrid, "NIS", siswa.getNis(), "Wali Kelas", kelas.getNamaWaliKelas()!=null?kelas.getNamaWaliKelas():"-");
        addIdRow(idGrid, "Tahun Ajaran", kelas.getTahunAjaran(), "Semester", kelas.getSemester());
        p.add(idGrid); p.add(Box.createVerticalStrut(14));

        // ── Tabel Nilai ──
        String[] cols = {"No","Mata Pelajaran","Tugas","UTS","UAS","Nilai Akhir","KKM","Keterangan"};
        DefaultTableModel tm = new DefaultTableModel(cols,0);
        int i=1;
        double sum=0; int count=0;
        for (Object[] r : nilaiList) {
            double akhir = (double)r[5];
            tm.addRow(new Object[]{i++, r[0], String.format("%.1f",(double)r[2]), String.format("%.1f",(double)r[3]),
                String.format("%.1f",(double)r[4]), String.format("%.2f",akhir),
                String.format("%.2f",(double)r[1]), akhir>=(double)r[1]?"Tuntas":"Remedial"});
            sum+=akhir; count++;
        }
        StyledTable tbl = new StyledTable(tm);
        tbl.setPreferredScrollableViewportSize(new Dimension(700, nilaiList.size()*36+40));
        JScrollPane sc = new JScrollPane(tbl); sc.setBorder(new LineBorder(Theme.BORDER,1));
        sc.setAlignmentX(Component.LEFT_ALIGNMENT); sc.setMaximumSize(new Dimension(Integer.MAX_VALUE, nilaiList.size()*36+60));
        p.add(sc); p.add(Box.createVerticalStrut(12));

        // Rata-rata
        JLabel rataLbl = new JLabel(String.format("Rata-rata Semua Mata Pelajaran:  %.2f", count>0?sum/count:0));
        rataLbl.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 13));
        rataLbl.setForeground(Theme.PRIMARY);
        rataLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.add(rataLbl); p.add(Box.createVerticalStrut(14));

        // ── Kehadiran ──
        JPanel absPanel = new JPanel(new GridLayout(1,4,16,0)); absPanel.setOpaque(false);
        absPanel.setAlignmentX(Component.LEFT_ALIGNMENT); absPanel.setMaximumSize(new Dimension(500,50));
        absPanel.add(absBox("Hadir",  String.valueOf(hadir), Theme.SUCCESS));
        absPanel.add(absBox("Izin",   String.valueOf(izin),  Theme.INFO));
        absPanel.add(absBox("Sakit",  String.valueOf(sakit), Theme.WARNING));
        absPanel.add(absBox("Alpha",  String.valueOf(alpha), Theme.DANGER));
        p.add(new JLabel("Rekap Kehadiran:")); p.add(Box.createVerticalStrut(4)); p.add(absPanel);
        return p;
    }

    private void addIdRow(JPanel grid, String l1, String v1, String l2, String v2) {
        JLabel lbl1=new JLabel(l1+":"); lbl1.setFont(Theme.FONT_BOLD); lbl1.setForeground(Theme.TEXT_MUTED);
        JLabel val1=new JLabel(v1);     val1.setFont(Theme.FONT_BOLD);  val1.setForeground(Theme.TEXT_DARK);
        JLabel lbl2=new JLabel(l2+":"); lbl2.setFont(Theme.FONT_BOLD); lbl2.setForeground(Theme.TEXT_MUTED);
        JLabel val2=new JLabel(v2);     val2.setFont(Theme.FONT_BOLD);  val2.setForeground(Theme.TEXT_DARK);
        grid.add(lbl1); grid.add(val1); grid.add(lbl2); grid.add(val2);
    }

    private JPanel absBox(String label, String val, Color c) {
        JPanel p = new JPanel(new GridLayout(2,1)); p.setBackground(new Color(c.getRed(),c.getGreen(),c.getBlue(),30));
        p.setBorder(new CompoundBorder(new LineBorder(c,1,true),new EmptyBorder(4,8,4,8)));
        JLabel lv = new JLabel(val, SwingConstants.CENTER); lv.setFont(new Font(Theme.FONT_NAME,Font.BOLD,20)); lv.setForeground(c);
        JLabel ll = new JLabel(label, SwingConstants.CENTER); ll.setFont(Theme.FONT_SMALL); ll.setForeground(c);
        p.add(lv); p.add(ll); return p;
    }
}
