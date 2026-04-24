package ui.forms;

import ui.components.StyledButton;
import util.Theme;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

/**
 * Base class for all content panels.
 * Provides helpers: buildPageHeader(), buildCard(), buildToolbar(), showMsg().
 */
public abstract class BasePanel extends JPanel {

    public BasePanel() {
        setBackground(Theme.BG_APP);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL, Theme.GAP_XL));
    }

    /** White card panel with shadow border. */
    protected JPanel buildCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));
        return card;
    }

    /** Page header: title + subtitle. */
    protected JPanel buildPageHeader(String title, String subtitle) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(Theme.FONT_TITLE);
        lblTitle.setForeground(Theme.TEXT_DARK);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(Theme.FONT_SUBTITLE);
        lblSub.setForeground(Theme.TEXT_MUTED);

        p.add(lblTitle);
        p.add(Box.createVerticalStrut(3));
        p.add(lblSub);
        return p;
    }

    /** Toolbar row: left components + right components. */
    protected JPanel buildToolbar(JComponent[] left, JComponent[] right) {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);
        toolbar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        leftPanel.setOpaque(false);
        for (JComponent c : left) leftPanel.add(c);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        rightPanel.setOpaque(false);
        for (JComponent c : right) rightPanel.add(c);

        toolbar.add(leftPanel,  BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);
        return toolbar;
    }

    /** Card header row (inside table card). */
    protected JPanel buildCardHeader(String title, JComponent... rightItems) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, Theme.BORDER_LIGHT),
            new EmptyBorder(12, 16, 12, 16)
        ));

        JLabel lbl = new JLabel(title);
        lbl.setFont(Theme.FONT_BOLD);
        lbl.setForeground(Theme.TEXT_BODY);
        p.add(lbl, BorderLayout.WEST);

        if (rightItems.length > 0) {
            JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
            right.setOpaque(false);
            for (JComponent c : rightItems) right.add(c);
            p.add(right, BorderLayout.EAST);
        }
        return p;
    }

    /** Badge label. */
    protected JLabel badge(String text, Color fg, Color bg) {
        JLabel l = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(Theme.FONT_SMALL);
        l.setForeground(fg);
        l.setOpaque(false);
        l.setBorder(new EmptyBorder(2, 8, 2, 8));
        return l;
    }

    /** Styled search field. */
    protected JTextField buildSearchField(String placeholder) {
        JTextField f = new JTextField(16);
        f.setFont(Theme.FONT_REGULAR);
        f.setPreferredSize(new Dimension(220, Theme.INPUT_HEIGHT));
        f.setBorder(new CompoundBorder(
            new LineBorder(Theme.BORDER, 1, true),
            new EmptyBorder(0, 8, 0, 8)
        ));
        f.setForeground(Theme.TEXT_BODY);
        // Placeholder
        f.setText(placeholder);
        f.setForeground(Theme.TEXT_MUTED);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(Theme.TEXT_BODY); }
            }
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(Theme.TEXT_MUTED); }
            }
        });
        return f;
    }

    /** Styled combo box. */
    protected JComboBox<String> buildCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(Theme.FONT_REGULAR);
        cb.setPreferredSize(new Dimension(150, Theme.INPUT_HEIGHT));
        cb.setBackground(Color.WHITE);
        return cb;
    }

    protected void showSuccess(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Berhasil", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    protected boolean confirmDelete() {
        return JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus data ini?", "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
    }
}
