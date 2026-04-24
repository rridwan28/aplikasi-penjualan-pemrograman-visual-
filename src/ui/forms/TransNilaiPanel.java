package ui.forms;

import dao.KelasDAO;
import dao.MataPelajaranDAO;
import dao.NilaiDAO;
import model.Kelas;
import model.MataPelajaran;
import ui.components.StyledButton;
import ui.components.StyledTable;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * TRANSAKSI NILAI
 * Pilih Kelas + Mata Pelajaran → tabel input nilai Tugas/UTS/UAS per siswa → Simpan
 */
public class TransNilaiPanel extends BasePanel {

    private KelasDAO         kelasDao  = new KelasDAO();
    private MataPelajaranDAO mapelDao  = new MataPelajaranDAO();
    private NilaiDAO         nilaiDao  = new NilaiDAO();

    private JComboBox<Kelas>          cmbKelas;
    private JComboBox<MataPelajaran>  cmbMapel;
    private StyledTable  table;
    private DefaultTableModel tableModel;
    private List<Object[]> currentRows;
    private double currentKkm = 75;

    private JLabel lblRata, lblTuntas, lblRemedial;

    private static final String[] COLS = {"#","Nama Siswa","NIS","Tugas (30%)","UTS (30%)","UAS (40%)","Nilai Akhir","Ket."};

