package ui.components;

import util.Theme;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*; 

public class StyledTable extends JTable {
    public StyledTable(TableModel model) {
        super(model);
        setupStyle();
    }
 
    public StyledTable() {
        super();
        setupStyle();
    }

    private void setupStyle() {
        setFont(Theme.FONT_REGULAR);
        setRowHeight(Theme.ROW_HEIGHT);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(Theme.BORDER_LIGHT);
        setSelectionBackground(Theme.PRIMARY_LIGHT);
        setSelectionForeground(Theme.TEXT_DARK);
        setBackground(Theme.BG_WHITE);
        setIntercellSpacing(new Dimension(0, 0));
        setFillsViewportHeight(true);
 
        // Header styling
        JTableHeader header = getTableHeader();
        header.setFont(new Font(Theme.FONT_NAME, Font.BOLD, 11));
        header.setBackground(Theme.BG_TABLE_HEADER);
        header.setForeground(Theme.TEXT_MUTED);
        header.setPreferredSize(new Dimension(0, 36));
        header.setReorderingAllowed(false);
 
        // Default renderer: left-aligned, proper padding
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(Theme.FONT_REGULAR);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (!isSelected) {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(0xFAFAFA));
                    setForeground(Theme.TEXT_BODY);
                }
                return this;
            }
        });
    }
    
}
