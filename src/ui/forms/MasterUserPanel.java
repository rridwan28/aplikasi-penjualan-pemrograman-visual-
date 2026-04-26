package ui.forms;

import dao.UserDAO;
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

public class MasterUserPanel extends BasePanel {

    private UserDAO dao = new UserDAO();
    private StyledTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private List<User> currentData;

    private static final String[] COLUMNS = {"#","Nama Lengkap","Username","Email","Role","Status","Aksi"};

    public MasterUserPanel() {
        buildUI();
    }

    private void buildUI() {
        JPanel wrapper = new JPanel();
        wrapper.setOpaque(false);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        // Header        
        JPanel headerPanel = new JPanel((new BorderLayout()));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(new EmptyBorder(0, 0, Theme.GAP_LG, 0));
       
        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));        
        
        JLabel titleLabel = new JLabel("Master User");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);  
      
        JLabel subtitleLabel = new JLabel("Kelola akun admin dan guru yang dapat mengakses sistem");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        
        headerText.add(titleLabel);
        headerText.add(Box.createVerticalStrut(3));
        headerText.add(subtitleLabel);

        headerPanel.add(headerText, BorderLayout.WEST);

        wrapper.add(headerPanel);
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XS));        
        
        // Toolbar
        txtSearch = buildSearchField("🔍  Cari nama atau username...");
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { filterTable(txtSearch.getText()); }
        });

        JComboBox<String> cmbRole = buildCombo("Semua Role", "Admin", "Guru");
        cmbRole.addActionListener(e -> filterByRole(cmbRole.getSelectedIndex()));

        StyledButton btnTambah = new StyledButton("＋  Tambah User", StyledButton.PRIMARY);
        btnTambah.setPreferredSize(new Dimension(140, Theme.BTN_HEIGHT));
        btnTambah.addActionListener(e -> openForm(null));

        wrapper.add(buildToolbar(
            new JComponent[]{txtSearch, cmbRole},
            new JComponent[]{btnTambah}
        ));
        wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));

        // Table card
        JPanel card = buildCard();
        card.add(buildCardHeader("👤  Daftar User"), BorderLayout.NORTH);

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new StyledTable(tableModel);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(180);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(80);
        table.getColumnModel().getColumn(6).setPreferredWidth(150);

        // Double-click to edit
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && table.getSelectedRow() >= 0) {
                    int idx = table.getSelectedRow();
                    if (currentData != null && idx < currentData.size())
                        openForm(currentData.get(idx));
                }
            }
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        card.add(scroll, BorderLayout.CENTER);

        // Pagination info
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(6, 16, 6, 16)
        ));
        JLabel pageInfo = new JLabel("Klik dua kali baris untuk edit.");
        pageInfo.setFont(Theme.FONT_SMALL);
        pageInfo.setForeground(Theme.TEXT_MUTED);
        footer.add(pageInfo);
        
        card.add(footer, BorderLayout.SOUTH);
        
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        wrapper.add(card);
        wrapper.add(Box.createVerticalGlue());  
        
        JScrollPane outerScroll = new JScrollPane(wrapper);
        outerScroll.setBorder(BorderFactory.createEmptyBorder());
        outerScroll.setOpaque(false);
        outerScroll.getViewport().setOpaque(false);
        
        add(outerScroll, BorderLayout.CENTER);

        // untuk manggil main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(wrapper, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    public void loadData() {
        currentData = dao.findAll();
        fillTable(currentData);
    }

    private void fillTable(List<User> users) {
        tableModel.setRowCount(0);
        int i = 1;
        for (User u : users) {
            tableModel.addRow(new Object[]{
                i++,
                u.getNamaLengkap(),
                u.getUsername(),
                u.getEmail() != null ? u.getEmail() : "-",
                u.getRole().equals("admin") ? "Admin" : "Guru",
                u.isStatusAktif() ? "Aktif" : "Non-Aktif",
                "[Edit]  [Hapus]"
            });
        }
    }

    private void filterTable(String keyword) {
        if (currentData == null) return;
        if (keyword.isEmpty() || keyword.equals("🔍  Cari nama atau username...")) {
            fillTable(currentData);
            return;
        }
        String kw = keyword.toLowerCase();
        java.util.List<User> filtered = new java.util.ArrayList<>();
        for (User u : currentData)
            if (u.getNamaLengkap().toLowerCase().contains(kw) || u.getUsername().toLowerCase().contains(kw))
                filtered.add(u);
        fillTable(filtered);
    }

    private void filterByRole(int idx) {
        if (currentData == null) return;
        if (idx == 0) { fillTable(currentData); return; }
        String role = idx == 1 ? "admin" : "guru";
        java.util.List<User> filtered = new java.util.ArrayList<>();
        for (User u : currentData)
            if (u.getRole().equals(role)) filtered.add(u);
        fillTable(filtered);
    }

    private void openForm(User existing) {
        boolean isEdit = existing != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            isEdit ? "Edit User" : "Tambah User Baru", true);
        dlg.setSize(480, isEdit ? 400 : 440);
        dlg.setLocationRelativeTo(this);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        JTextField txtNama  = new JTextField(isEdit ? existing.getNamaLengkap() : "");
        JTextField txtUname = new JTextField(isEdit ? existing.getUsername() : "");
        JTextField txtEmail = new JTextField(isEdit && existing.getEmail() != null ? existing.getEmail() : "");
        JPasswordField txtPwd = new JPasswordField();
        JComboBox<String> cmbRole = new JComboBox<>(new String[]{"guru","admin"});
        if (isEdit) cmbRole.setSelectedItem(existing.getRole());
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"Aktif","Non-Aktif"});
        if (isEdit) cmbStatus.setSelectedIndex(existing.isStatusAktif() ? 0 : 1);

        addField(body, "Nama Lengkap *", txtNama);
        body.add(Box.createVerticalStrut(10));
        addField(body, "Username *", txtUname);
        body.add(Box.createVerticalStrut(10));
        addField(body, "Email", txtEmail);
        if (!isEdit) {
            body.add(Box.createVerticalStrut(10));
            addField(body, "Password *", txtPwd);
        }
        body.add(Box.createVerticalStrut(10));
        addField(body, "Role", cmbRole);
        body.add(Box.createVerticalStrut(10));
        addField(body, "Status", cmbStatus);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footer.setBackground(new Color(0xF9FAFB));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));

        JButton btnCancel = new JButton("Batal");
        btnCancel.addActionListener(e -> dlg.dispose());

        JButton btnSave = new JButton(isEdit ? "Simpan Perubahan" : "Simpan User");
        btnSave.setBackground(Theme.PRIMARY);
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(Theme.FONT_BOLD);
        btnSave.addActionListener(e -> {
            String nama  = txtNama.getText().trim();
            String uname = txtUname.getText().trim();
            if (nama.isEmpty() || uname.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Nama dan Username wajib diisi!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            User u = isEdit ? existing : new User();
            u.setNamaLengkap(nama);
            u.setUsername(uname);
            u.setEmail(txtEmail.getText().trim());
            u.setRole((String) cmbRole.getSelectedItem());
            u.setStatusAktif(cmbStatus.getSelectedIndex() == 0);
            if (!isEdit) u.setPassword(new String(txtPwd.getPassword()));

            boolean ok = isEdit ? dao.update(u) : dao.insert(u);
            if (ok) {
                showSuccess(isEdit ? "User berhasil diperbarui." : "User berhasil ditambahkan.");
                dlg.dispose();
                loadData();
            } else {
                showError("Gagal menyimpan data. Cek username sudah ada?");
            }
        });

        footer.add(btnCancel);
        footer.add(btnSave);

        dlg.setLayout(new BorderLayout());
        dlg.add(new JScrollPane(body), BorderLayout.CENTER);
        dlg.add(footer, BorderLayout.SOUTH);
        dlg.getRootPane().setDefaultButton(btnSave);
        dlg.setVisible(true);
    }

    private void addField(JPanel parent, String label, JComponent field) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_BODY);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        if (field instanceof JTextField || field instanceof JPasswordField)
            field.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(0,8,0,8)));
        field.setFont(Theme.FONT_REGULAR);

        parent.add(lbl);
        parent.add(Box.createVerticalStrut(4));
        parent.add(field);
    }
}
