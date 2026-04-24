package ui.forms;

import dao.SiswaDAO;
import model.Siswa;
import ui.components.StyledButton;
import ui.components.StyledTable;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MasterSiswaPanel extends BasePanel {

    private SiswaDAO dao = new SiswaDAO();
    private StyledTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private List<Siswa> currentData;

    private static final String[] COLUMNS = {"#","NIS","Nama Lengkap","L/P","Tgl Lahir","Nama Ortu","No. Telp","Status"};

    public MasterSiswaPanel() {
        buildUI();
    }

    private void buildUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // ─ top wrapper ─
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        wrapper.add(buildPageHeader("Master Siswa", "Data seluruh siswa yang terdaftar di sekolah"));
        wrapper.add(Box.createVerticalStrut(Theme.GAP_LG));

        txtSearch = buildSearchField("🔍  Cari nama atau NIS...");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = txtSearch.getText().trim();
                if (!kw.isEmpty() && !kw.startsWith("🔍")) filterTable(kw);
                else fillTable(currentData);
            }
        });

        JComboBox<String> cmbStatus = buildCombo("Semua Status","Aktif","Non-Aktif");
        cmbStatus.addActionListener(e -> {
            if (currentData == null) return;
            if (cmbStatus.getSelectedIndex() == 0) { fillTable(currentData); return; }
            boolean aktif = cmbStatus.getSelectedIndex() == 1;
            java.util.List<Siswa> filtered = new java.util.ArrayList<>();
            for (Siswa s : currentData) if (s.isStatusAktif() == aktif) filtered.add(s);
            fillTable(filtered);
        });

        StyledButton btnTambah = new StyledButton("＋  Tambah Siswa");
        btnTambah.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnTambah.addActionListener(e -> openForm(null));

        wrapper.add(buildToolbar(
            new JComponent[]{txtSearch, cmbStatus},
            new JComponent[]{btnTambah}
        ));
        wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));

        // ─ table card ─
        JPanel card = buildCard();
        card.add(buildCardHeader("👨‍🎓  Daftar Siswa"), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(180);
        table.getColumnModel().getColumn(3).setPreferredWidth(50);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(150);
        table.getColumnModel().getColumn(6).setPreferredWidth(120);
        table.getColumnModel().getColumn(7).setPreferredWidth(80);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0)
                    openForm(currentData.get(table.getSelectedRow()));
            }
        });

        // Right-click menu
        JPopupMenu popup = new JPopupMenu();
        JMenuItem editItem   = new JMenuItem("✏️  Edit");
        JMenuItem deleteItem = new JMenuItem("🗑️  Hapus");
        editItem.addActionListener(e -> {
            if (table.getSelectedRow() >= 0) openForm(currentData.get(table.getSelectedRow()));
        });
        deleteItem.addActionListener(e -> {
            if (table.getSelectedRow() >= 0 && confirmDelete()) {
                dao.delete(currentData.get(table.getSelectedRow()).getSiswaId());
                loadData();
            }
        });
        popup.add(editItem);
        popup.add(deleteItem);
        table.setComponentPopupMenu(popup);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1,0,0,0,Theme.BORDER_LIGHT),
            new EmptyBorder(6,16,6,16)));
        JLabel hint = new JLabel("💡  Klik dua kali baris untuk edit. Klik kanan untuk opsi lebih.");
        hint.setFont(Theme.FONT_SMALL);
        hint.setForeground(Theme.TEXT_MUTED);
        footer.add(hint);
        card.add(footer, BorderLayout.SOUTH);

        mainPanel.add(wrapper, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

    public void loadData() {
        currentData = dao.findAll();
        fillTable(currentData);
    }

    private void fillTable(List<Siswa> list) {
        tableModel.setRowCount(0);
        int i = 1;
        for (Siswa s : list) {
            tableModel.addRow(new Object[]{
                i++,
                s.getNis(),
                s.getNamaLengkap(),
                s.getJenisKelamin(),
                s.getTanggalLahir() != null ? s.getTanggalLahir() : "-",
                s.getNamaOrtu()    != null ? s.getNamaOrtu()    : "-",
                s.getNoTelp()      != null ? s.getNoTelp()      : "-",
                s.isStatusAktif() ? "Aktif" : "Non-Aktif"
            });
        }
    }

    private void filterTable(String kw) {
        if (currentData == null) return;
        String k = kw.toLowerCase();
        java.util.List<Siswa> filtered = new java.util.ArrayList<>();
        for (Siswa s : currentData)
            if (s.getNamaLengkap().toLowerCase().contains(k) || s.getNis().contains(k))
                filtered.add(s);
        fillTable(filtered);
    }

    private void openForm(Siswa existing) {
        boolean isEdit = existing != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Edit Siswa" : "Tambah Siswa Baru", true);
        dlg.setSize(500, 500);
        dlg.setLocationRelativeTo(this);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        // Use GridLayout 2-column
        JPanel grid = new JPanel(new GridLayout(0, 2, Theme.GAP_MD, Theme.GAP_MD));
        grid.setOpaque(false);
        grid.setAlignmentX(Component.LEFT_ALIGNMENT);
        grid.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JTextField txtNis    = styled(new JTextField(isEdit ? existing.getNis() : ""));
        JTextField txtNama   = styled(new JTextField(isEdit ? existing.getNamaLengkap() : ""));
        JComboBox<String> cmbJK = new JComboBox<>(new String[]{"L","P"});
        if (isEdit) cmbJK.setSelectedItem(existing.getJenisKelamin());
        JTextField txtTgl    = styled(new JTextField(isEdit && existing.getTanggalLahir() != null ? existing.getTanggalLahir() : ""));
        JTextField txtOrtu   = styled(new JTextField(isEdit && existing.getNamaOrtu() != null ? existing.getNamaOrtu() : ""));
        JTextField txtTelp   = styled(new JTextField(isEdit && existing.getNoTelp() != null ? existing.getNoTelp() : ""));
        JTextArea  txtAlamat = new JTextArea(isEdit && existing.getAlamat() != null ? existing.getAlamat() : "");
        txtAlamat.setFont(Theme.FONT_REGULAR);
        txtAlamat.setRows(2);
        txtAlamat.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(4,8,4,8)));

        addLabelField(grid, "NIS *",             txtNis);
        addLabelField(grid, "Nama Lengkap *",    txtNama);
        addLabelField(grid, "Jenis Kelamin",     cmbJK);
        addLabelField(grid, "Tgl Lahir (YYYY-MM-DD)", txtTgl);
        addLabelField(grid, "Nama Orang Tua",    txtOrtu);
        addLabelField(grid, "No. Telepon",       txtTelp);

        JPanel alamatPanel = new JPanel(new BorderLayout());
        alamatPanel.setOpaque(false);
        JLabel lblAlamat = new JLabel("Alamat");
        lblAlamat.setFont(Theme.FONT_BOLD);
        alamatPanel.add(lblAlamat, BorderLayout.NORTH);
        alamatPanel.add(new JScrollPane(txtAlamat), BorderLayout.CENTER);

        body.add(grid);
        body.add(Box.createVerticalStrut(10));
        body.add(alamatPanel);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        footer.setBackground(new Color(0xF9FAFB));
        footer.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER));
        JButton btnCancel = new JButton("Batal");
        btnCancel.addActionListener(e -> dlg.dispose());
        JButton btnSave = new JButton(isEdit ? "Simpan" : "Tambah Siswa");
        btnSave.setBackground(Theme.PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(Theme.FONT_BOLD);
        btnSave.addActionListener(e -> {
            if (txtNis.getText().trim().isEmpty() || txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg,"NIS dan Nama wajib diisi!","Validasi",JOptionPane.WARNING_MESSAGE);
                return;
            }
            Siswa s = isEdit ? existing : new Siswa();
            s.setNis(txtNis.getText().trim());
            s.setNamaLengkap(txtNama.getText().trim());
            s.setJenisKelamin((String) cmbJK.getSelectedItem());
            s.setTanggalLahir(txtTgl.getText().trim());
            s.setNamaOrtu(txtOrtu.getText().trim());
            s.setNoTelp(txtTelp.getText().trim());
            s.setAlamat(txtAlamat.getText().trim());
            s.setStatusAktif(true);
            boolean ok = isEdit ? dao.update(s) : dao.insert(s);
            if (ok) { showSuccess("Data siswa berhasil disimpan."); dlg.dispose(); loadData(); }
            else      showError("Gagal menyimpan. Cek NIS sudah ada?");
        });
        footer.add(btnCancel);
        footer.add(btnSave);

        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(body), BorderLayout.CENTER);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.getRootPane().setDefaultButton(btnSave);
        dlg.setVisible(true);
    }

    private void addLabelField(JPanel grid, String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(0,4));
        p.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_BODY);
        field.setPreferredSize(new Dimension(200, Theme.INPUT_HEIGHT));
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        grid.add(p);
    }

    private JTextField styled(JTextField f) {
        f.setFont(Theme.FONT_REGULAR);
        f.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(0,8,0,8)));
        return f;
    }
}
