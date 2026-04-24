package ui.forms;

import dao.MataPelajaranDAO;
import model.MataPelajaran;
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
 * MasterMapelPanel - Master Data Mata Pelajaran
 */
public class MasterMapelPanel extends BasePanel {

    private MataPelajaranDAO dao = new MataPelajaranDAO();
    private StyledTable table;
    private DefaultTableModel model;
    private List<MataPelajaran> data;
    private static final String[] COLS = {"#","Kode","Nama Mata Pelajaran","Kategori","KKM"};

    public MasterMapelPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);
        JPanel top  = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        top.add(buildPageHeader("Master Mata Pelajaran","Daftar semua mata pelajaran beserta nilai KKM"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        JTextField search = buildSearchField("🔍  Cari nama atau kode...");
        search.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) {
                String k = search.getText().toLowerCase();
                if (k.isEmpty() || k.startsWith("🔍")) { fillTable(data); return; }
                java.util.List<MataPelajaran> f = new java.util.ArrayList<>();
                for (MataPelajaran m : data)
                    if (m.getNamaMapel().toLowerCase().contains(k) || m.getKodeMapel().toLowerCase().contains(k)) f.add(m);
                fillTable(f);
            }
        });
        StyledButton btnAdd = new StyledButton("＋  Tambah Mapel");
        btnAdd.setPreferredSize(new Dimension(150, Theme.BTN_HEIGHT));
        btnAdd.addActionListener(e -> openForm(null));
        top.add(buildToolbar(new JComponent[]{search}, new JComponent[]{btnAdd}));
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        JPanel card = buildCard();
        card.add(buildCardHeader("📚  Daftar Mata Pelajaran"), BorderLayout.NORTH);

        model = new DefaultTableModel(COLS, 0) { @Override public boolean isCellEditable(int r,int c) { return false; } };
        table = new StyledTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        table.getColumnModel().getColumn(2).setPreferredWidth(220);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2 && table.getSelectedRow()>=0) openForm(data.get(table.getSelectedRow()));
            }
        });
        JPopupMenu pop = new JPopupMenu();
        JMenuItem edit = new JMenuItem("✏️ Edit");
        JMenuItem del  = new JMenuItem("🗑️ Hapus");
        edit.addActionListener(e -> { if (table.getSelectedRow()>=0) openForm(data.get(table.getSelectedRow())); });
        del.addActionListener(e -> {
            if (table.getSelectedRow()>=0 && confirmDelete()) { dao.delete(data.get(table.getSelectedRow()).getMapelId()); loadData(); }
        });
        pop.add(edit); pop.add(del); table.setComponentPopupMenu(pop);

        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void loadData() { data = dao.findAll(); fillTable(data); }

    private void fillTable(List<MataPelajaran> list) {
        model.setRowCount(0);
        int i=1;
        for (MataPelajaran m : list)
            model.addRow(new Object[]{i++, m.getKodeMapel(), m.getNamaMapel(), m.getKategori(), String.format("%.2f", m.getKkm())});
    }

    private void openForm(MataPelajaran ex) {
        boolean isEdit = ex != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isEdit?"Edit Mapel":"Tambah Mapel", true);
        dlg.setSize(400,300); dlg.setLocationRelativeTo(this);
        JPanel body = new JPanel(); body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20,24,10,24));

        JTextField txtKode = field(isEdit ? ex.getKodeMapel() : "");
        JTextField txtNama = field(isEdit ? ex.getNamaMapel() : "");
        JTextField txtKkm  = field(isEdit ? String.valueOf(ex.getKkm()) : "75.00");
        JTextField txtKat  = field(isEdit && ex.getKategori()!=null ? ex.getKategori() : "");

        addF(body,"Kode Mapel *", txtKode); body.add(Box.createVerticalStrut(8));
        addF(body,"Nama Mata Pelajaran *", txtNama); body.add(Box.createVerticalStrut(8));
        addF(body,"KKM (0-100)",  txtKkm); body.add(Box.createVerticalStrut(8));
        addF(body,"Kategori",     txtKat);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,8));
        foot.setBackground(new Color(0xF9FAFB));
        foot.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER));
        JButton btnC = new JButton("Batal"); btnC.addActionListener(e->dlg.dispose());
        JButton btnS = new JButton("Simpan"); btnS.setBackground(Theme.PRIMARY); btnS.setForeground(Color.WHITE); btnS.setFont(Theme.FONT_BOLD);
        btnS.addActionListener(e -> {
            if (txtKode.getText().trim().isEmpty()||txtNama.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(dlg,"Kode dan Nama wajib diisi!","Validasi",JOptionPane.WARNING_MESSAGE); return; }
            MataPelajaran m = isEdit ? ex : new MataPelajaran();
            m.setKodeMapel(txtKode.getText().trim()); m.setNamaMapel(txtNama.getText().trim());
            try { m.setKkm(Double.parseDouble(txtKkm.getText().trim())); } catch (Exception ignored) { m.setKkm(75); }
            m.setKategori(txtKat.getText().trim());
            boolean ok = isEdit ? dao.update(m) : dao.insert(m);
            if (ok) { showSuccess("Berhasil disimpan."); dlg.dispose(); loadData(); } else showError("Gagal. Cek kode sudah ada?");
        });
        foot.add(btnC); foot.add(btnS);
        dlg.setLayout(new BorderLayout());
        dlg.add(body, BorderLayout.CENTER); dlg.add(foot, BorderLayout.SOUTH);
        dlg.getRootPane().setDefaultButton(btnS);
        dlg.setVisible(true);
    }

    private JTextField field(String val) {
        JTextField f = new JTextField(val); f.setFont(Theme.FONT_REGULAR);
        f.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true), new EmptyBorder(0,8,0,8)));
        return f;
    }
    private void addF(JPanel p, String lbl, JComponent c) {
        JLabel l = new JLabel(lbl); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_BODY); l.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setAlignmentX(Component.LEFT_ALIGNMENT); c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(c);
    }
}
