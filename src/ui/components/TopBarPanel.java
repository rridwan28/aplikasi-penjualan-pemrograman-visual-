package ui.components;

import util.Session;
import util.Theme;
import javax.swing.*;
import java.awt.*;

//TopBar horizontal biru di atas semua halaman.
//Preferred height: 48px

public class TopBarPanel extends JPanel{
    
    public TopBarPanel() {
        setBackground(Theme.PRIMARY);
        setPreferredSize(new Dimension(0, Theme.TOPBAR_HEIGHT));
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
 
        // ─ Left: Logo + app name ─
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        left.setOpaque(false);
 
        JLabel logoIcon = new JLabel("🏫");
        logoIcon.setFont(new Font(Theme.FONT_NAME, Font.PLAIN, 20));
 
        JLabel appName = new JLabel("SISTEM SEKOLAH");
        appName.setFont(Theme.FONT_TOPBAR);
        appName.setForeground(Theme.TEXT_WHITE);
 
        left.add(logoIcon);
        left.add(appName);
 
        // ─ Right: User info ─
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);
 
        String nama = Session.currentUser != null ? Session.currentUser.getNamaLengkap() : "User";
        String role = Session.currentUser != null ? Session.currentUser.getRole() : "";
 
        JLabel userLabel = new JLabel(nama + "  (" + role + ")");
        userLabel.setFont(Theme.FONT_REGULAR);
        userLabel.setForeground(Theme.TEXT_WHITE);
 
        right.add(new JLabel("👤  "));
        right.add(userLabel);
 
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }
    
}
