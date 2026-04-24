package ui.components;
import util.Theme;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

//Helper untuk membuat field form yang konsisten.
//Setiap field: Label (atas) + Control (bawah), height input = 32px.

public class FormField {
    
    /** Label + JTextField dalam satu JPanel vertikal. */
    public static JPanel text(String label, JTextField field) {
        return wrap(label, styleField(field));
    }
 
    /** Label + JPasswordField. */
    public static JPanel password(String label, JPasswordField field) {
        return wrap(label, styleField(field));
    }
 
    /** Label + JComboBox. */
    public static JPanel combo(String label, JComboBox<?> combo) {
        combo.setFont(Theme.FONT_REGULAR);
        combo.setPreferredSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        combo.setBackground(Theme.BG_WHITE);
        return wrap(label, combo);
    }
 
    /** Label + JTextArea dalam JScrollPane. */
    public static JPanel textArea(String label, JTextArea area) {
        area.setFont(Theme.FONT_REGULAR);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(Integer.MAX_VALUE, 72));
        scroll.setBorder(fieldBorder());
        return wrap(label, scroll);
    }
 
    /** Utility: pakai ini untuk memberi border pada field yang sudah ada. */
    private static <T extends JComponent> T styleField(T field) {
        field.setFont(Theme.FONT_REGULAR);
        field.setPreferredSize(new Dimension(Integer.MAX_VALUE, Theme.INPUT_HEIGHT));
        field.setBorder(BorderFactory.createCompoundBorder(
            fieldBorder(),
            BorderFactory.createEmptyBorder(0, 8, 0, 8)
        ));
        return field;
    }
 
    private static Border fieldBorder() {
        return BorderFactory.createLineBorder(Theme.BORDER, 1, true);
    }
 
    private static JPanel wrap(String labelText, JComponent control) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
 
        JLabel label = new JLabel(labelText);
        label.setFont(Theme.FONT_BOLD);
        label.setForeground(Theme.TEXT_BODY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        control.setAlignmentX(Component.LEFT_ALIGNMENT);
        control.setMaximumSize(new Dimension(Integer.MAX_VALUE, control.getPreferredSize().height + 10));
 
        panel.add(label);
        panel.add(Box.createVerticalStrut(4));
        panel.add(control);
        return panel;
    }    
}
