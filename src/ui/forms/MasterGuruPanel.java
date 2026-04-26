package ui.forms;

import dao.GuruDAO;
import dao.MataPelajaranDAO;
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
import model.MataPelajaran;

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
        
        JLabel titleLabel = new JLabel("Master Guru");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);  
      
        JLabel subtitleLabel = new JLabel("Kelola data guru beserta mata pelajaran yang diampu");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        
        headerText.add(titleLabel);
        headerText.add(Box.createVerticalStrut(3));
        headerText.add(subtitleLabel);

        headerPanel.add(headerText, BorderLayout.WEST);

        wrapper.add(headerPanel);
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XS));    
        
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

        wrapper.add(buildToolbar(new JComponent[]{txtSearch, cmbStatus}, new JComponent[]{btnTambah}));
        wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));

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
            if (guruDAO.update(g)) { loadData(); }
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

        //untuk manggil semua main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(wrapper, BorderLayout.NORTH);
        mainPanel.add(card, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
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
        dlg.setSize(500, isEdit ? 400 : 380);
        dlg.setLocationRelativeTo(this);

        JPanel body = new JPanel();
        body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20, 24, 10, 24));

        // Dropdown nama guru (dari users yang role='guru')
        JComboBox<User> cmbNama = new JComboBox<>();
        cmbNama.setFont(Theme.FONT_REGULAR);
        cmbNama.setEditable(false); // PENTING: jangan biarkan editable
        cmbNama.setPreferredSize(new Dimension(400, Theme.INPUT_HEIGHT));      

        List<User> guruUsers = userDAO.findAllGuru();
        List<Guru> guruProfilList = guruDAO.findAll();

        // Filter: ambil user yang BELUM punya profil di tabel guru
        for (User u : guruUsers) {
            boolean sudahAda = false;
            for (Guru g : guruProfilList) {
                if (g.getNamaLengkap().equals(u.getNamaLengkap())) {
                    sudahAda = true;
                    break;
                }
            }

            // Hanya tambahkan user yang belum punya profil (atau jika sedang edit user itu)
            if (!sudahAda || (isEdit && existing != null && existing.getNamaLengkap().equals(u.getNamaLengkap()))) {
                cmbNama.addItem(u);
                System.out.println("  Added to dropdown: " + u.getNamaLengkap());
            }
        }
        if (isEdit && cmbNama.getItemCount() > 0 && existing != null) {
            // Set dropdown ke guru yang sedang di-edit
            for (int i = 0; i < cmbNama.getItemCount(); i++) {
                User u = (User) cmbNama.getItemAt(i);
                if (u.getNamaLengkap().equals(existing.getNamaLengkap())) {
                    cmbNama.setSelectedIndex(i);
                    break;
                }
            }
        }

        JTextField txtNip   = field(isEdit && existing != null ? existing.getNip() : "");
        
        JComboBox<MataPelajaran> cmbMapel = new JComboBox<>();
        cmbMapel.setFont(Theme.FONT_REGULAR);
        cmbMapel.setEditable(false);
        cmbMapel.setPreferredSize(new Dimension(400, Theme.INPUT_HEIGHT));
        
        MataPelajaranDAO mapelDAO = new MataPelajaranDAO();
        List<MataPelajaran> mapelList = mapelDAO.findAll();
        for (MataPelajaran m : mapelList) {
            cmbMapel.addItem(m);
        }
        
        if (isEdit && existing != null && !existing.getMataPelajaran().isEmpty()) {
            for (MataPelajaran m : mapelList) {
                if (m.getNamaMapel().equals(existing.getMataPelajaran())) {
                    cmbMapel.setSelectedItem(m);
                    break;
                }
            }
        }        

        
        JTextField txtTelp  = field(isEdit && existing != null ? existing.getNoTelp() : "");

        addF(body, "Nama Guru *", cmbNama);      
        body.add(Box.createVerticalStrut(8));
        addF(body, "NIP *", txtNip);             
        body.add(Box.createVerticalStrut(8));
        addF(body, "Mata Pelajaran", cmbMapel);  
        body.add(Box.createVerticalStrut(8));
        addF(body, "No. Telepon", txtTelp);

        // Footer
        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        foot.setBackground(new Color(0xF9FAFB));
        foot.setBorder(new MatteBorder(1, 0, 0, 0, Theme.BORDER));
        JButton btnC = new JButton("Batal"); 
        btnC.addActionListener(e -> dlg.dispose());

        JButton btnS = new JButton(isEdit ? "Simpan Perubahan" : "Tambah Guru");      
        btnS.setBackground(Theme.PRIMARY); 
        btnS.setForeground(Color.WHITE); 
        btnS.setFont(Theme.FONT_BOLD);

        btnS.addActionListener(e -> {
            User selectedUser = (User) cmbNama.getSelectedItem();
            MataPelajaran selectedMapel = (MataPelajaran) cmbMapel.getSelectedItem();
            String nip = txtNip.getText().trim();

            if (selectedUser == null || nip.isEmpty()) {
                JOptionPane.showMessageDialog(dlg, "Pilih Nama Guru dan isi NIP!", "Validasi", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (isEdit && existing != null) {
                // Update profil guru
                existing.setNamaLengkap(selectedUser.getNamaLengkap());
                existing.setEmail(selectedUser.getEmail());
                existing.setNip(nip);
                existing.setMataPelajaran(selectedMapel != null ? selectedMapel.getNamaMapel() : "");
                existing.setNoTelp(txtTelp.getText().trim());

                if (guruDAO.update(existing)) {
                    showSuccess("Profil guru berhasil disimpan.");
                    dlg.dispose();
                    loadData();
                } else {
                    showError("Gagal menyimpan profil guru.");
                }
            } else {
                // Insert guru baru
                Guru g = new Guru();
                g.setNamaLengkap(selectedUser.getNamaLengkap());
                g.setEmail(selectedUser.getEmail());
                g.setNip(nip);
                g.setMataPelajaran(selectedMapel != null ? selectedMapel.getNamaMapel() : "");
                g.setNoTelp(txtTelp.getText().trim());
                g.setStatusAktif(true);

                if (guruDAO.insert(g)) {
                    showSuccess("Guru berhasil ditambahkan.");
                    dlg.dispose();
                    loadData();
                } else {
                    showError("Gagal menambahkan guru. NIP sudah ada?");
                }
            }
        });

        foot.add(btnC); 
        foot.add(btnS);
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
        JLabel l = new JLabel(lbl); 
        l.setFont(Theme.FONT_BOLD); 
        l.setForeground(Theme.TEXT_BODY); 
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));  // ← tambah ini kembali
        
        p.add(l); 
        p.add(Box.createVerticalStrut(3)); 
        p.add(c);
    }
}
