package dblook.model;

/**
 * 数据库连接信息
 */
public class ConnectionInfo {

    private String id;
    private String name;
    private String dbType;
    private String driver;
    private String url;
    private String username;
    private String password;
    private boolean connected;

    public ConnectionInfo() {
    }

    public ConnectionInfo(String id, String name, String dbType, String driver, String url, String username, String password) {
        this.id = id;
        this.name = name;
        this.dbType = dbType;
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        this.connected = false;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    @Override
    public String toString() {
        return name != null ? name : url;
    }
}