    public TransNilaiPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);

        JPanel top = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Transaksi Nilai","Input nilai tugas, UTS, dan UAS per mata pelajaran"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        // Filter
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setOpaque(false);
        cmbKelas = new JComboBox<>(); cmbKelas.setFont(Theme.FONT_REGULAR); cmbKelas.setPreferredSize(new Dimension(200, Theme.INPUT_HEIGHT));
        cmbMapel = new JComboBox<>(); cmbMapel.setFont(Theme.FONT_REGULAR); cmbMapel.setPreferredSize(new Dimension(200, Theme.INPUT_HEIGHT));
        StyledButton btnLoad = new StyledButton("🔍 Tampilkan", StyledButton.SECONDARY);
        btnLoad.setPreferredSize(new Dimension(130, Theme.BTN_HEIGHT));
        btnLoad.addActionListener(e -> loadNilai());
        filterBar.add(new JLabel("Kelas:")); filterBar.add(cmbKelas);
        filterBar.add(Box.createHorizontalStrut(8));
        filterBar.add(new JLabel("Mata Pelajaran:")); filterBar.add(cmbMapel);
        filterBar.add(Box.createHorizontalStrut(8));
        filterBar.add(btnLoad);
        top.add(filterBar);
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        // Card
        JPanel card = buildCard();
        StyledButton btnSimpan = new StyledButton("💾 Simpan Semua");
        btnSimpan.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnSimpan.addActionListener(e -> saveNilai());
        card.add(buildCardHeader("📝  Input Nilai", btnSimpan), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c >= 3 && c <= 5; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(80);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);
        table.getColumnModel().getColumn(6).setPreferredWidth(100);
        table.getColumnModel().getColumn(7).setPreferredWidth(100);

        // Recalculate on edit stop
        table.getModel().addTableModelListener(e -> {
            int col = e.getColumn();
            if (col >= 3 && col <= 5) recalcRow(e.getFirstRow());
        });

        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);

        // Footer summary
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        footer.setBackground(new Color(0xF9FAFB));
        footer.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER_LIGHT));
        lblRata     = footerLabel("Rata-rata kelas: -", Theme.PRIMARY);
        lblTuntas   = footerLabel("Tuntas: -", Theme.SUCCESS);
        lblRemedial = footerLabel("Remedial: -", Theme.DANGER);
        footer.add(lblRata); footer.add(lblTuntas); footer.add(lblRemedial);
        card.add(footer, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void init() {
        cmbKelas.removeAllItems();
        cmbMapel.removeAllItems();
        for (Kelas k : kelasDao.findAll())          cmbKelas.addItem(k);
        for (MataPelajaran m : mapelDao.findAll())   cmbMapel.addItem(m);
    }

    private void loadNilai() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        MataPelajaran mapel = (MataPelajaran) cmbMapel.getSelectedItem();
        if (kelas == null || mapel == null) { showError("Pilih kelas dan mata pelajaran."); return; }
        currentKkm = mapel.getKkm();
        currentRows = nilaiDao.getNilaiByKelasMapel(kelas.getKelasId(), mapel.getMapelId());
        tableModel.setRowCount(0);
        int i = 1;
        for (Object[] row : currentRows) {
            double t  = (double) row[3];
            double u  = (double) row[4];
            double ua = (double) row[5];
            double akhir = t*0.3 + u*0.3 + ua*0.4;
            tableModel.addRow(new Object[]{
                i++, row[1], row[2],
                String.format("%.1f", t),
                String.format("%.1f", u),
                String.format("%.1f", ua),
                String.format("%.2f", akhir),
                akhir >= currentKkm ? "Tuntas" : "Remedial"
            });
        }
        updateSummary();
    }

    private void recalcRow(int row) {
        try {
            double t  = Double.parseDouble(tableModel.getValueAt(row,3).toString());
            double u  = Double.parseDouble(tableModel.getValueAt(row,4).toString());
            double ua = Double.parseDouble(tableModel.getValueAt(row,5).toString());
            double akhir = t*0.3 + u*0.3 + ua*0.4;
            tableModel.setValueAt(String.format("%.2f", akhir), row, 6);
            tableModel.setValueAt(akhir >= currentKkm ? "Tuntas" : "Remedial", row, 7);
            updateSummary();
        } catch (NumberFormatException ignored) {}
    }

    private void updateSummary() {
        if (tableModel.getRowCount() == 0) return;
        double sum = 0; int tuntas = 0;
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            try {
                double v = Double.parseDouble(tableModel.getValueAt(r,6).toString());
                sum += v;
                if (v >= currentKkm) tuntas++;
            } catch (Exception ignored) {}
        }
        int total = tableModel.getRowCount();
        lblRata.setText(String.format("Rata-rata kelas: %.2f", total > 0 ? sum/total : 0));
        lblTuntas.setText("Tuntas: " + tuntas);
        lblRemedial.setText("Remedial: " + (total - tuntas));
    }

    private void saveNilai() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        MataPelajaran mapel = (MataPelajaran) cmbMapel.getSelectedItem();
        if (kelas == null || mapel == null || currentRows == null) { showError("Tampilkan data dulu sebelum menyimpan."); return; }
        if (table.isEditing()) table.getCellEditor().stopCellEditing();

        List<Object[]> toSave = new java.util.ArrayList<>();
        for (int r = 0; r < tableModel.getRowCount(); r++) {
            int siswaId = (int) currentRows.get(r)[0];
            try {
                double t  = Double.parseDouble(tableModel.getValueAt(r,3).toString());
                double u  = Double.parseDouble(tableModel.getValueAt(r,4).toString());
                double ua = Double.parseDouble(tableModel.getValueAt(r,5).toString());
                toSave.add(new Object[]{siswaId, t, u, ua, 0.0});
            } catch (NumberFormatException e) {
                showError("Nilai pada baris " + (r+1) + " tidak valid. Gunakan angka desimal.");
                return;
            }
        }
        boolean ok = nilaiDao.saveNilaiBatch(kelas.getKelasId(), mapel.getMapelId(),
            Session.currentUser.getUserId(), toSave);
        if (ok) showSuccess("Nilai berhasil disimpan!"); else showError("Gagal menyimpan nilai.");
    }

    private JLabel footerLabel(String text, Color c) {
        JLabel l = new JLabel(text);
        l.setFont(Theme.FONT_BOLD);
        l.setForeground(c);
        return l;
    }
}
