package ui.forms;

import dao.UserDAO;
import model.User;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.net.URL;

public class LoginForm extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private UserDAO userDAO = new UserDAO();

    public LoginForm(){
        initUI();
    }
    
    private void initUI(){
        setTitle                 ("Login - Sistem Informasi Sekolah");
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setPreferredSize         (new Dimension (450, 580));
        setMinimumSize           (new Dimension (350, 520));
        setResizable             (false);
        
        JPanel root = new JPanel (new GridLayout(1, 2));
        root.add(buildBrandingPanel());
        root.add(buildFormPanel());
        
        setContentPane(buildFormPanel());
        
        pack();
        setLocationRelativeTo(null);                                       
    }
    
    private JPanel buildBrandingPanel(){
        JPanel p = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                
                GradientPaint gp = new GradientPaint(
                        0,0, Theme.PRIMARY,
                        getWidth(), getHeight(), new Color(0x3B3E9E)
                                );
                        g2.setPaint(gp);
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        
                        g2.setColor(new Color(255, 255, 255, 20));
                        g2.fillOval(-80, -80, 280, 280);
                        g2.fillOval(getWidth() - 100, getHeight() - 120, 200, 200);
                        g2.dispose();
            }        
        };
        p.setLayout(new GridBagLayout());
 
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        
         // Icon
        JLabel icon = new JLabel("🏫", SwingConstants.CENTER);
        icon.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 52));
        icon.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // App title
        JLabel title = new JLabel("SISTEM INFORMASI");
        title.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 22));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel subtitle = new JLabel("SEKOLAH");
        subtitle.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 22));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        JLabel desc = new JLabel("<html><center>Manajemen Akademik<br>SMP Negeri 1 Jakarta</center></html>");
        desc.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 14));
        desc.setForeground(new Color(200, 210, 255));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        desc.setHorizontalAlignment(SwingConstants.CENTER);
 
        // Version
        JLabel ver = new JLabel("v1.0.0  •  2025/2026");
        ver.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 11));
        ver.setForeground(new Color(180, 190, 240));
        ver.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        content.add(icon);
        content.add(Box.createVerticalStrut(16));
        content.add(title);
        content.add(subtitle);
        content.add(Box.createVerticalStrut(12));
        content.add(desc);
        content.add(Box.createVerticalStrut(24));
        content.add(ver);
 
        p.add(content);
        return p;
    }
    
    
    // ─ Right panel: white login form 
    private JPanel buildFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(0, 0, 0, 0);
        
        // ── Logo/Gambar 
    try {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/icons/logo login.png"));
        Image scaledImage = imageIcon.getImage().getScaledInstance(300, 200, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(scaledImage));
        logo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 10);
        form.add(logo, gbc);
    } catch (Exception e) {
        System.out.println("Error loading image: " + e.getMessage());
        e.printStackTrace();
    }
        
        // ── Header 
        JLabel welcome = new JLabel("SMP NEGERI XX KOTA BEKASI");
        welcome.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 24));
        welcome.setForeground(Theme.TEXT_DARK);
        welcome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        form.add(welcome, gbc);

        JLabel subwelcome = new JLabel("Masuk ke akun Anda");
        subwelcome.setFont(Theme.FONT_SUBTITLE);
        subwelcome.setForeground(Theme.TEXT_MUTED);
        subwelcome.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(subwelcome, gbc);

        // ── Error label 
        lblError = new JLabel("username atau password salah");
        lblError.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 12));
        lblError.setForeground(Theme.DANGER);
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        form.add(lblError, gbc);

        // ── Username label 
        JLabel lblUsr = new JLabel("Username");
        lblUsr.setFont(Theme.FONT_BOLD);
        lblUsr.setForeground(Theme.TEXT_BODY);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 4, 0);
        form.add(lblUsr, gbc);

        // ── Username field
        txtUsername = new JTextField();
        txtUsername.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 14));
        txtUsername.setPreferredSize(new Dimension(360, 40));
        txtUsername.setMinimumSize(new Dimension(360, 40));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 12, 0);
        form.add(txtUsername, gbc);

        // ── Password label 
        JLabel lblPwd = new JLabel("Password");
        lblPwd.setFont(Theme.FONT_BOLD);
        lblPwd.setForeground(Theme.TEXT_BODY);
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 4, 0);
        form.add(lblPwd, gbc);

        // ── Password field 
        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 14));
        txtPassword.setPreferredSize(new Dimension(360, 40));
        txtPassword.setMinimumSize(new Dimension(360, 40));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Theme.BORDER, 1, true),
            BorderFactory.createEmptyBorder(0, 10, 0, 10)
        ));
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 20, 0);
        form.add(txtPassword, gbc);

        // ── Login button 
        btnLogin = new JButton("Masuk") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isRollover() ? Theme.PRIMARY_DARK : Theme.PRIMARY);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 14));
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth() - fm.stringWidth(getText())) / 2,
                    (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                g2.dispose();
            }
        };
        btnLogin.setPreferredSize(new Dimension(360, 42));
        btnLogin.setMinimumSize(new Dimension(360, 42));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setContentAreaFilled(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 0, 0);
        form.add(btnLogin, gbc);

        // Hint
        JLabel hint = new JLabel("default akun");
        hint.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 11));
        hint.setForeground(Theme.TEXT_MUTED);       
        hint.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 9;
        gbc.insets = new Insets(8, 0, 0, 0);
        form.add(hint, gbc);
        
        // ── Tambahkan form ke panel utama (tengah)
        p.add(form, new GridBagConstraints());

        // Enter key
        txtPassword.addActionListener(e -> doLogin());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
        btnLogin.addActionListener(e -> doLogin());
 
        return p;
    }
 
    private void doLogin() {
        String uname = txtUsername.getText().trim();
        String pwd   = new String(txtPassword.getPassword());
 
        if (uname.isEmpty() || pwd.isEmpty()) {
            lblError.setText("Username dan password tidak boleh kosong.");
            return;
        }
 
        User user = userDAO.login(uname, pwd);
        if (user != null) {
            Session.currentUser = user;
            dispose();
            // ── Route berdasarkan role ──
            if ("admin".equals(user.getRole())) {
                new AdminMainFrame().setVisible(true);
            } else {
                new GuruMainFrame().setVisible(true);
            }
        } else {
            lblError.setText("Username atau password salah.");
            txtPassword.setText("");
        }
    }
                    
    public static void main(String[] args) {
                SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new LoginForm().setVisible(true);
        });
    }    
    
}