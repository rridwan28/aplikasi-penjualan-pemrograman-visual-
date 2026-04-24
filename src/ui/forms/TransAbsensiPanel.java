package ui.forms;

import dao.AbsensiDAO;
import dao.KelasDAO;
import dao.MataPelajaranDAO;
import model.Kelas;
import ui.components.StyledButton;
import ui.components.StyledTable;
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
 * TRANSAKSI ABSENSI
 * Pilih kelas + tanggal → tampilkan tabel siswa → klik status H/I/S/A → Simpan
 */
public class TransAbsensiPanel extends BasePanel {

    private KelasDAO    kelasDao   = new KelasDAO();
    private AbsensiDAO  absensiDao = new AbsensiDAO();

    private JComboBox<Kelas> cmbKelas;
    private JTextField       txtTanggal;
    private StyledTable      table;
    private DefaultTableModel tableModel;

    // Hold current absensi data: { siswa_id, nama, nis, status, keterangan }
    private List<Object[]> currentRows = new ArrayList<>();

    // Status buttons per row — map rowIndex -> current status
    private Map<Integer, String[]> statusMap = new HashMap<>(); // rowIndex -> [status]

    private JLabel lblHadir, lblIzin, lblSakit, lblAlpha;

    private static final String[] COLS = {"#","Nama Siswa","NIS","Status","Keterangan"};
    private static final String[] STATUS_LIST = {"H","I","S","A"};
    private static final String[] STATUS_LABEL = {"Hadir","Izin","Sakit","Alpha"};

