package dblook.ui;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 笔记窗口 - 无模态独立窗口
 */
public class NoteWindow extends JFrame {

    private JTextArea textArea;
    private static final String NOTE_FILE = ".dblook_notes.txt";

    public NoteWindow() {
        super("笔记");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText(loadNotes());
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeBtn = new JButton("关闭");
        closeBtn.setPreferredSize(new Dimension(80, 28));
        closeBtn.addActionListener(e -> onClose());

        btnPanel.add(closeBtn);

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private String loadNotes() {
        File file = new File(System.getProperty("user.home"), NOTE_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    private void saveNotes() {
        File file = new File(System.getProperty("user.home"), NOTE_FILE);
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(textArea.getText());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "保存失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void onClose() {
        saveNotes();
        dispose();
    }
}
