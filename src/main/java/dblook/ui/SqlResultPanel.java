package dblook.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * SQL 执行结果面板
 */
public class SqlResultPanel extends JPanel {

    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SqlResultPanel() {
        setLayout(new BorderLayout());
        
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resultTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane tableScroll = new JScrollPane(resultTable);
        add(tableScroll, BorderLayout.CENTER);
    }

    public void setData(java.util.List<String> headers, java.util.List<java.util.List<Object>> data) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (headers != null && !headers.isEmpty()) {
            tableModel.setColumnIdentifiers(headers.toArray());
        }

        if (data != null) {
            for (java.util.List<Object> row : data) {
                tableModel.addRow(row.toArray());
            }
        }
    }

    public void setEmpty() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
    }

    public void clear() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
    }
}
