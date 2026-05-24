package dblook.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 连接管理器：负责连接信息的增删改查和持久化
 */
public class ConnectionManager {

    private static final String DATA_FILE = "resources/connections.json";

    private List<ConnectionInfo> connections;
    private final Gson gson;

    public ConnectionManager() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.connections = new ArrayList<>();
        load();
    }

    public List<ConnectionInfo> getConnections() {
        return connections;
    }

    /**
     * 添加连接信息
     */
    public void addConnection(ConnectionInfo info) {
        if (info.getId() == null || info.getId().isEmpty()) {
            info.setId(UUID.randomUUID().toString());
        }
        connections.add(info);
        save();
    }

    /**
     * 删除连接信息
     */
    public void removeConnection(String id) {
        connections.removeIf(c -> c.getId().equals(id));
        save();
    }

    /**
     * 更新连接信息
     */
    public void updateConnection(ConnectionInfo info) {
        for (int i = 0; i < connections.size(); i++) {
            if (connections.get(i).getId().equals(info.getId())) {
                connections.set(i, info);
                break;
            }
        }
        save();
    }

    /**
     * 断开所有连接
     */
    public void disconnectAll() {
        for (ConnectionInfo c : connections) {
            c.setConnected(false);
        }
    }

    /**
     * 断开指定连接
     */
    public void disconnect(String id) {
        for (ConnectionInfo c : connections) {
            if (c.getId().equals(id)) {
                c.setConnected(false);
                break;
            }
        }
    }

    /**
     * 获取已连接的连接
     */
    public ConnectionInfo getConnectedConnection() {
        for (ConnectionInfo c : connections) {
            if (c.isConnected()) {
                return c;
            }
        }
        return null;
    }

    /**
     * 保存连接信息到文件
     */
    public void save() {
        try {
            String filePath = resolveDataPath();
            File file = new File(filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            // 保存前断开标记（不保存连接状态）
            List<ConnectionInfo> toSave = new ArrayList<>();
            for (ConnectionInfo c : connections) {
                ConnectionInfo copy = new ConnectionInfo(
                        c.getId(), c.getName(), c.getDbType(), c.getDriver(),
                        c.getUrl(), c.getUsername(), c.getPassword());
                toSave.add(copy);
            }

            try (Writer writer = new OutputStreamWriter(
                    new FileOutputStream(file), StandardCharsets.UTF_8)) {
                gson.toJson(toSave, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从文件加载连接信息
     */
    @SuppressWarnings("unchecked")
    public void load() {
        try {
            String filePath = resolveDataPath();
            File file = new File(filePath);
            if (!file.exists()) {
                return;
            }

            try (Reader reader = new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8)) {
                Type type = new TypeToken<List<ConnectionInfo>>() {
                }.getType();
                List<ConnectionInfo> loaded = gson.fromJson(reader, type);
                if (loaded != null) {
                    this.connections = loaded;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析数据文件路径，优先使用 resources/ 目录
     */
    private String resolveDataPath() {
        String filePath = DATA_FILE;

        // 尝试当前目录
        File file = new File(filePath);
        if (file.exists()) {
            return filePath;
        }

        // 尝试 classpath
        java.net.URL url = getClass().getClassLoader().getResource(filePath);
        if (url != null) {
            return url.getPath();
        }

        // 默认返回
        return filePath;
    }
}
