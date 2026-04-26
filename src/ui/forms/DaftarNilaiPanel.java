package ui.forms;

import dao.KelasDAO;
import dao.NilaiDAO;
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
 * DaftarNilaiPanel - Daftar Nilai (Ranking Siswa)
 */
public class DaftarNilaiPanel extends BasePanel {

    private KelasDAO kelasDao = new KelasDAO();
    private NilaiDAO nilaiDao = new NilaiDAO();
    private JComboBox<Kelas> cmbKelas;
    private StyledTable table;
    private DefaultTableModel model;
    private static final String[] COLS={"Ranking","Nama Siswa","NIS","Rata-rata Nilai","Predikat"};

    public DaftarNilaiPanel() { buildUI(); }

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
        
        JLabel titleLabel = new JLabel("Daftar Nilai");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);  
      
        JLabel subtitleLabel = new JLabel("Rekap nilai rata-rata seluruh siswa per kelas");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        
        headerText.add(titleLabel);
        headerText.add(Box.createVerticalStrut(3));
        headerText.add(subtitleLabel);

        headerPanel.add(headerText, BorderLayout.WEST);

        wrapper.add(headerPanel);
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XS));         

        JPanel fc = buildCard(); fc.setMaximumSize(new Dimension(Integer.MAX_VALUE,80));
        JPanel fi = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); fi.setBackground(Color.WHITE);
        cmbKelas = new JComboBox<>(); cmbKelas.setFont(Theme.FONT_REGULAR); cmbKelas.setPreferredSize(new Dimension(200,Theme.INPUT_HEIGHT));
        StyledButton btnT = new StyledButton("🔍 Tampilkan",StyledButton.SECONDARY); btnT.setPreferredSize(new Dimension(120,Theme.BTN_HEIGHT)); btnT.addActionListener(e->loadData());
        StyledButton btnExp = new StyledButton("📄 Export");  btnExp.setPreferredSize(new Dimension(100,Theme.BTN_HEIGHT));
        btnExp.addActionListener(e->showSuccess("Export Excel memerlukan Apache POI library.\nTambahkan poi-ooxml ke project."));
        fi.add(new JLabel("Kelas:")); fi.add(cmbKelas); fi.add(btnT); fi.add(btnExp);
        
        fc.add(fi, BorderLayout.CENTER); wrapper.add(fc); wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));

        JPanel card = buildCard();
        card.add(buildCardHeader("📋  Daftar Nilai Siswa"), BorderLayout.NORTH);
        model = new DefaultTableModel(COLS,0){@Override public boolean isCellEditable(int r,int c){return false;}};
        table = new StyledTable(model);
        int[] ws={80,220,90,150,120};
        for(int i=0;i<ws.length;i++) table.getColumnModel().getColumn(i).setPreferredWidth(ws[i]);
        JScrollPane sc = new JScrollPane(table); sc.setBorder(BorderFactory.createEmptyBorder());
        card.add(sc, BorderLayout.CENTER);

        //untuk manggil semua main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.add(wrapper, BorderLayout.NORTH);

        add(mainPanel, BorderLayout.CENTER);         
    }

    public void init() { cmbKelas.removeAllItems(); for(Kelas k:kelasDao.findAll()) cmbKelas.addItem(k); }

    private void loadData() {
        Kelas kelas = (Kelas) cmbKelas.getSelectedItem(); if(kelas==null){showError("Pilih kelas.");return;}
        List<Object[]> rows = nilaiDao.getDaftarNilaiKelas(kelas.getKelasId());
        model.setRowCount(0);
        for(Object[] r:rows){
            double rata=(double)r[3];
            String predikat = rata>=90?"A (Sangat Baik)":rata>=80?"B (Baik)":rata>=70?"C (Cukup)":"D (Kurang)";
            model.addRow(new Object[]{r[0], r[1], r[2], String.format("%.2f",rata), predikat});
        }
    }
}
