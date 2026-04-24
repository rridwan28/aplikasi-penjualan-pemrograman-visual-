package ui;
 
//import ui.components.TopBarPanel;
import util.Session;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

class AdminMainFrame {
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton activeMenuBtn = null;

    public AdminMainFrame() {
        initUI();
    }
    
    private void initUI() {
        /**setTitle                 ("Sistem Informasi Sekolah");
        setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
        setPreferredSize         (new Dimension(1280, 720));
        setMinimumSize           (new Dimension(1024, 600));**/
        
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Theme.BG_APP);
        
        //topbar
//        root.add(new TopBarPanel(), BorderLayout.NORTH);        
        
        //body
        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.BG_APP);
        
        
        //contentcard
        cardLayout  = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_APP);
        body.add(contentPanel, BorderLayout.CENTER);
        
        //Instantiate & register pages
    }   

    void setVisible(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}  
    
