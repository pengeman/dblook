package dblook.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * SQL 执行结果面板
 */
public class SqlResultPanel extends JPanel {

    private JTextArea statusArea;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SqlResultPanel() {
        setLayout(new BorderLayout());

        statusArea = new JTextArea(2, 40);
        statusArea.setEditable(false);
        statusArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane statusScroll = new JScrollPane(statusArea);
        statusScroll.setPreferredSize(new Dimension(400, 40));

        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        resultTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane tableScroll = new JScrollPane(resultTable);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, statusScroll, tableScroll);
        splitPane.setDividerLocation(40);
        splitPane.setResizeWeight(0);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * 显示查询结果
     */
    public void showResult(java.util.List<java.util.Map<String, Object>> dataTable, java.util.List<String> colNames, String message) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);

        if (dataTable != null && !dataTable.isEmpty()) {
            if (colNames != null) {
                tableModel.setColumnIdentifiers(colNames.toArray());
            }
            for (java.util.Map<String, Object> row : dataTable) {
                Object[] rowData = new Object[tableModel.getColumnCount()];
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    String col = (String) tableModel.getColumnName(i);
                    rowData[i] = row.get(col);
                }
                tableModel.addRow(rowData);
            }
        }

        statusArea.setText(message != null ? message : "");
    }

    /**
     * 显示状态消息（DDL 等非查询结果）
     */
    public void showMessage(String message) {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        statusArea.setText(message);
    }

    public void clear() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        statusArea.setText("");
    }

    public void setLoading(boolean loading) {
        if (loading) {
            statusArea.setText("正在执行...");
        }
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

        statusArea.setText("查询完成，共 " + data.size() + " 行");
    }

    public void setEmpty() {
        tableModel.setRowCount(0);
        tableModel.setColumnCount(0);
        statusArea.setText("");
    }
}
