package ui.forms;

import dao.GuruDAO;
import dao.UserDAO;
import model.Guru;
import model.User;
import ui.components.StyledButton;
import ui.components.StyledTable;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MasterGuruPanel - Manajemen Data Guru (Admin Only)
 *
 * Menampilkan semua user dengan role='guru', plus data profil
 * (NIP, mata pelajaran diampu, no telp) dari tabel guru_profil.
 */
public class MasterGuruPanel extends BasePanel {

    private GuruDAO guruDAO = new GuruDAO();
    private UserDAO userDAO = new UserDAO();
    private StyledTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private List<Guru> currentData;

    private static final String[] COLUMNS = {
        "#", "Nama Lengkap", "NIP", "Mata Pelajaran Diampu", "Email", "No. Telp", "Status", "Aksi"
    };

    public MasterGuruPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);

        // ── Top wrapper ──
        JPanel top = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(buildPageHeader("Master Guru", "Kelola data guru beserta mata pelajaran yang diampu"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        // Toolbar
        txtSearch = buildSearchField("🔍  Cari nama atau NIP...");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String kw = txtSearch.getText().trim();
                if (!kw.isEmpty() && !kw.startsWith("🔍")) filterTable(kw);
                else fillTable(currentData);
            }
        });

        JComboBox<String> cmbStatus = buildCombo("Semua Status", "Aktif", "Non-Aktif");
        cmbStatus.addActionListener(e -> {
            if (currentData == null) return;
            int idx = cmbStatus.getSelectedIndex();
            if (idx == 0) { fillTable(currentData); return; }
            boolean aktif = idx == 1;
            java.util.List<Guru> filtered = new java.util.ArrayList<>();
            for (Guru g : currentData) if (g.isStatusAktif() == aktif) filtered.add(g);
            fillTable(filtered);
        });

        StyledButton btnTambah = new StyledButton("＋  Tambah Guru");
        btnTambah.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnTambah.addActionListener(e -> openForm(null));

        top.add(buildToolbar(new JComponent[]{txtSearch, cmbStatus}, new JComponent[]{btnTambah}));
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        // ── Table card ──
        JPanel card = buildCard();
        card.add(buildCardHeader("👨‍🏫  Daftar Guru"), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(140);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(180);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(90);
        table.getColumnModel().getColumn(7).setPreferredWidth(130);

        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0)
                    openForm(currentData.get(table.getSelectedRow()));
            }
        });

        // Right-click menu
        JPopupMenu popup = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("✏️  Edit Profil Guru");
        JMenuItem toggleItem = new JMenuItem("🔄  Toggle Aktif/Non-Aktif");
        editItem.addActionListener(e -> { if (table.getSelectedRow() >= 0) openForm(currentData.get(table.getSelectedRow())); });
        toggleItem.addActionListener(e -> {
            if (table.getSelectedRow() < 0) return;
            Guru g = currentData.get(table.getSelectedRow());
            boolean newStatus = !g.isStatusAktif();
            if (guruDAO.updateStatus(g.getGuruId(), newStatus)) { loadData(); }
            else showError("Gagal mengubah status guru.");
        });
        popup.add(editItem);
        popup.add(toggleItem);
        table.setComponentPopupMenu(popup);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(6, 16, 6, 16)));
        JLabel hint = new JLabel("💡  Klik dua kali baris untuk edit. Klik kanan untuk opsi lebih.");
        hint.setFont(Theme.FONT_SMALL); hint.setForeground(Theme.TEXT_MUTED);
        footer.add(hint);
        card.add(footer, BorderLayout.SOUTH);

        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void loadData() {
        currentData = guruDAO.findAll();
        fillTable(currentData);
    }

    private void fillTable(List<Guru> list) {
        tableModel.setRowCount(0);
        int i = 1;
        for (Guru g : list) {
            tableModel.addRow(new Object[]{
                i++,
                g.getNamaLengkap(),
                g.getNip().isEmpty() ? "-" : g.getNip(),
                g.getMataPelajaran().isEmpty() ? "-" : g.getMataPelajaran(),
                g.getEmail() != null ? g.getEmail() : "-",
                g.getNoTelp().isEmpty() ? "-" : g.getNoTelp(),
                g.isStatusAktif() ? "Aktif" : "Non-Aktif"
            });
        }
    }

    private void filterTable(String kw) {
        if (currentData == null) return;
        String k = kw.toLowerCase();
        java.util.List<Guru> filtered = new java.util.ArrayList<>();
        for (Guru g : currentData)
            if (g.getNamaLengkap().toLowerCase().contains(k) || g.getNip().contains(k))
                filtered.add(g);
        fillTable(filtered);
    }

    private void openForm(Guru existing) {
        boolean isEdit = existing != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Edit Profil Guru" : "Tambah Guru Baru", true);
        dlg.setSize(500, isEdit ? 400 : 480);
        dlg.setLocationRelativeTo(this);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        // Jika tambah baru: butuh username + password juga
        JTextField txtNama   = field(isEdit ? existing.getNamaLengkap() : "");
        JTextField txtEmail  = field(isEdit && existing.getEmail() != null ? existing.getEmail() : "");
        JTextField txtNip    = field(isEdit ? existing.getNip() : "");
        JTextField txtMapel  = field(isEdit ? existing.getMataPelajaran() : "");
        JTextField txtTelp   = field(isEdit ? existing.getNoTelp() : "");
        JPasswordField txtPwd = new JPasswordField();
        styleComp(txtPwd);
        JTextField txtUsername = field("");

        if (!isEdit) {
            addF(body, "Username *", txtUsername); body.add(Box.createVerticalStrut(8));
            addF(body, "Password *", txtPwd);      body.add(Box.createVerticalStrut(8));
        }
        addF(body, "Nama Lengkap *", txtNama);    body.add(Box.createVerticalStrut(8));
        addF(body, "Email",          txtEmail);   body.add(Box.createVerticalStrut(8));
        addF(body, "NIP",            txtNip);     body.add(Box.createVerticalStrut(8));
        addF(body, "Mata Pelajaran", txtMapel);   body.add(Box.createVerticalStrut(8));
        addF(body, "No. Telepon",    txtTelp);

        // Footer
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        foot.setBackground(new Color(0xF9FAFB));
        foot.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
        JButton btnC = new JButton("Batal"); btnC.addActionListener(e -> dlg.dispose());
        JButton btnS = new JButton(isEdit ? "Simpan Perubahan" : "Tambah Guru");
        btnS.setBackground(Theme.PRIMARY); btnS.setForeground(Color.WHITE); btnS.setFont(Theme.FONT_BOLD);
        btnS.addActionListener(e -> {
            if (txtNama.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Nama Lengkap wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (isEdit) {
                // Update profil saja
                existing.setNamaLengkap(txtNama.getText().trim());
                existing.setEmail(txtEmail.getText().trim());
                existing.setNip(txtNip.getText().trim());
                existing.setMataPelajaran(txtMapel.getText().trim());
                existing.setNoTelp(txtTelp.getText().trim());

                // Update nama di tabel users
                User u = new User();
                u.setUserId(existing.getGuruId());
                u.setNamaLengkap(existing.getNamaLengkap());
                u.setEmail(existing.getEmail());
                u.setUsername(""); // tidak diubah, set dummy
                u.setRole("guru");
                u.setStatusAktif(existing.isStatusAktif());

                boolean ok = guruDAO.saveProfil(existing);
                if (ok) { showSuccess("Profil guru berhasil disimpan."); dlg.dispose(); loadData(); }
                else      showError("Gagal menyimpan profil guru.");
            } else {
                // Buat user baru dulu, lalu insert profil
                if (txtUsername.getText().trim().isEmpty() || new String(txtPwd.getPassword()).isEmpty()) {
                    JOptionPane.showMessageDialog(dlg, "Username dan password wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                User u = new User();
                u.setUsername(txtUsername.getText().trim());
                u.setPassword(new String(txtPwd.getPassword()));
                u.setNamaLengkap(txtNama.getText().trim());
                u.setEmail(txtEmail.getText().trim());
                u.setRole("guru");
                u.setStatusAktif(true);
                boolean userOk = userDAO.insert(u);
                if (!userOk) { showError("Gagal membuat akun. Username sudah ada?"); return; }

                // Dapatkan user_id yang baru dibuat
                List<model.User> found = userDAO.search(txtUsername.getText().trim());
                if (!found.isEmpty()) {
                    Guru g = new Guru();
                    g.setGuruId(found.get(0).getUserId());
                    g.setNip(txtNip.getText().trim());
                    g.setMataPelajaran(txtMapel.getText().trim());
                    g.setNoTelp(txtTelp.getText().trim());
                    guruDAO.saveProfil(g);
                }
                showSuccess("Guru berhasil ditambahkan.");
                dlg.dispose(); loadData();
            }
        });
        foot.add(btnC); foot.add(btnS);
        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(body), BorderLayout.CENTER);
        dlg.add(foot, BorderLayout.SOUTH);
        dlg.getRootPane().setDefaultButton(btnS);
        dlg.setVisible(true);
    }

    private JTextField field(String val) {
        JTextField f = new JTextField(val);
        styleComp(f);
        return f;
    }

    private void styleComp(JComponent c) {
        c.setFont(Theme.FONT_REGULAR);
        c.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER, 1, true), new EmptyBorder(0, 8, 0, 8)));
    }

    private void addF(JPanel p, String lbl, JComponent c) {
        JLabel l = new JLabel(lbl); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_BODY); l.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setAlignmentX(Component.LEFT_ALIGNMENT); c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(c);
    }
}
