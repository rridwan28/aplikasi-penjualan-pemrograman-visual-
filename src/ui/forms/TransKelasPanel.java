package ui.forms;

import dao.KelasDAO;
import dao.UserDAO;
import model.Kelas;
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
 * TransKelasPanel - Transaksi Kelas
 */
public class TransKelasPanel extends BasePanel {

    private KelasDAO kelasDao = new KelasDAO();
    private UserDAO  userDao  = new UserDAO();
    private StyledTable table;
    private DefaultTableModel model;
    private List<Kelas> data;
    private static final String[] COLS = {"#","Nama Kelas","Wali Kelas","Tahun Ajaran","Semester","Jml Siswa"};

    public TransKelasPanel() { buildUI(); }

    private void buildUI() {
        JPanel main = new JPanel(new BorderLayout()); main.setOpaque(false);
        JPanel top  = new JPanel(); top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(buildPageHeader("Transaksi Kelas","Kelola pembagian siswa ke kelas dan penugasan guru"));
        top.add(Box.createVerticalStrut(Theme.GAP_LG));

        JComboBox<String> cmbTA = buildCombo("2025/2026","2024/2025");
        JComboBox<String> cmbSem = buildCombo("Semester 1","Semester 2");
        StyledButton btnAdd = new StyledButton("＋  Tambah Kelas");
        btnAdd.setPreferredSize(new Dimension(150,Theme.BTN_HEIGHT));
        btnAdd.addActionListener(e -> openForm(null));
        top.add(buildToolbar(new JComponent[]{new JLabel("T.A:"), cmbTA, new JLabel("  Sem:"), cmbSem}, new JComponent[]{btnAdd}));
        top.add(Box.createVerticalStrut(Theme.GAP_MD));

        JPanel card = buildCard();
        card.add(buildCardHeader("🏫  Daftar Kelas"), BorderLayout.NORTH);
        model = new DefaultTableModel(COLS,0) { @Override public boolean isCellEditable(int r,int c){return false;} };
        table = new StyledTable(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);
        table.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount()==2 && table.getSelectedRow()>=0) openForm(data.get(table.getSelectedRow()));
            }
        });
        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);
        main.add(top, BorderLayout.NORTH);
        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    public void loadData() { data = kelasDao.findAll(); fillTable(data); }

    private void fillTable(List<Kelas> list) {
        model.setRowCount(0);
        int i=1;
        for (Kelas k : list)
            model.addRow(new Object[]{i++, k.getNamaKelas(), k.getNamaWaliKelas()!=null?k.getNamaWaliKelas():"-", k.getTahunAjaran(), "Sem. "+k.getSemester(), "-"});
    }

    private void openForm(Kelas ex) {
        boolean isEdit = ex != null;
        JDialog dlg = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), isEdit?"Edit Kelas":"Tambah Kelas", true);
        dlg.setSize(420,320); dlg.setLocationRelativeTo(this);
        JPanel body = new JPanel(); body.setBackground(Color.WHITE);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBorder(new EmptyBorder(20,24,10,24));

        JTextField txtNamaKelas = new JTextField(isEdit?ex.getNamaKelas():"");
        txtNamaKelas.setFont(Theme.FONT_REGULAR);
        txtNamaKelas.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true),new EmptyBorder(0,8,0,8)));
        JTextField txtTA = new JTextField(isEdit?ex.getTahunAjaran():"2025/2026");
        txtTA.setFont(Theme.FONT_REGULAR);
        txtTA.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true),new EmptyBorder(0,8,0,8)));
        JComboBox<String> cmbSem = new JComboBox<>(new String[]{"1","2"});
        if (isEdit) cmbSem.setSelectedItem(ex.getSemester());

        List<User> guruList = userDao.findAllGuru();
        JComboBox<User> cmbGuru = new JComboBox<>();
        for (User u : guruList) cmbGuru.addItem(u);
        if (isEdit) { for (User u : guruList) { if (u.getUserId()==ex.getWaliKelasId()) { cmbGuru.setSelectedItem(u); break; } } }

        addF(body,"Nama Kelas (contoh: 7A) *", txtNamaKelas); body.add(Box.createVerticalStrut(8));
        addF(body,"Tahun Ajaran *", txtTA); body.add(Box.createVerticalStrut(8));
        addF(body,"Semester", cmbSem); body.add(Box.createVerticalStrut(8));
        addF(body,"Wali Kelas", cmbGuru);

        JPanel foot = new JPanel(new FlowLayout(FlowLayout.RIGHT,8,8));
        foot.setBackground(new Color(0xF9FAFB)); foot.setBorder(new MatteBorder(1,0,0,0,Theme.BORDER));
        JButton btnC = new JButton("Batal"); btnC.addActionListener(e->dlg.dispose());
        JButton btnS = new JButton("Simpan"); btnS.setBackground(Theme.PRIMARY); btnS.setForeground(Color.WHITE); btnS.setFont(Theme.FONT_BOLD);
        btnS.addActionListener(e -> {
            Kelas k = isEdit ? ex : new Kelas();
            k.setNamaKelas(txtNamaKelas.getText().trim());
            k.setTahunAjaran(txtTA.getText().trim());
            k.setSemester((String)cmbSem.getSelectedItem());
            k.setWaliKelasId(((User)cmbGuru.getSelectedItem()).getUserId());
            boolean ok = isEdit ? kelasDao.update(k) : kelasDao.insert(k);
            if (ok) { showSuccess("Kelas berhasil disimpan."); dlg.dispose(); loadData(); } else showError("Gagal menyimpan kelas.");
        });
        foot.add(btnC); foot.add(btnS);
        dlg.setLayout(new BorderLayout());
        dlg.add(body, BorderLayout.CENTER); dlg.add(foot, BorderLayout.SOUTH);
        dlg.setVisible(true);
    }
    private void addF(JPanel p, String lbl, JComponent c) {
        JLabel l = new JLabel(lbl); l.setFont(Theme.FONT_BOLD); l.setForeground(Theme.TEXT_BODY); l.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setAlignmentX(Component.LEFT_ALIGNMENT); c.setMaximumSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        p.add(l); p.add(Box.createVerticalStrut(3)); p.add(c);
    }
}
