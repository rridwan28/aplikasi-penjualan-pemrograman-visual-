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
 * RekapPanel - Rekap Kehadiran per Kelas
 */
public class RekapPanel extends BasePanel {

    private KelasDAO   kelasDao = new KelasDAO();
    private AbsensiDAO absDao   = new AbsensiDAO();
    private JComboBox<Kelas> cmbKelas;
    private JTextField txtDari, txtSampai;
    private StyledTable table;
    private DefaultTableModel model;
    private static final String[] COLS={"Kelas","Wali Kelas","Total Siswa","Hadir","Izin","Sakit","Alpha","% Hadir"};

    public RekapPanel() { buildUI(); }

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
        
        JLabel titleLabel = new JLabel("Rekap Kehadiran");
        titleLabel.setFont(Theme.FONT_TITLE);
        titleLabel.setForeground(Theme.TEXT_DARK);  
      
        JLabel subtitleLabel = new JLabel("Ringkasan kehadiran per kelas dan per periode");
        subtitleLabel.setFont(Theme.FONT_SUBTITLE);
        subtitleLabel.setForeground(Theme.TEXT_MUTED);
        
        headerText.add(titleLabel);
        headerText.add(Box.createVerticalStrut(3));
        headerText.add(subtitleLabel);

        headerPanel.add(headerText, BorderLayout.WEST);

        wrapper.add(headerPanel);
        wrapper.add(Box.createVerticalStrut(Theme.GAP_XS)); 
        
        JPanel fc = buildCard(); fc.setMaximumSize(new Dimension(Integer.MAX_VALUE,90));
        JPanel fi = new JPanel(new FlowLayout(FlowLayout.LEFT,10,10)); fi.setBackground(Color.WHITE);
        cmbKelas = new JComboBox<>(); cmbKelas.setFont(Theme.FONT_REGULAR); cmbKelas.setPreferredSize(new Dimension(200,Theme.INPUT_HEIGHT));
        txtDari   = df("2026-01-01"); txtSampai = df(java.time.LocalDate.now().toString());
        StyledButton btnT = new StyledButton("🔍 Tampilkan",StyledButton.SECONDARY); btnT.setPreferredSize(new Dimension(120,Theme.BTN_HEIGHT)); btnT.addActionListener(e->loadData());
        fi.add(new JLabel("Kelas:")); fi.add(cmbKelas);
        fi.add(new JLabel("  Dari:")); fi.add(txtDari);
        fi.add(new JLabel("  s/d:")); fi.add(txtSampai);
        fi.add(btnT);
        
        fc.add(fi, BorderLayout.CENTER); wrapper.add(fc); wrapper.add(Box.createVerticalStrut(Theme.GAP_MD));

        JPanel card = buildCard();
        card.add(buildCardHeader("📈  Rekap Per Kelas"), BorderLayout.NORTH);
        model = new DefaultTableModel(COLS,0){@Override public boolean isCellEditable(int r,int c){return false;}};
        table = new StyledTable(model);
        int[] ws={120,180,100,80,80,80,80,100};
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
        List<Object[]> rows = absDao.getRekapByKelas(kelas.getKelasId(), txtDari.getText(), txtSampai.getText());
        model.setRowCount(0);
        int totalH=0,totalI=0,totalS=0,totalA=0,totalHari=0;
        for(Object[] r:rows){totalH+=(int)r[2];totalI+=(int)r[3];totalS+=(int)r[4];totalA+=(int)r[5];totalHari+=(int)r[6];}
        double pct = totalHari>0?(totalH*100.0/totalHari):0;
        model.addRow(new Object[]{kelas.getNamaKelas(), kelas.getNamaWaliKelas()!=null?kelas.getNamaWaliKelas():"-",
            rows.size(), totalH, totalI, totalS, totalA, String.format("%.1f%%", pct)});
    }

    private JTextField df(String v) {
        JTextField f=new JTextField(v,11); f.setFont(Theme.FONT_REGULAR); f.setPreferredSize(new Dimension(120,Theme.INPUT_HEIGHT));
        f.setBorder(new CompoundBorder(new LineBorder(Theme.BORDER,1,true),new EmptyBorder(0,8,0,8))); return f;
    }
}
