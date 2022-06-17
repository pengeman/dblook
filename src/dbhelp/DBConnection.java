package dbhelp;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.yaml.snakeyaml.Yaml;

/**
 * @param
 * @version 1.0
 * @Description 链接对象，用于链接数据库。
 * @Author pengweitao 2022/5/8 上午12:09
 * @exception
 * @return
 */


public class DBConnection {
    private java.sql.Connection sqlConnection;
    private BasicDataSource bds;
    private String driverName; // 驱动程序名称
    private String dbName;     // 数据库名称
    private String url;       //  数据库链接参数
    private String useName;   // 登录数据库用户名


    public java.sql.Connection getSqlConnection() {
        return sqlConnection;
    }

    public void setSqlConnection(java.sql.Connection sqlConnection) {
        this.sqlConnection = sqlConnection;
    }

    /**
     * getBasicDataSource
     *
     * @return
     */
    public BasicDataSource getBds() {
        return bds;
    }

    /**
     * setBaseicDataSource
     *
     * @param bds
     */
    public void setBds(BasicDataSource bds) {
        this.bds = bds;
    }

    public DBConnection() {
        this.sqlConnection = this.sqlConnection();
    }

    public DBConnection(String driver, String url, String username, String password) {
        this.sqlConnection = this.sqlConnection(driver, url, username, password);
    }

    private java.sql.Connection sqlConnection() {
        Properties pro = new Properties();
        pro = this.getConfig(); // 读取配置文件信息,保存到properties中

        String driver = pro.getProperty("driver");
        String url = pro.getProperty("url");
        String username = pro.getProperty("username");
        String password = pro.getProperty("password");
        return this.sqlConnection(driver, url, username, password);
    }

    private java.sql.Connection sqlConnection(String driver, String url, String username, String password) {
        String characterEncoding;
        Properties pro;
        java.sql.Connection con = null;
        try {
            int InitialSize = 1;
            int MaxActive = 2;
            long MaxWait = 1000;
            int MinIdle = 1;

            BasicDataSource ds = new BasicDataSource();
            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setInitialSize(InitialSize);
            ds.setMaxWait(MaxWait);
            ds.setMaxActive(MaxActive);
            ds.setMinIdle(MinIdle);
            con = ds.getConnection();
            this.bds = ds;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    public DataBase createDatabase() {
        DataBase dataBase = new DataBase(this.useName, this.url, this);
        return dataBase;
    }

    /**
     * 查找配置文件,查找顺序:properties,xml,yml,json,如果没有找到,返回null
     *
     * @return
     */
    private Properties getConfig() {
        File configFile = null;
        URL url = null;

        // 查找配置文件,查找顺序:properties,xml,yml,json
        int urlFlag = 0; // url文件的类型,0:properties,1:xml,2:yml,3:json
        url = this.getClass().getResource("/resource/sqlcofig.properties");
        if (url == null) {
            url = this.getClass().getResource("/resource/sqlconfig.xml");
            urlFlag = 1;
            if (url == null) {
                url = this.getClass().getResource("/resource/sqlconfig.yml");
                urlFlag = 2;
            }
            if (url == null) {
                url = this.getClass().getResource("/resource/sqlconfig.json");
                urlFlag = 3;
            }
        }
        if (url == null)
            return null;
        else {
            // 解析url
            return parseURL(url, urlFlag);
        }
    }

    private Properties parseURL(URL url, int urlFlag) {
        Properties pro = new Properties();
        File configFile = new File(url.toString());
        dbhelp.YamlUtil yamlUtil = new YamlUtil();
        try {
            switch (urlFlag) {
                case 0: // properties
                    pro.load(new FileInputStream(configFile));
                    break;
                case 1: // xml
                    pro.loadFromXML(new FileInputStream(configFile));
                    break;
                case 2: // yml
                    Yaml yml = new Yaml();
                    //HashMap  ret = yml.load(this.getClass().getClassLoader().getResourceAsStream(url.toString()));
                    pro = yamlUtil.toProperties(url.getFile());
                    break;
                case 3: // json                   //JSON to Map
                    /*String mapJson = JSONObject.toJSONString(map);
                    Map maps = (Map)JSON.parse(mapJson);*/
                    Map<String, String> ymlmap = yamlUtil.getPropertiesMap(url.toString());
                    Properties finalPro = pro;
                    ymlmap.forEach((k, v) -> {
                        finalPro.setProperty(k, v);
                    });
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + urlFlag);
            }
            return pro;
        } catch (java.io.IOException e) {

        }
        return null;
    }

    private void sa() {
        Properties pro = this.getConfig();
        System.out.println(pro);
    }

    public static void main(String[] args) {
        DBConnection con = new DBConnection();
        con.sa();
    }
}