    public TransAbsensiPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);

        // ── Top section ──
        JPanel top = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Transaksi Absensi","Input kehadiran siswa per hari"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        // Filter bar
        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filterBar.setOpaque(false);
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        cmbKelas = new JComboBox<>();
        cmbKelas.setFont(Theme.FONT_REGULAR);
        cmbKelas.setPreferredSize(new Dimension(220, Theme.INPUT_HEIGHT));

        txtTanggal = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()), 12);
        txtTanggal.setFont(Theme.FONT_REGULAR);
        txtTanggal.setPreferredSize(new Dimension(130, Theme.INPUT_HEIGHT));
        txtTanggal.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(0,8,0,8)));

        StyledButton btnLoad = new StyledButton("🔍 Tampilkan", StyledButton.SECONDARY);
        btnLoad.setPreferredSize(new Dimension(130, Theme.BTN_HEIGHT));
        btnLoad.addActionListener(e -> loadAbsensi());

        StyledButton btnAllHadir = new StyledButton("✅ Hadir Semua", StyledButton.SUCCESS);
        btnAllHadir.setPreferredSize(new Dimension(130, Theme.BTN_HEIGHT));
        btnAllHadir.addActionListener(e -> setAllStatus("H"));

        filterBar.add(new JLabel("Kelas: ")); filterBar.add(cmbKelas);
        filterBar.add(Box.createHorizontalStrut(8));
        filterBar.add(new JLabel("Tanggal: ")); filterBar.add(txtTanggal);
        filterBar.add(Box.createHorizontalStrut(8));
        filterBar.add(btnLoad);
        filterBar.add(Box.createHorizontalStrut(4));
        filterBar.add(btnAllHadir);

        top.add(filterBar);
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        // ── Table card ──
        JPanel card = buildCard();
        StyledButton btnSimpan = new StyledButton("💾 Simpan Absensi");
        btnSimpan.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnSimpan.addActionListener(e -> saveAbsensi());
        card.add(buildCardHeader("📋  Daftar Kehadiran", btnSimpan), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 4; } // only keterangan editable
            @Override public Class<?> getColumnClass(int c) { return c==3 ? JPanel.class : Object.class; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(90);
        table.getColumnModel().getColumn(3).setPreferredWidth(280);
        table.getColumnModel().getColumn(4).setPreferredWidth(200);

        // Custom renderer + editor for status column
        table.getColumnModel().getColumn(3).setCellRenderer((tbl, val, sel, foc, row, col) -> {
            return buildStatusPanel(row);
        });
        table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            @Override public Component getTableCellEditorComponent(JTable t, Object v, boolean sel, int row, int col) {
                return buildStatusPanel(row);
            }
        });
        table.setRowHeight(42);

        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);

        // Footer: counters
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        footer.setBackground(new Color(0xF9FAFB));
        footer.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER_LIGHT));
        lblHadir = counter("✅ Hadir:", "0", Theme.SUCCESS);
        lblIzin  = counter("📋 Izin:", "0", Theme.INFO);
        lblSakit = counter("🤒 Sakit:", "0", Theme.WARNING);
        lblAlpha = counter("❌ Alpha:", "0", Theme.DANGER);
        footer.add(lblHadir); footer.add(lblIzin); footer.add(lblSakit); footer.add(lblAlpha);
        card.add(footer, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void init() {
        // Reload kelas list
        cmbKelas.removeAllItems();
        List<Kelas> kelasList = kelasDao.findAll();
        for (Kelas k : kelasList) cmbKelas.addItem(k);
    }

    private void loadAbsensi() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        String tanggal = txtTanggal.getText().trim();
        if (kelas == null || tanggal.isEmpty()) { showError("Pilih kelas dan isi tanggal."); return; }

        currentRows = absensiDao.getAbsensiByKelasAndDate(kelas.getKelasId(), tanggal);
        statusMap.clear();
        tableModel.setRowCount(0);

        int i = 1;
        for (Object[] row : currentRows) {
            String status = (String) row[3];
            String ket    = (String) row[4];
            statusMap.put(i - 1, new String[]{status});
            tableModel.addRow(new Object[]{i++, row[1], row[2], status, ket});
        }
        updateCounters();
    }

    private JPanel buildStatusPanel(int row) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        p.setBackground(Color.WHITE);
        String[] stArr = statusMap.getOrDefault(row, new String[]{"H"});
        String current = stArr[0];

        for (int i = 0; i < STATUS_LIST.length; i++) {
            String st = STATUS_LIST[i];
            String lb = STATUS_LABEL[i];
            boolean active = st.equals(current);

            JButton btn = new JButton(lb);
            btn.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 11));
            btn.setFocusPainted(false);
            btn.setBorderPainted(true);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(58, 26));

            Color[] colors = {Theme.SUCCESS, Theme.INFO, Theme.WARNING, Theme.DANGER};
            Color[] bgs    = {Theme.SUCCESS_BG, Theme.INFO_BG, Theme.WARNING_BG, Theme.DANGER_BG};
            if (active) {
                btn.setBackground(bgs[i]);
                btn.setForeground(colors[i]);
                btn.setBorder(new LineBorder(colors[i], 2, true));
            } else {
                btn.setBackground(new Color(0xF9FAFB));
                btn.setForeground(Theme.TEXT_MUTED);
                btn.setBorder(new LineBorder(Theme.BORDER, 1, true));
            }
            final int finalI = i;
            btn.addActionListener(e -> {
                stArr[0] = STATUS_LIST[finalI];
                statusMap.put(row, stArr);
                table.repaint();
                updateCounters();
            });
            p.add(btn);
        }
        return p;
    }

    private void setAllStatus(String status) {
        for (Map.Entry<Integer, String[]> entry : statusMap.entrySet())
            entry.getValue()[0] = status;
        table.repaint();
        updateCounters();
    }

    private void updateCounters() {
        int h=0,i=0,s=0,a=0;
        for (String[] st : statusMap.values()) {
            switch (st[0]) {
                case "H": h++; break;
                case "I": i++; break;
                case "S": s++; break;
                case "A": a++; break;
            }
        }
        lblHadir.setText("✅ Hadir: " + h);
        lblIzin.setText("📋 Izin: " + i);
        lblSakit.setText("🤒 Sakit: " + s);
        lblAlpha.setText("❌ Alpha: " + a);
    }

    private void saveAbsensi() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem();
        String tanggal = txtTanggal.getText().trim();
        if (kelas == null || currentRows.isEmpty()) { showError("Tidak ada data absensi untuk disimpan."); return; }

        // Stop editing
        if (table.isEditing()) table.getCellEditor().stopCellEditing();

        List<Object[]> toSave = new ArrayList<>();
        for (int i = 0; i < currentRows.size(); i++) {
            Object[] row = currentRows.get(i);
            String status = statusMap.getOrDefault(i, new String[]{"H"})[0];
            String ket    = (String) tableModel.getValueAt(i, 4);
            toSave.add(new Object[]{row[0], status, ket != null ? ket : ""});
        }

        boolean ok = absensiDao.saveAbsensiBatch(kelas.getKelasId(), tanggal,
            Session.currentUser.getUserId(), toSave);
        if (ok) showSuccess("Absensi berhasil disimpan untuk " + tanggal + ".");
        else     showError("Gagal menyimpan absensi.");
    }

    private JLabel counter(String prefix, String val, Color c) {
        JLabel l = new JLabel(prefix + " " + val);
        l.setFont(Theme.FONT_BOLD);
        l.setForeground(c);
        return l;
    }
}
