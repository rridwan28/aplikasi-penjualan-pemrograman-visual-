package ui.components;

import util.Theme;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
 
//Gunakan: new StyledButton("Label", StyledButton.PRIMARY)

public class StyledButton extends JButton {

    public static final String PRIMARY   = "PRIMARY";
    public static final String SECONDARY = "SECONDARY";
    public static final String DANGER    = "DANGER";
    public static final String SUCCESS   = "SUCCESS";
 
    private String variant;
    private boolean hovered = false;
 
    public StyledButton(String text, String variant) {
        super(text);
        this.variant = variant;
        setup();
    }
 
    public StyledButton(String text) {
        this(text, PRIMARY);
    }
    
    private void setup() {
        setFont(Theme.FONT_BOLD);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(getPreferredSize().width, Theme.BTN_HEIGHT));
 
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hovered = true; repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hovered = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);            
    
        Color bg, fg, border;
        switch (variant) {
            case PRIMARY:
                bg     = hovered ? Theme.PRIMARY_DARK : Theme.PRIMARY;
                fg     = Color.WHITE;
                border = null;
                break;
            case DANGER:
                bg     = hovered ? new Color(0xFCA5A5) : Theme.DANGER_BG;
                fg     = Theme.DANGER;
                border = new Color(0xFCA5A5);
                break;
            case SUCCESS:
                bg     = hovered ? new Color(0xBBF7D0) : Theme.SUCCESS_BG;
                fg     = Theme.SUCCESS;
                border = new Color(0x86EFAC);
                break;
            default: // SECONDARY
                bg     = hovered ? new Color(0xF9FAFB) : Color.WHITE;
                fg     = Theme.TEXT_BODY;
                border = Theme.BORDER;
                break;
        }
  
        // Background
        g2.setColor(bg);
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 6, 6));
 
        // Border
        if (border != null) {
            g2.setColor(border);
            g2.setStroke(new BasicStroke(1f));
            g2.draw(new RoundRectangle2D.Double(0.5, 0.5, getWidth()-1, getHeight()-1, 6, 6));
        }
 
        // Text
        g2.setFont(getFont());
        g2.setColor(fg);
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
 
        g2.dispose();
    }

}