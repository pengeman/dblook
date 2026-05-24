package dblook.ui;

import dblook.model.ConnectionInfo;

import javax.swing.*;
import java.awt.*;

/**
 * 数据库连接对话框
 */
public class ConnectionDialog extends JDialog {

    private JTextField nameField;
    private JComboBox<String> typeCombo;
    private JTextField driverField;
    private JTextField urlField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ConnectionInfo result;
    private boolean confirmed;

    private static final String[][] DB_TYPES = {
            {"MySQL", "com.mysql.cj.jdbc.Driver", "jdbc:mysql://host:3306/database"},
            {"SQLite", "org.sqlite.JDBC", "jdbc:sqlite:/path/to/database.db"},
            {"SQL Server", "com.microsoft.sqlserver.jdbc.SQLServerDriver", "jdbc:sqlserver://host:1433;databaseName=database"},
            {"Oracle", "oracle.jdbc.OracleDriver", "jdbc:oracle:thin:@host:1521:database"},
            {"PostgreSQL", "org.postgresql.Driver", "jdbc:postgresql://host:5432/database"},
            {"H2", "org.h2.Driver", "jdbc:h2:~/test"},
            {"其他", "", ""}
    };

    public ConnectionDialog(Frame parent) {
        this(parent, null);
    }

    public ConnectionDialog(Frame parent, ConnectionInfo editInfo) {
        super(parent, "连接数据库", true);
        this.confirmed = false;
        this.result = null;
        initComponents(editInfo);
    }

    private void initComponents(ConnectionInfo editInfo) {
        setLayout(new BorderLayout(10, 10));
        setSize(500, 320);
        setLocationRelativeTo(getParent());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        addField(formPanel, gbc, "名称:", nameField = new JTextField(25), 0);
        addField(formPanel, gbc, "类型:", typeCombo = buildTypeCombo(), 1);
        addField(formPanel, gbc, "驱动:", driverField = new JTextField(25), 2);
        addField(formPanel, gbc, "URL:", urlField = new JTextField(25), 3);
        addField(formPanel, gbc, "用户名:", usernameField = new JTextField(25), 4);
        addField(formPanel, gbc, "密码:", passwordField = new JPasswordField(25), 5);

        add(formPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okBtn = new JButton("确定");
        JButton cancelBtn = new JButton("取消");
        okBtn.setPreferredSize(new Dimension(80, 28));
        cancelBtn.setPreferredSize(new Dimension(80, 28));

        okBtn.addActionListener(e -> onOk());
        cancelBtn.addActionListener(e -> onCancel());

        btnPanel.add(okBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        if (editInfo != null) {
            loadInfo(editInfo);
        } else {
            typeCombo.setSelectedIndex(0);
            onDbTypeChanged();
        }
    }

    private JComboBox<String> buildTypeCombo() {
        JComboBox<String> combo = new JComboBox<>();
        for (String[] db : DB_TYPES) {
            combo.addItem(db[0]);
        }
        combo.addActionListener(e -> onDbTypeChanged());
        return combo;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        // 标签列
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(65, 25));
        panel.add(label, gbc);

        // 输入框列
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private void onDbTypeChanged() {
        int idx = typeCombo.getSelectedIndex();
        if (idx >= 0 && idx < DB_TYPES.length) {
            driverField.setText(DB_TYPES[idx][1]);
            urlField.setText(DB_TYPES[idx][2]);
        }
    }

    private void loadInfo(ConnectionInfo info) {
        nameField.setText(info.getName());
        for (int i = 0; i < DB_TYPES.length; i++) {
            if (DB_TYPES[i][0].equals(info.getDbType())) {
                typeCombo.setSelectedIndex(i);
                break;
            }
        }
        driverField.setText(info.getDriver());
        urlField.setText(info.getUrl());
        usernameField.setText(info.getUsername());
        passwordField.setText(info.getPassword());
    }

    private void onOk() {
        String name = nameField.getText().trim();
        String dbType = (String) typeCombo.getSelectedItem();
        String driver = driverField.getText().trim();
        String url = urlField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (driver.isEmpty() || url.isEmpty()) {
            JOptionPane.showMessageDialog(this, "驱动和URL不能为空", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.result = new ConnectionInfo(null, name, dbType, driver, url, username, password);
        this.confirmed = true;
        dispose();
    }

    private void onCancel() {
        this.confirmed = false;
        this.result = null;
        dispose();
    }

    public ConnectionInfo getConnectionInfo() {
        return result;
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
