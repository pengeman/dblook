package dblook.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * SQL 编辑器面板
 */
public class SqlEditorPanel extends JPanel {

    private JTextArea sqlArea;
    private JButton executeBtn;

    public SqlEditorPanel(boolean showExecuteBtn) {
        setLayout(new BorderLayout());

        sqlArea = new JTextArea();
        sqlArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        sqlArea.setLineWrap(false);
        JScrollPane scrollPane = new JScrollPane(sqlArea);

        if (showExecuteBtn) {
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            executeBtn = new JButton("执行");
            btnPanel.add(executeBtn);
            add(btnPanel, BorderLayout.NORTH);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    public String getSql() {
        String selected = sqlArea.getSelectedText();
        if (selected != null && !selected.trim().isEmpty()) {
            return selected.trim();
        }
        return sqlArea.getText().trim();
    }

    public void setSql(String sql) {
        sqlArea.setText(sql);
    }

    public void setExecuteAction(ActionListener action) {
        executeBtn.addActionListener(action);
    }
}
