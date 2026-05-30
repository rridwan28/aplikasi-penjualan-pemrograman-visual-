package form;
import config.koneksi;
import com.formdev.flatlaf.FlatDarkLaf; 
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Locale;
import java.awt.event.KeyEvent;
import javax.swing.table.TableModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.awt.event.KeyEvent;

public class FormBarang extends javax.swing.JFrame {
    private int xMouse, yMouse;     
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;    
    
    public FormBarang() {        
        try {
            UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Gagal menerapkan FlatLaf");
        }
        
        this.setUndecorated(true);   
        initComponents();             
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Abaikan jika gagal mengembalikan tema
        }
        
        kosong();
        aktif();
        datatable();
        kd_barang.setText(generateId());
        kd_barang.setEditable(false);
        
        cbjenis.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[] { "Makanan","Minuman" }
        ));        
        
        //JFRAME
        this.setBackground(new java.awt.Color(0, 0, 0, 0));
        this.getContentPane().setBackground(new java.awt.Color(0, 0, 0, 0));
        ((javax.swing.JComponent)this.getContentPane()).setOpaque(false);
        this.getRootPane().setOpaque(false);
        //CONTENT
        CONTENT.setOpaque(false);         
        CONTENT.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));                        
        CONTENT.putClientProperty("JComponent.roundRect", true);
        CONTENT.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
            new java.awt.Insets(0, 0, 0, 0),
            new java.awt.Color(150, 150, 150), 
            0, 
            30
        ));
        //BUTTON STYLE
        javax.swing.JButton[] buttons = {bsave, bchange, bdelete, bclear};
        for (javax.swing.JButton b : buttons) {        
        b.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
            new java.awt.Insets(5, 15, 5, 15),   
            new java.awt.Color(0, 0, 0, 0),      
            0,                                   
            15                                   
        ));    
        b.putClientProperty("flatlaf.style", "focusWidth: 0;");    
        b.setContentAreaFilled(true);
        b.setFocusPainted(false);
        b.setBorderPainted(true);
        }
        
        //TABLE
        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder()); 
        jScrollPane1.setViewportBorder(null);
        tbl_brg.setShowHorizontalLines(true);
        tbl_brg.setShowVerticalLines(true);
        tbl_brg.setGridColor(new java.awt.Color(230, 230, 230));
        tbl_brg.setRowHeight(30);
        tbl_brg.getTableHeader().putClientProperty("flatlaf.style", "separatorColor: rgba(0,0,0,0)");
        tbl_brg.getTableHeader().setBackground(java.awt.Color.WHITE);
        
        //PLACEHOLDER
        nm_brg.putClientProperty("JTextField.placeholderText", "Masukkan nama barang lengkap...");
        hargabeli.putClientProperty("JTextField.placeholderText", "Contoh: 50000");
        hargajual.putClientProperty("JTextField.placeholderText", "Contoh: 75000");        
        
        //STYLE
        initMoving(this);
        setLocationRelativeTo(null);        
        rounded_style(this);
        ExitStyle (this);           
    
        javax.swing.Action enterAction = new javax.swing.AbstractAction() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            javax.swing.JButton btn = (javax.swing.JButton) e.getSource();
                btn.doClick();
                }
            };
        javax.swing.KeyStroke enter = javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        
            bsave.getInputMap().put(enter, "enter");
            bsave.getActionMap().put("enter", enterAction);

            bchange.getInputMap().put(enter, "enter");
            bchange.getActionMap().put("enter", enterAction);

            bdelete.getInputMap().put(enter, "enter");
            bdelete.getActionMap().put("enter", enterAction);

            bclear.getInputMap().put(enter, "enter");
            bclear.getActionMap().put("enter", enterAction);
    }  
    
    protected String generateId() {
    String newId = "B001"; 
    try {
        String sql = "SELECT kd_barang FROM barang ORDER BY kd_barang DESC LIMIT 1";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        if (rs.next()) {
            String lastId = rs.getString("kd_barang");        
            int angka = Integer.parseInt(lastId.substring(3));
            angka++;            
            newId = String.format("B%03d", angka);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal generate ID: " + e);
    }
    return newId;
}
    protected void aktif(){
        kd_barang.requestFocus();
    }
    
    protected void kosong(){
        kd_barang.setText("");
        nm_brg.setText("");
        hargajual.setText("");
        hargabeli.setText("");        
    }
    
    protected void datatable(){
        Object[] Baris ={"Kode Barang","Nama Barang","Jenis","Harga Jual","Harga Beli"};
            tabmode = new DefaultTableModel(null, Baris);
            String cariitem = txtcari.getText();
            
            try{
                String sql = "SELECT * FROM barang where kd_barang like '%"+cariitem+"%' or nm_brg like '%"+cariitem+"%' order by kd_barang asc";
                Statement stat = conn.createStatement();
                ResultSet hasil = stat.executeQuery(sql);
                while(hasil.next()){
                    tabmode.addRow(new Object[]{
                    hasil.getString(1),
                    hasil.getString(2),
                    hasil.getString(3),
                    hasil.getString(4),
                    hasil.getString(5),
                    });
                }
                tbl_brg.setModel(tabmode);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(null, "Data gagal dipanggil" + e);
                }   
    }        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        CONTENT = new javax.swing.JPanel();
        header = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());

                // Kita gambar lengkungan hanya di ATAS, tapi KOTAK di BAWAH
                // Triknya: Gambar RoundRect, tapi tingginya dilebihkan 20-30 pixel
                // ke bawah agar bagian lengkung bawahnya "keluar" dari area panel dan tidak terlihat.
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 30, 30, 30);

                g2.dispose();
            }
        };
        header.setOpaque(false); // WAJIB agar kotak aslinya hilang
        ;
        exit = new javax.swing.JButton();
        title1 = new javax.swing.JLabel();
        KodeBarang = new javax.swing.JLabel();
        NamaBarang = new javax.swing.JLabel();
        kd_barang = new javax.swing.JTextField();
        nm_brg = new javax.swing.JTextField();
        labelJenis = new javax.swing.JLabel();
        cbjenis = new javax.swing.JComboBox<>();
        HargaBeli = new javax.swing.JLabel();
        hargabeli = new javax.swing.JTextField();
        HargaJual = new javax.swing.JLabel();
        hargajual = new javax.swing.JTextField();
        bsave = new javax.swing.JButton();
        bchange = new javax.swing.JButton();
        bdelete = new javax.swing.JButton();
        bclear = new javax.swing.JButton();
        title = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        DataBarang = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tbl_brg = new javax.swing.JTable();
        txtcari = new javax.swing.JTextField();
        bsearch = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Form Barang - Manajemen Data Barang");
        setBackground(new java.awt.Color(0,0,0,0));
        setUndecorated(true);
        setResizable(false);

        CONTENT.setBackground(new java.awt.Color(255, 255, 255));
        CONTENT.setForeground(new java.awt.Color(255, 255, 255));
        CONTENT.setPreferredSize(new java.awt.Dimension(600, 500));

        header.setBackground(new java.awt.Color(119, 143, 123));
        header.setAlignmentX(0.0F);
        header.setAlignmentY(0.0F);
        header.setPreferredSize(new java.awt.Dimension(341, 70));

        exit.setAlignmentX(0.5F);
        exit.setBorder(null);
        exit.setMargin(new java.awt.Insets(0, 0, 0, 0));
        exit.setPreferredSize(new java.awt.Dimension(28, 28));
        exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitActionPerformed(evt);
            }
        });

        title1.setBackground(new java.awt.Color(204, 204, 204));
        title1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 15)); // NOI18N
        title1.setForeground(new java.awt.Color(255, 255, 255));
        title1.setLabelFor(CONTENT);
        title1.setText("FORM DATA BARANG - MANA JEMEN DATA BARANG");
        title1.setMaximumSize(new java.awt.Dimension(233, 233));
        title1.setPreferredSize(new java.awt.Dimension(160, 40));

        javax.swing.GroupLayout headerLayout = new javax.swing.GroupLayout(header);
        header.setLayout(headerLayout);
        headerLayout.setHorizontalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(title1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(73, 73, 73)
                .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        headerLayout.setVerticalGroup(
            headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, headerLayout.createSequentialGroup()
                .addGap(0, 4, Short.MAX_VALUE)
                .addGroup(headerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(title1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        KodeBarang.setBackground(new java.awt.Color(204, 204, 204));
        KodeBarang.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        KodeBarang.setLabelFor(CONTENT);
        KodeBarang.setText("Kode Barang");
        KodeBarang.setPreferredSize(new java.awt.Dimension(130, 20));

        NamaBarang.setBackground(new java.awt.Color(204, 204, 204));
        NamaBarang.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        NamaBarang.setLabelFor(CONTENT);
        NamaBarang.setText("Nama Barang");
        NamaBarang.setMaximumSize(new java.awt.Dimension(133, 18));
        NamaBarang.setMinimumSize(new java.awt.Dimension(133, 18));
        NamaBarang.setPreferredSize(new java.awt.Dimension(130, 20));

        kd_barang.setBackground(new java.awt.Color(204, 204, 204));
        kd_barang.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        kd_barang.setBorder(null);
        kd_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kd_barangActionPerformed(evt);
            }
        });

        nm_brg.setBackground(new java.awt.Color(204, 204, 204));
        nm_brg.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        nm_brg.setForeground(new java.awt.Color(255, 255, 255));
        nm_brg.setBorder(null);
        nm_brg.setMinimumSize(new java.awt.Dimension(6, 21));
        nm_brg.setPreferredSize(new java.awt.Dimension(130, 20));
        nm_brg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nm_brgActionPerformed(evt);
            }
        });

        labelJenis.setBackground(new java.awt.Color(204, 204, 204));
        labelJenis.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        labelJenis.setLabelFor(CONTENT);
        labelJenis.setText("Jenis");
        labelJenis.setPreferredSize(new java.awt.Dimension(130, 20));

        cbjenis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbjenisActionPerformed(evt);
            }
        });

        HargaBeli.setBackground(new java.awt.Color(204, 204, 204));
        HargaBeli.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        HargaBeli.setLabelFor(CONTENT);
        HargaBeli.setText("Harga Beli");
        HargaBeli.setPreferredSize(new java.awt.Dimension(130, 20));

        hargabeli.setBackground(new java.awt.Color(204, 204, 204));
        hargabeli.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hargabeli.setBorder(null);
        hargabeli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargabeliActionPerformed(evt);
            }
        });

        HargaJual.setBackground(new java.awt.Color(204, 204, 204));
        HargaJual.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        HargaJual.setLabelFor(CONTENT);
        HargaJual.setText("Harga Jual");
        HargaJual.setPreferredSize(new java.awt.Dimension(130, 20));

        hargajual.setBackground(new java.awt.Color(204, 204, 204));
        hargajual.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        hargajual.setBorder(null);
        hargajual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hargajualActionPerformed(evt);
            }
        });

        bsave.setBackground(new java.awt.Color(51, 255, 153));
        bsave.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bsave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/save.png"))); // NOI18N
        bsave.setText("Save");
        bsave.setPreferredSize(new java.awt.Dimension(103, 27));
        bsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsaveActionPerformed(evt);
            }
        });

        bchange.setBackground(new java.awt.Color(200, 200, 200));
        bchange.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bchange.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/change.png"))); // NOI18N
        bchange.setText("Change");
        bchange.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bchangeActionPerformed(evt);
            }
        });

        bdelete.setBackground(new java.awt.Color(232, 17, 35));
        bdelete.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bdelete.setForeground(new java.awt.Color(255, 255, 255));
        bdelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/delete.png"))); // NOI18N
        bdelete.setText("Delete");
        bdelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bdeleteActionPerformed(evt);
            }
        });

        bclear.setBackground(new java.awt.Color(102, 102, 102));
        bclear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        bclear.setForeground(new java.awt.Color(255, 255, 255));
        bclear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/clear.png"))); // NOI18N
        bclear.setText("Cancel");
        bclear.setPreferredSize(new java.awt.Dimension(103, 27));
        bclear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bclearActionPerformed(evt);
            }
        });

        title.setBackground(new java.awt.Color(204, 204, 204));
        title.setFont(new java.awt.Font("Segoe UI Semibold", 0, 22)); // NOI18N
        title.setForeground(new java.awt.Color(51, 51, 51));
        title.setLabelFor(CONTENT);
        title.setText("Input Data Barang");
        title.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        title.setMaximumSize(new java.awt.Dimension(233, 233));
        title.setPreferredSize(new java.awt.Dimension(160, 40));

        jSeparator1.setPreferredSize(new java.awt.Dimension(50, 5));

        DataBarang.setBackground(new java.awt.Color(204, 204, 204));
        DataBarang.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        DataBarang.setLabelFor(CONTENT);
        DataBarang.setText("Data Barang");
        DataBarang.setPreferredSize(new java.awt.Dimension(130, 20));

        tbl_brg.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Jenis", "Harga Beli", "Harga Jual"
            }
        ));
        tbl_brg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_brgMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tbl_brg);

        txtcari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtcariActionPerformed(evt);
            }
        });
        txtcari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtcariKeyPressed(evt);
            }
        });

        bsearch.setBackground(new java.awt.Color(51, 255, 153));
        bsearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bsearch.setText("Cari");
        bsearch.setPreferredSize(new java.awt.Dimension(103, 27));
        bsearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bsearchMouseClicked(evt);
            }
        });
        bsearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsearchActionPerformed(evt);
            }
        });
        bsearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                bsearchKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout CONTENTLayout = new javax.swing.GroupLayout(CONTENT);
        CONTENT.setLayout(CONTENTLayout);
        CONTENTLayout.setHorizontalGroup(
            CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(header, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addGroup(CONTENTLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(CONTENTLayout.createSequentialGroup()
                        .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(DataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CONTENTLayout.createSequentialGroup()
                                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(CONTENTLayout.createSequentialGroup()
                                        .addGap(8, 8, 8)
                                        .addComponent(labelJenis, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(kd_barang)
                                    .addComponent(cbjenis, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(nm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(CONTENTLayout.createSequentialGroup()
                                        .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(CONTENTLayout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(hargabeli, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(CONTENTLayout.createSequentialGroup()
                                                .addGap(8, 8, 8)
                                                .addComponent(HargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(CONTENTLayout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(bsave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(bchange)
                                .addGap(18, 18, 18)
                                .addComponent(bdelete)
                                .addGap(18, 18, 18)
                                .addComponent(bclear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(CONTENTLayout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addComponent(KodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 518, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(CONTENTLayout.createSequentialGroup()
                                    .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(bsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addGap(0, 30, Short.MAX_VALUE)))
                .addContainerGap())
        );
        CONTENTLayout.setVerticalGroup(
            CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CONTENTLayout.createSequentialGroup()
                .addComponent(header, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(KodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(kd_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nm_brg, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(CONTENTLayout.createSequentialGroup()
                        .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(HargaBeli, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(labelJenis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(hargabeli)
                            .addComponent(cbjenis, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(CONTENTLayout.createSequentialGroup()
                        .addComponent(HargaJual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hargajual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bsave, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bchange, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bdelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bclear, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(DataBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CONTENTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CONTENTLayout.createSequentialGroup()
                        .addComponent(bsearch, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CONTENTLayout.createSequentialGroup()
                        .addComponent(txtcari, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(CONTENT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(CONTENT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        CONTENT.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void kd_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kd_barangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_kd_barangActionPerformed

    private void nm_brgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nm_brgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nm_brgActionPerformed

    private void exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitActionPerformed
        dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_exitActionPerformed

    private void hargabeliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargabeliActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargabeliActionPerformed

    private void hargajualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hargajualActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hargajualActionPerformed

    private void bsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsaveActionPerformed
        String jenis = cbjenis.getSelectedItem().toString();
        
        String sql = "insert into barang values (?,?,?,?,?)";
        try{
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, kd_barang.getText());
            stat.setString(2, nm_brg.getText());
            stat.setString(3, jenis);
            stat.setString(4, hargabeli.getText());
            stat.setString(5, hargajual.getText());

            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "data berhasil disimpan"); // ← TAMBAHKAN INI
            
            kosong();
            kd_barang.setText(generateId());
            kd_barang.setEditable(false);
            kd_barang.requestFocus();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal disimpan" + e);
            kd_barang.setText(generateId()); 
            kd_barang.setEditable(false);
            kd_barang.requestFocus();
        }
        datatable();                
    }//GEN-LAST:event_bsaveActionPerformed

    private void bchangeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bchangeActionPerformed
        try{
            String sql  = "Update barang set nm_brg=?,jenis=?,hargabeli=?,hargajual=? where kd_barang='"+kd_barang.getText()+"'";      
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, nm_brg.getText());
            stat.setString(2, cbjenis.getSelectedItem().toString());
            stat.setString(3, hargabeli.getText());   
            stat.setString(4, hargajual.getText());
            
            stat.executeUpdate();
            JOptionPane.showMessageDialog(null, "data berhasil diubah");
            kosong();
            kd_barang.requestFocus();                      
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal diubah" + e);
        }
        datatable();
                                        
    }//GEN-LAST:event_bchangeActionPerformed

    private void bdeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bdeleteActionPerformed
        int ok = JOptionPane.showConfirmDialog(null, "Apakah anda ingin menghapus data ini?", "konfirmasi dialog", JOptionPane.YES_NO_OPTION);
            if (ok==0){
                String sql = "delete from barang where kd_barang ='"+kd_barang.getText()+"'" ;                
                try{
                    PreparedStatement stat = conn.prepareStatement(sql);
                    stat.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
                    kosong();
                    kd_barang.setText(generateId());
                    kd_barang.setEditable(false);
                    kd_barang.requestFocus();                                                            
                }
                catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "data gagal dihapus" + e);
                }
                datatable();
            }        
    }//GEN-LAST:event_bdeleteActionPerformed

    private void bclearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bclearActionPerformed
        kosong();
        datatable();
        kd_barang.setText(generateId());
        kd_barang.setEditable(false);        
    }//GEN-LAST:event_bclearActionPerformed

    private void cbjenisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbjenisActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbjenisActionPerformed

    private void txtcariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtcariActionPerformed
        
    }//GEN-LAST:event_txtcariActionPerformed

    private void bsearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsearchActionPerformed
        datatable();
    }//GEN-LAST:event_bsearchActionPerformed

    private void bsearchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bsearchMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_bsearchMouseClicked

    private void bsearchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_bsearchKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_bsearchKeyPressed

    private void txtcariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcariKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER){
            datatable();
        }
    }//GEN-LAST:event_txtcariKeyPressed

    private void tbl_brgMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_brgMouseClicked
        
        int bar = tbl_brg.getSelectedRow();
            if (bar == -1) return;
            String a = tabmode.getValueAt(bar, 0).toString();
            String b = tabmode.getValueAt(bar, 1).toString();
            String c = tabmode.getValueAt(bar, 2).toString();
            String d = tabmode.getValueAt(bar, 3).toString();
            String e = tabmode.getValueAt(bar, 4).toString();

            kd_barang.setText(a);
            nm_brg.setText(b);
            cbjenis.setSelectedItem(c);
            hargabeli.setText(d);
            hargajual.setText(e);
                
    }//GEN-LAST:event_tbl_brgMouseClicked
    public static void main(String args[]) {
        try {          
        FlatLightLaf.setup();        
        UIManager.put("TitlePane.unifiedBackground", false);
        javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
                         
        UIManager.put("TitlePane.background", new java.awt.Color(59, 91, 165));
        UIManager.put("Root.background", new java.awt.Color(59, 91, 165)); 
        UIManager.put("TitlePane.foreground", new java.awt.Color(250, 250, 250));         
        
    } catch (Exception ex) {
        ex.printStackTrace();
    }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FormBarang().setVisible(true);
            }
        });
    }
    
    private void initMoving(java.awt.Container container) {    
        header.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                xMouse = evt.getX();
                yMouse = evt.getY();
            }
        });
        
        header.addMouseMotionListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent evt) {            
            int x = evt.getXOnScreen();
            int y = evt.getYOnScreen();                       
            setLocation(x - xMouse, y - yMouse);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CONTENT;
    private javax.swing.JLabel DataBarang;
    private javax.swing.JLabel HargaBeli;
    private javax.swing.JLabel HargaJual;
    private javax.swing.JLabel KodeBarang;
    private javax.swing.JLabel NamaBarang;
    private javax.swing.JButton bchange;
    private javax.swing.JButton bclear;
    private javax.swing.JButton bdelete;
    private javax.swing.JButton bsave;
    private javax.swing.JButton bsearch;
    private javax.swing.JComboBox<String> cbjenis;
    private javax.swing.JButton exit;
    private javax.swing.JTextField hargabeli;
    private javax.swing.JTextField hargajual;
    private javax.swing.JPanel header;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField kd_barang;
    private javax.swing.JLabel labelJenis;
    private javax.swing.JTextField nm_brg;
    private javax.swing.JTable tbl_brg;
    private javax.swing.JLabel title;
    private javax.swing.JLabel title1;
    private javax.swing.JTextField txtcari;
    // End of variables declaration//GEN-END:variables
    
    private void rounded_style(java.awt.Container container) {
    for (java.awt.Component c : container.getComponents()) {
        //TEXTFIELD STYLE
        if (c instanceof javax.swing.JTextField) {            
            javax.swing.JTextField tf = (javax.swing.JTextField) c;            
            tf.putClientProperty("flatlaf.style", "arc: 15");
            tf.putClientProperty("JComponent.roundRect", true);
            tf.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
            tf.setForeground(java.awt.Color.BLACK);
            tf.setBackground(java.awt.Color.white);
            tf.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
                new java.awt.Insets(5, 10, 5, 10), 
                new java.awt.Color (170, 170, 170), 
                1, 
                15
            ));
            //COMBO BOX STYLE
            } else if (c instanceof javax.swing.JComboBox) {
            javax.swing.JComboBox cb = (javax.swing.JComboBox) c;                        
            cb.putClientProperty("JComponent.roundRect", true); 
            cb.putClientProperty("flatlaf.style", "arc: 15; focusWidth: 0;");            
            cb.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
                new java.awt.Insets(2, 5, 2, 5), 
                new java.awt.Color(170, 170, 170), 1, 15
            ));
            //TABLE
            } else if (c instanceof javax.swing.JScrollPane) {
            ((javax.swing.JScrollPane)c).setBorder(javax.swing.BorderFactory.createEmptyBorder());
            
            } else if (c instanceof java.awt.Container) {
                rounded_style((java.awt.Container) c);
            }
        }
    }
        
    private void ExitStyle (java.awt.Container container) { 
    ImageIcon iconNormal = new javax.swing.ImageIcon(getClass().getResource("/icon/exit.png"));
    ImageIcon iconHover = new javax.swing.ImageIcon(getClass().getResource("/icon/exit_putih.png"));
    
    exit.setIcon(iconNormal);
    
    exit.setMargin(new java.awt.Insets(0, 0, 0, 0));
    exit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    exit.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
    exit.setIconTextGap(0);
    
    exit.setBorder(new com.formdev.flatlaf.ui.FlatLineBorder(
            new java.awt.Insets(0, 0, 0, 0), 
            new java.awt.Color(0, 0, 0, 0), 0, 15));    
    exit.setBackground(new java.awt.Color(240, 243, 240)); 
    exit.setForeground(new java.awt.Color(100, 100, 100)); 
    exit.setBorderPainted(false); 
    exit.setFocusPainted(false);
    
    exit.addMouseListener(new java.awt.event.MouseAdapter() {       
        public void mouseEntered(java.awt.event.MouseEvent evt) {
            exit.setBackground(new java.awt.Color(232, 17, 35)); 
            exit.setForeground(java.awt.Color.WHITE);
            exit.setIcon(iconHover);
        }

        public void mouseExited(java.awt.event.MouseEvent evt) {            
            exit.setBackground(new java.awt.Color(240, 240, 240)); 
            exit.setForeground(new java.awt.Color(100, 100, 100));
            exit.setIcon(iconNormal);
        }
    });
}
}
