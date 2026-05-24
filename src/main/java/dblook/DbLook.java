package dblook;

import dblook.model.ConnectionInfo;
import dblook.model.ConnectionManager;
import dblook.ui.ConnectionDialog;
import dblook.ui.NoteWindow;
import dblook.ui.SqlEditorPanel;
import dblook.ui.SqlResultPanel;
import dbhelp.DBConnection;
import dbhelp.DataBase;
import dbhelp.DataSet;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbLook extends JFrame {

    private JTree connectionTree;
    private DefaultTreeModel treeModel;
    private JTabbedPane sqlTabbedPane;
    private ConnectionManager connectionManager;
    private Map<String, DBConnection> activeConnections;

    public DbLook() {
        super("DBLook - 数据库管理工具");
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectionManager = new ConnectionManager();
        activeConnections = new HashMap<>();

        initUI();
    }

    private void initUI() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton connectBtn = new JButton("连接数据库");
        JButton executeBtn = new JButton("执行");
        JButton noteBtn = new JButton("笔记");

        connectBtn.addActionListener(e -> onConnect());
        executeBtn.addActionListener(e -> onExecuteCurrentTab());
        noteBtn.addActionListener(e -> onNote());

        toolBar.add(connectBtn);
        toolBar.add(executeBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(noteBtn);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);

        splitPane.setLeftComponent(buildLeftPanel());
        splitPane.setRightComponent(buildRightPanel());

        add(toolBar, BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);

        refreshTree();
    }

    private JPanel buildLeftPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("数据库树形结构"));

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("数据库");
        treeModel = new DefaultTreeModel(root);
        connectionTree = new JTree(treeModel);
        connectionTree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showTreeContextMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showTreeContextMenu(e);
                }
            }
        });

        panel.add(new JScrollPane(connectionTree), BorderLayout.CENTER);
        return panel;
    }

    private JComponent buildRightPanel() {
        sqlTabbedPane = new JTabbedPane();
        addNewTab();

        return sqlTabbedPane;
    }

    private void addNewTab() {
        JPanel panel = new JPanel(new BorderLayout());
        SqlEditorPanel editorPanel = new SqlEditorPanel(false);
        SqlResultPanel resultPanel = new SqlResultPanel();

        panel.add(editorPanel, BorderLayout.CENTER);
        panel.add(resultPanel, BorderLayout.SOUTH);

        int index = sqlTabbedPane.getTabCount();
        sqlTabbedPane.addTab("SQL " + (index + 1), panel);

        JPanel tabHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        tabHeader.add(new JLabel("SQL " + (index + 1)));
        JButton closeBtn = new JButton("×");
        closeBtn.setBorderPainted(false);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setPreferredSize(new Dimension(16, 16));
        closeBtn.addActionListener(e -> {
            int tabIndex = sqlTabbedPane.indexOfComponent(panel);
            if (tabIndex >= 0) {
                sqlTabbedPane.remove(tabIndex);
            }
        });
        tabHeader.add(closeBtn);
        tabHeader.setOpaque(false);
        sqlTabbedPane.setTabComponentAt(index, tabHeader);
    }

    private void refreshTree() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        root.removeAllChildren();

        for (ConnectionInfo info : connectionManager.getConnections()) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(info);
            root.add(node);
            if (info.isConnected()) {
                DBConnection dbConn = activeConnections.get(info.getId());
                if (dbConn != null) {
                    loadTables(dbConn, node);
                }
            }
        }

        JButton addTabBtn = new JButton("+");
        addTabBtn.addActionListener(e -> addNewTab());
        treeModel.reload();
    }

    private void loadTables(DBConnection dbConn, DefaultMutableTreeNode parent) {
        try {
            DataBase db = new DataBase("", "", dbConn);
            DataSet ds = db.query("SHOW TABLES");
            if (ds != null && ds.getDataTable() != null) {
                for (Map<String, Object> row : ds.getDataTable()) {
                    String tableName = row.values().iterator().next().toString();
                    parent.add(new DefaultMutableTreeNode(tableName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showTreeContextMenu(MouseEvent e) {
        TreePath path = connectionTree.getPathForLocation(e.getX(), e.getY());
        if (path == null) return;

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (!(node.getUserObject() instanceof ConnectionInfo)) return;

        ConnectionInfo info = (ConnectionInfo) node.getUserObject();

        JPopupMenu menu = new JPopupMenu();

        JMenuItem connectItem = new JMenuItem("连接");
        JMenuItem viewTablesItem = new JMenuItem("查看表");
        JMenuItem disconnectItem = new JMenuItem("断开");
        JMenuItem deleteItem = new JMenuItem("删除");

        if (info.isConnected()) {
            connectItem.setEnabled(false);
        } else {
            viewTablesItem.setEnabled(false);
            disconnectItem.setEnabled(false);
        }

        connectItem.addActionListener(ev -> connectAndRefresh(info));
        viewTablesItem.addActionListener(ev -> {
            node.removeAllChildren();
            DBConnection dbConn = activeConnections.get(info.getId());
            if (dbConn != null) {
                loadTables(dbConn, node);
                treeModel.reload(node);
                connectionTree.expandPath(path);
            }
        });
        disconnectItem.addActionListener(ev -> {
            connectionManager.disconnect(info.getId());
            DBConnection dbConn = activeConnections.remove(info.getId());
            if (dbConn != null) {
                dbConn.close();
                dbConn.closeDataSource();
            }
            refreshTree();
        });
        deleteItem.addActionListener(ev -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "确定删除此连接？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                connectionManager.removeConnection(info.getId());
                connectionManager.save();
                refreshTree();
            }
        });

        menu.add(connectItem);
        menu.add(viewTablesItem);
        menu.add(disconnectItem);
        menu.addSeparator();
        menu.add(deleteItem);

        menu.show(connectionTree, e.getX(), e.getY());
    }

    private void onConnect() {
        ConnectionDialog dialog = new ConnectionDialog(this);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            ConnectionInfo info = dialog.getConnectionInfo();
            info.setName(info.getName() != null && !info.getName().isEmpty() ? info.getName() : info.getUrl());
            connectAndRefresh(info);
        }
    }

    private void connectAndRefresh(ConnectionInfo info) {
        try {
            DBConnection dbConn = new DBConnection(info.getDriver(), info.getUrl(), info.getUsername(), info.getPassword());
            if (dbConn.getSqlConnection() != null) {
                connectionManager.disconnectAll();
                info.setConnected(true);
                activeConnections.put(info.getId(), dbConn);

                if (info.getId() == null || connectionManager.getConnections().stream().noneMatch(c -> c.getId().equals(info.getId()))) {
                    connectionManager.addConnection(info);
                } else {
                    connectionManager.updateConnection(info);
                }
                connectionManager.save();
                refreshTree();
                JOptionPane.showMessageDialog(this, "连接成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "连接失败，请检查驱动、URL、用户名和密码是否正确", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "连接失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onExecuteCurrentTab() {
        int index = sqlTabbedPane.getSelectedIndex();
        if (index >= 0) {
            JPanel panel = (JPanel) sqlTabbedPane.getComponentAt(index);
            for (Component comp : panel.getComponents()) {
                if (comp instanceof SqlEditorPanel) {
                    onExecute((SqlEditorPanel) comp);
                    break;
                }
            }
        }
    }

    private void onExecute(SqlEditorPanel editorPanel) {
        String sql = editorPanel.getSql();
        if (sql.isEmpty()) {
            JOptionPane.showMessageDialog(this, "SQL 语句不能为空", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ConnectionInfo connected = connectionManager.getConnectedConnection();
        if (connected == null) {
            JOptionPane.showMessageDialog(this, "请先连接数据库", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DBConnection dbConn = activeConnections.get(connected.getId());
        if (dbConn == null) {
            JOptionPane.showMessageDialog(this, "数据库连接已断开，请重新连接", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = sqlTabbedPane.getSelectedIndex();
        JPanel panel = (JPanel) sqlTabbedPane.getComponentAt(index);
        SqlResultPanel resultPanel = null;
        for (Component comp : panel.getComponents()) {
            if (comp instanceof SqlResultPanel) {
                resultPanel = (SqlResultPanel) comp;
                break;
            }
        }

        if (resultPanel != null) {
            resultPanel.setLoading(true);
        }

        final SqlResultPanel finalResultPanel = resultPanel;
        final ConnectionInfo finalConnected = connected;
        final DBConnection finalDbConn = dbConn;

        new Thread(() -> {
            try {
                DataBase db = new DataBase(finalConnected.getUsername(), finalConnected.getUrl(), finalDbConn);
                String trimmedSql = sql.trim().toUpperCase();

                if (trimmedSql.startsWith("SELECT") || trimmedSql.startsWith("SHOW") || trimmedSql.startsWith("DESCRIBE") || trimmedSql.startsWith("EXPLAIN")) {
                    DataSet ds = db.query(sql);
                    if (ds != null && ds.getDataTable() != null) {
                        List<String> headers = new ArrayList<>();
                        if (!ds.getDataTable().isEmpty()) {
                            headers.addAll(ds.getDataTable().get(0).keySet());
                        }
                        List<List<Object>> data = new ArrayList<>();
                        for (Map<String, Object> row : ds.getDataTable()) {
                            List<Object> rowData = new ArrayList<>();
                            for (String header : headers) {
                                rowData.add(row.get(header));
                            }
                            data.add(rowData);
                        }
                        if (finalResultPanel != null) {
                            finalResultPanel.setData(headers, data);
                        }
                    } else {
                        if (finalResultPanel != null) {
                            finalResultPanel.setEmpty();
                        }
                    }
                } else {
                    int affected = db.execute(sql);
                    JOptionPane.showMessageDialog(DbLook.this, "执行成功，影响 " + affected + " 行", "成功", JOptionPane.INFORMATION_MESSAGE);
                    if (finalResultPanel != null) {
                        finalResultPanel.setEmpty();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(DbLook.this, "执行失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } finally {
                if (finalResultPanel != null) {
                    finalResultPanel.setLoading(false);
                }
            }
        }).start();
    }

    private void onNote() {
        NoteWindow noteWindow = new NoteWindow();
        noteWindow.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DbLook().setVisible(true);
        });
    }
}
