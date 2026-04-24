package ui.forms;

import dao.AbsensiDAO;
import dao.KelasDAO;
import model.Kelas;
import ui.components.StyledButton;
import ui.components.StyledTable;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * LapAbsensiPanel - Laporan Absensi Siswa
 */
public class LapAbsensiPanel extends BasePanel {

    private KelasDAO   kelasDao  = new KelasDAO();
    private AbsensiDAO absDao    = new AbsensiDAO();
    private JComboBox<Kelas> cmbKelas;
    private JTextField txtDari, txtSampai;
    private StyledTable table;
    private DefaultTableModel model;
    private static final String[] COLS = {"#","Nama Siswa","NIS","Hadir","Izin","Sakit","Alpha","Total Hari","% Hadir","Status"};

    public LapAbsensiPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);
        JPanel top  = new JPanel(); top.setOpaque(false); top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Laporan Absensi Siswa","Rekap kehadiran siswa per periode"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        // Filter card
        JPanel filterCard = buildCard();
        filterCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        JPanel filterInner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        filterInner.setBackground(Color.WHITE);
        cmbKelas = new JComboBox<>(); cmbKelas.setFont(Theme.FONT_REGULAR); cmbKelas.setPreferredSize(new Dimension(200, Theme.INPUT_HEIGHT));
        txtDari   = dateField("2026-01-01");
        txtSampai = dateField(java.time.LocalDate.now().toString());
        StyledButton btnTampil = new StyledButton("🔍 Tampilkan", StyledButton.SECONDARY);
        btnTampil.setPreferredSize(new Dimension(120, Theme.BTN_HEIGHT));
        btnTampil.addActionListener(e -> loadData());
        StyledButton btnCetak = new StyledButton("📄 Cetak PDF");
        btnCetak.setPreferredSize(new Dimension(120, Theme.BTN_HEIGHT));
        btnCetak.addActionListener(e -> showSuccess("Fitur cetak PDF memerlukan library iText.\nSilakan tambahkan itext-7 ke project."));
        filterInner.add(new JLabel("Kelas:")); filterInner.add(cmbKelas);
        filterInner.add(new JLabel("  Dari:")); filterInner.add(txtDari);
        filterInner.add(new JLabel("  s/d:")); filterInner.add(txtSampai);
        filterInner.add(btnTampil); filterInner.add(btnCetak);
        filterCard.add(filterInner, BorderLayout.CENTER);
        top.add(filterCard);
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        JPanel card = buildCard();
        card.add(buildCardHeader("📊  Rekap Kehadiran"), BorderLayout.NORTH);
        model = new DefaultTableModel(COLS, 0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        table = new StyledTable(model);
        int[] widths = {40,180,80,60,60,60,60,80,80,100};
        for (int i=0;i<widths.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH); main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void init() {
        cmbKelas.removeAllItems();
        for (Kelas k : kelasDao.findAll()) cmbKelas.addItem(k);
    }

    private void loadData() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        if (kelas == null) { showError("Pilih kelas."); return; }
        List<Object[]> rows = absDao.getRekapByKelas(kelas.getKelasId(), txtDari.getText(), txtSampai.getText());
        model.setRowCount(0);
        int i=1;
        for (Object[] r : rows) {
            int hadir = (int)r[2], izin=(int)r[3], sakit=(int)r[4], alpha=(int)r[5], total=(int)r[6];
            double pct = total > 0 ? (hadir*100.0/total) : 0;
            String status = pct>=90?"Baik":pct>=80?"Perlu Perhatian":"Kritis";
            model.addRow(new Object[]{i++, r[0], r[1], hadir, izin, sakit, alpha, total, String.format("%.1f%%", pct), status});
        }
    }

    private JTextField dateField(String val) {
        JTextField f = new JTextField(val, 11); f.setFont(Theme.FONT_REGULAR);
        f.setPreferredSize(new Dimension(120, Theme.INPUT_HEIGHT));
        f.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(0,8,0,8)));
        return f;
    }
}
