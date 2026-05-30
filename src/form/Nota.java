package form;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.JSpinner;
import config.koneksi;
import java.util.HashMap;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class Nota extends javax.swing.JFrame {
   
    public String id, nama, jenis, telp, almt;
    public String kdbrg, nmbrg, jenisbrg, hb, hj;
    private Connection conn = new koneksi().connect();
    private DefaultTableModel tabmode;
    
    public Nota() {
        initComponents();
        
        String KD = UserID.getUserLogin(); 
        txtidkasir.setText(KD);
        
        nama(); 
        kosong();
        aktif();
        autonumber();        
    }
        
     protected void nama(){
        try {
            String sql = "SELECT * FROM kasir WHERE id_kasir='"+txtidkasir.getText()+"'";
            Statement stat = conn.createStatement();
            ResultSet hasil = stat.executeQuery(sql);
            if(hasil.next()){
                txtnama.setText(hasil.getString("nm_kasir"));
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "data gagal dipanggil "+e);
        }
    }
    
    protected void aktif(){
        txtqty.requestFocus();
        jtgl.setEditor(new JSpinner.DateEditor(jtgl, "yyyy/MM/dd"));
        Object[] Baris = {"KD Barang", "Nama", "Harga Beli", "Harga Jual", "QTY", "Total"};
        tabmode = new DefaultTableModel(null, Baris);
        tbltransaksi.setModel(tabmode);
    } 
    
    protected void kosong(){
        txtid.setText("");
        txtnm.setText("");
        txtalmt.setText("");
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtqty.setText("");
        txttotal.setText("");
    }
    
    protected void autonumber(){
        try {
            String sql = "SELECT idnota FROM nota order by idnota asc";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            txtidnota.setText("IN0001");
            while (rs.next()) {
                String Nol = "";
                String id_nota = rs.getString("idnota").substring(2);
                int AN = Integer.parseInt(id_nota) + 1;
                if(AN<10) { Nol = "000"; }
                else if (AN<100) { Nol = "00"; }
                else if (AN<1000) { Nol = "0"; }
                else if(AN<10000) { Nol = ""; }
                txtidnota.setText("IN" + Nol + AN);
            }
        } catch(Exception e){
            JOptionPane.showMessageDialog(null, "Auto Number Gagal " + e);
        }
    }
       
    public void itemTerpilih(){
        popuppelanggan Pp = new popuppelanggan();
        Pp.plgn = this;
        txtid.setText(id);
        txtnm.setText(nama);
        txtalmt.setText(almt);
    }

    public void itemTerpilihBrg(){
        popupbarang Pbrg = new popupbarang();
        Pbrg.brg = this;
        txtkdbrg.setText(kdbrg);
        txtnmbrg.setText(nmbrg);
        txthb.setText(hb);
        txthj.setText(hj);
        txtqty.requestFocus();
    }

    public void hitung(){
        int total = 0;
        for (int i=0; i< tbltransaksi.getRowCount(); i++){
            int amount = Integer.valueOf(tbltransaksi.getValueAt(i, 5).toString());
            total += amount;
        }
        txtttotal.setText(Integer.toString(total));
    }
    
    public void cetak(){
    try {
        java.io.InputStream reportStream = getClass().getResourceAsStream("/form/nota.jasper");
        
        if (reportStream == null) {
            JOptionPane.showMessageDialog(this, "File nota.jasper TIDAK ditemukan");
            return;
        }

        HashMap<String, Object> parameter = new HashMap<>();
        parameter.put("idnota", txtidnota.getText()); // "p_idnota" harus sama dengan nama parameter di iReport kamu

        JasperPrint print = JasperFillManager.fillReport(reportStream, parameter, conn);
        JasperViewer.viewReport(print, false);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Gagal menampilkan nota: " + ex);
    }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtidkasir = new javax.swing.JTextField();
        txtidnota = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        txtid = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtnm = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtalmt = new javax.swing.JTextField();
        bcarip = new java.awt.Button();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtkdbrg = new javax.swing.JTextField();
        txtnmbrg = new javax.swing.JTextField();
        bcaribrg = new java.awt.Button();
        jLabel12 = new javax.swing.JLabel();
        txthb = new javax.swing.JTextField();
        txthj = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtqty = new javax.swing.JTextField();
        txttotal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        btambah = new java.awt.Button();
        jLabel6 = new javax.swing.JLabel();
        txtnama = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbltransaksi = new javax.swing.JTable();
        bsimpan = new java.awt.Button();
        bhapus = new java.awt.Button();
        bbatal = new java.awt.Button();
        bkeluar = new java.awt.Button();
        jLabel16 = new javax.swing.JLabel();
        txtttotal = new javax.swing.JTextField();
        jtgl = new javax.swing.JSpinner();

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 20)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Nota");

        jLabel3.setText("ID Kasir");

        jLabel4.setText("ID Nota");

        txtidkasir.setEditable(false);
        txtidkasir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidkasirActionPerformed(evt);
            }
        });

        txtidnota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidnotaActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pelanggan"));

        txtid.setEditable(false);
        txtid.setBackground(new java.awt.Color(255, 255, 255));
        txtid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtidActionPerformed(evt);
            }
        });

        jLabel7.setText("ID Pelanggan");

        jLabel8.setText("Nama");

        txtnm.setEditable(false);
        txtnm.setBackground(new java.awt.Color(255, 255, 255));
        txtnm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmActionPerformed(evt);
            }
        });

        jLabel9.setText("Alamat");

        txtalmt.setEditable(false);
        txtalmt.setBackground(new java.awt.Color(255, 255, 255));
        txtalmt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtalmtActionPerformed(evt);
            }
        });

        bcarip.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bcarip.setLabel("Cari");
        bcarip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaripActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(41, 41, 41)
                        .addComponent(txtalmt, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtnm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bcarip, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(42, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bcarip, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtnm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtalmt)
                        .addContainerGap())))
        );

        jLabel5.setText("Nama Kasir");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Barang"));
        jPanel2.setPreferredSize(new java.awt.Dimension(253, 190));

        jLabel10.setText("KD Barang");

        jLabel11.setText("Nama");

        txtkdbrg.setEditable(false);
        txtkdbrg.setBackground(new java.awt.Color(255, 255, 255));
        txtkdbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtkdbrgActionPerformed(evt);
            }
        });

        txtnmbrg.setEditable(false);
        txtnmbrg.setBackground(new java.awt.Color(255, 255, 255));
        txtnmbrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnmbrgActionPerformed(evt);
            }
        });

        bcaribrg.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bcaribrg.setLabel("Cari");
        bcaribrg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcaribrgActionPerformed(evt);
            }
        });

        jLabel12.setText("Harga Beli");

        txthb.setEditable(false);
        txthb.setBackground(new java.awt.Color(255, 255, 255));
        txthb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthbActionPerformed(evt);
            }
        });

        txthj.setEditable(false);
        txthj.setBackground(new java.awt.Color(255, 255, 255));
        txthj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthjActionPerformed(evt);
            }
        });

        jLabel13.setText("Harga Jual");

        jLabel14.setText("QTY");

        txtqty.setBackground(new java.awt.Color(255, 255, 255));
        txtqty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtqtyActionPerformed(evt);
            }
        });
        txtqty.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtqtyKeyReleased(evt);
            }
        });

        txttotal.setEditable(false);
        txttotal.setBackground(new java.awt.Color(255, 255, 255));
        txttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttotalActionPerformed(evt);
            }
        });

        jLabel15.setText("Total");

        btambah.setActionCommand("tambah");
        btambah.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        btambah.setLabel("Tambah");
        btambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btambahActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtkdbrg, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bcaribrg, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btambah, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel11)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtnmbrg, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabel12)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                            .addComponent(txthb, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13)
                                .addComponent(jLabel15)
                                .addComponent(jLabel14))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txttotal, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(txthj, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(txtqty)))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel10)
                        .addComponent(txtkdbrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(bcaribrg, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtnmbrg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txthb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txthj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtqty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btambah, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btambah.getAccessibleContext().setAccessibleName("Tambah\n");

        jLabel6.setText("Tgl Nota");

        txtnama.setEditable(false);
        txtnama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnamaActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pelanggan"));

        tbltransaksi.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tbltransaksi);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 10, Short.MAX_VALUE))
        );

        bsimpan.setActionCommand("bsimpan");
        bsimpan.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bsimpan.setLabel("Simpan");
        bsimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bsimpanActionPerformed(evt);
            }
        });

        bhapus.setActionCommand("bhapus");
        bhapus.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bhapus.setLabel("Hapus\n");
        bhapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bhapusActionPerformed(evt);
            }
        });

        bbatal.setActionCommand("bbatal");
        bbatal.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bbatal.setLabel("Batal");
        bbatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bbatalActionPerformed(evt);
            }
        });

        bkeluar.setActionCommand("bkeluar");
        bkeluar.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        bkeluar.setLabel("Keluar");
        bkeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bkeluarActionPerformed(evt);
            }
        });

        jLabel16.setText("Total Harga");

        txtttotal.setEditable(false);
        txtttotal.setBackground(new java.awt.Color(255, 255, 255));
        txtttotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtttotalActionPerformed(evt);
            }
        });

        jtgl.setModel(new javax.swing.SpinnerDateModel());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtidkasir, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtidnota))))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtnama, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                    .addComponent(jtgl)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(bkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtttotal, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(bhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(244, 244, 244)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtidkasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtidnota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtnama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jtgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bhapus, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel16)
                                .addComponent(txtttotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(bsimpan, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bbatal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(bkeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(54, Short.MAX_VALUE))
        );

        bbatal.getAccessibleContext().setAccessibleName("batal");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtidkasirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidkasirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidkasirActionPerformed

    private void txtidnotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidnotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidnotaActionPerformed

    private void txtnamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnamaActionPerformed

    private void txtidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtidActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtidActionPerformed

    private void txtnmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmActionPerformed

    private void txtalmtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtalmtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtalmtActionPerformed

    private void bcaripActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaripActionPerformed
        popuppelanggan Pp = new popuppelanggan();
        Pp.plgn = this;
        Pp.setVisible(true);
        Pp.setResizable(false);                                       
    }//GEN-LAST:event_bcaripActionPerformed

    private void txtkdbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtkdbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtkdbrgActionPerformed

    private void txtnmbrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnmbrgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnmbrgActionPerformed

    private void bcaribrgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcaribrgActionPerformed
        popupbarang Pbrg = new popupbarang();
        Pbrg.brg = this;
        Pbrg.setVisible(true);
        Pbrg.setResizable(false);    
    }//GEN-LAST:event_bcaribrgActionPerformed

    private void txthbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthbActionPerformed

    private void txthjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthjActionPerformed

    private void txtqtyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtqtyActionPerformed
        try {
            int xhrgj = Integer.parseInt(txthj.getText());
            int xqty = Integer.parseInt(txtqty.getText());
            int xjml = xhrgj * xqty;
            txttotal.setText(String.valueOf(xjml)); // Mengisi txttotal secara otomatis
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga atau QTY harus berupa angka yang valid!");
        }
    }//GEN-LAST:event_txtqtyActionPerformed

    private void txttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttotalActionPerformed

    private void btambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btambahActionPerformed
    // --- PROTEKSI: Jika kolom penting masih kosong, batalkan proses ---
    if (txtkdbrg.getText().equals("") || txtqty.getText().equals("") || txttotal.getText().equals("")) {
        JOptionPane.showMessageDialog(this, "Pilih barang dan isi QTY terlebih dahulu!");
        return; // Menghentikan eksekusi kode di bawahnya agar tidak eror
    }

    try {
        String kode = txtkdbrg.getText();
        String nama = txtnmbrg.getText();
        int hargab = Integer.parseInt(txthb.getText());
        int hargaj = Integer.parseInt(txthj.getText());
        int qty = Integer.parseInt(txtqty.getText());
        int total = Integer.parseInt(txttotal.getText());
        
        tabmode.addRow(new Object[] {kode, nama, hargab, hargaj, qty, total});
        tbltransaksi.setModel(tabmode);
        
        // Membersihkan kolom input barang setelah berhasil ditambah ke tabel
        txtkdbrg.setText("");
        txtnmbrg.setText("");
        txthb.setText("");
        txthj.setText("");
        txtqty.setText("");
        txttotal.setText("");
        hitung();
        
    } catch(Exception e) {
        JOptionPane.showMessageDialog(this, "Gagal menambahkan data: " + e);
    }

    }//GEN-LAST:event_btambahActionPerformed

    private void bsimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bsimpanActionPerformed
        if (txtid.getText().equals("") || tbltransaksi.getRowCount() == 0) {
        JOptionPane.showMessageDialog(this, "Data Pelanggan atau Item Belanja masih kosong!");
        return;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fd = sdf.format(jtgl.getValue());
        String sql = "insert into nota values (?,?,?,?,?)";
        String zsql = "insert into isi values (?,?,?,?,?)";
        try {
            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1, txtidnota.getText());
            stat.setString(2, fd);
            stat.setString(3, txtid.getText());
            stat.setString(4, txtttotal.getText());
            stat.setString(5, txtidkasir.getText());
            stat.executeUpdate();
            
            int t = tbltransaksi.getRowCount();
            for(int i=0; i<t; i++) {
                String xkd = tbltransaksi.getValueAt(i, 0).toString();
                String xhb = tbltransaksi.getValueAt(i, 2).toString();
                String xhj = tbltransaksi.getValueAt(i, 3).toString();
                String xqty = tbltransaksi.getValueAt(i, 4).toString();
                
                PreparedStatement stat2 = conn.prepareStatement(zsql);
                stat2.setString(1, txtidnota.getText());
                stat2.setString(2, xkd);
                stat2.setString(3, xhb);
                stat2.setString(4, xhj);
                stat2.setString(5, xqty);
                stat2.executeUpdate();
            }
            JOptionPane.showMessageDialog(null, "data berhasil disimpan");
                cetak();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "data gagal disimpan " + e);
        }
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bsimpanActionPerformed

    private void bhapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bhapusActionPerformed
        int index = tbltransaksi.getSelectedRow();
        tabmode.removeRow(index);
        tbltransaksi.setModel(tabmode);
        hitung();
    }//GEN-LAST:event_bhapusActionPerformed
       
    private void bbatalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bbatalActionPerformed
        kosong();
        aktif();
        autonumber();
    }//GEN-LAST:event_bbatalActionPerformed

    private void bkeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bkeluarActionPerformed
        dispose();
    }//GEN-LAST:event_bkeluarActionPerformed

    private void txtttotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtttotalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtttotalActionPerformed

    private void txtqtyKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtqtyKeyReleased
            try {
        // Jika kolom QTY kosong saat diketik, set total jadi 0 agar tidak error
        if (txtqty.getText().equals("")) {
            txttotal.setText("0");
            return;
        }
        
        int xhrgj = Integer.parseInt(txthj.getText());
        int xqty = Integer.parseInt(txtqty.getText());
        int xjml = xhrgj * xqty;
        
        txttotal.setText(String.valueOf(xjml)); // Otomatis mengisi kolom total sewaktu diketik
    } catch (NumberFormatException e) {
        txttotal.setText("0");
    }
    }//GEN-LAST:event_txtqtyKeyReleased

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Nota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Button bbatal;
    private java.awt.Button bcaribrg;
    private java.awt.Button bcarip;
    private java.awt.Button bhapus;
    private java.awt.Button bkeluar;
    private java.awt.Button bsimpan;
    private java.awt.Button btambah;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jtgl;
    private javax.swing.JTable tbltransaksi;
    private javax.swing.JTextField txtalmt;
    private javax.swing.JTextField txthb;
    private javax.swing.JTextField txthj;
    private javax.swing.JTextField txtid;
    private javax.swing.JTextField txtidkasir;
    private javax.swing.JTextField txtidnota;
    private javax.swing.JTextField txtkdbrg;
    private javax.swing.JTextField txtnama;
    private javax.swing.JTextField txtnm;
    private javax.swing.JTextField txtnmbrg;
    private javax.swing.JTextField txtqty;
    private javax.swing.JTextField txttotal;
    private javax.swing.JTextField txtttotal;
    // End of variables declaration//GEN-END:variables
}
