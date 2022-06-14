package dbhelpx;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 此类不供人员直接调用 数据库连接工具类
 *
 */
public abstract class DBHelp {

//    org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(DBHelp.class);

    private static DBHelp dbh;

    private static BasicDataSource ds = new BasicDataSource();
    private String driver;
    private String db;
    private String url;
    private String username;
    private String password;
    private String characterEncoding;
    private Properties properties;

    private DBHelp() {
    }

    public static DBHelp getInstanc(String driver) {
        if (dbh == null) {
            dbh = new DBHelp() {
            };
            dbh.DBHelp(driver);
        }
        return dbh;
    }

    public static DBHelp getInstanc() {
        if (dbh == null) {
            dbh = new DBHelp() {
            };
            dbh.DBHelp();
        }
        return dbh;
    }

    private void DBHelp(String driverName) {
        Properties properties = new Properties();
        try {

//            java.net.URL url = DBHelp.class.getClassLoader().getResource("resource/dblook.properties");
//            System.out.println(url);
            URL furl = getClass().getResource("/resource/dblook.properties");
//            System.out.println(DBHelp.class.getResource("dblook.properties"));
//            System.out.println(DBHelp.class.getResource("../resource/dblook.properties"));
//            System.out.println(DBHelp.class.getClassLoader().getResource("/resource/dblook.properties"));
//            System.out.println(getClass().getResourceAsStream("/resource/dblook.properties"));
//            System.out.println(DBHelp.class.getClassLoader().getResourceAsStream("resource/dblook.properties"));
            //URI uri = Paths.get("resource/dblook.properties").toAbsolutePath().toUri();
            System.out.println(furl);
            System.out.println("................................................");
            //properties.load(new java.io.FileInputStream(new java.io.File(url.toURI())));

            File file = new File(furl.toURI());
            properties.load(new java.io.FileInputStream(file));
            this.setProperties(properties);

            driver = properties.getProperty(driverName + ".driver");
            url = properties.getProperty(driverName + ".url");
            username = properties.getProperty(driverName + ".username");
            password = properties.getProperty(driverName + ".password");
            characterEncoding = properties.getProperty(driverName + ".characterEncoding");
            int InitialSize = Integer.parseInt(properties.getProperty(driverName + ".InitialSize"));
            int MaxActive = Integer.parseInt(properties.getProperty(driverName + ".MaxActive"));
            long MaxWait = Long.parseLong(properties.getProperty(driverName + ".MaxWait"));
            int MinIdle = Integer.parseInt(properties.getProperty(driverName + ".MinIdle"));

            ds.setDriverClassName(driver);
            ds.setUrl(url);
            ds.setUsername(username);
            ds.setPassword(password);
            ds.setInitialSize(InitialSize);
            ds.setMaxWait(MaxWait);
            ds.setMaxActive(MaxActive);
            ds.setMinIdle(MinIdle);
            //设置连接编码方式
            //ds.addConnectionProperty(name, value);
            //System.out.println("数据库初始化已完成.....");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DBHelp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DBHelp.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void DBHelp() {
        this.DBHelp("default");
    }

    public static void destroy() {
        dbh = null;
    }

    /**
     * 获取数据库连接对象
     *
     * @return
     */
    public Connection getConnection() {
        try {
            Connection conn = getDs().getConnection();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 执行非select insert update delete语句
     *
     * @param sql
     * @return
     */
    public DataSet execute(String sql) {
        boolean hasmore = true;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        QuerySet qs = null;
        DataSet ds = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);

            

            hasmore = stat.execute();
            if (hasmore) {
                rs = stat.getResultSet();
                qs = new QuerySet();
                qs.setResultSet(rs);
                ds = qs.getDataSet();
            }else{
                int i = stat.getUpdateCount();
                ds = new DataSet("影响了" + i + "条记录");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            ds = new DataSet(e.toString());
        } finally {
            close(stat, conn);
        }
        return ds;
    }

    /**
     * 执行insert update delete语句
     *
     * @param sql
     */
    public DataSet executeSQL(String sql, Object... args) {
        int result = 0;
        DataSet ds = null;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            System.out.println(stat.toString());
            result = stat.executeUpdate();
            ds = new DataSet("影响了数据库"+result+"条记录");
        } catch (SQLException e) {
            e.printStackTrace();
            ds = new DataSet(e.toString());
        } finally {
            close(stat, conn);
        }
        return ds;
    }

    /**
     * 查询select
     *
     * @throws SQLException
     */
    public DataSet executeQuery(String sql, Object... args) throws dbException {
        List list = null;
        ResultSet rs = null;
        QuerySet qs = null;
        DataSet ds = null;
        Connection conn = null;
        PreparedStatement stat = null;
        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            //System.out.println(stat.toString());
            System.out.println("executeQuery sql : " + sql);
            rs = stat.executeQuery();
            //list = resultSetToList(rs);
            qs = new QuerySet();
            qs.setResultSet(rs);
            ds = qs.getDataSet();
        } catch (SQLException e) {
            throw new dbException(e.getMessage());
        } finally {
            close(stat, conn);
        }
        return ds;
    }

    /**
     * 释放连接
     *
     * @param stat
     * @param conn
     */
    private void close(PreparedStatement stat, Connection conn) {
        close(null, stat, conn);
    }

    /**
     * 释放连接
     *
     * @param rs
     * @param stat
     * @param conn
     */
    private void close(ResultSet rs, PreparedStatement stat, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stat != null) {
                    stat.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将resultset转化为List
     *
     */
    private List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        if (rs == null) {
            return Collections.EMPTY_LIST;
        }
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        List list = new ArrayList();
        //将map放入集合中方便使用个别的查询
        Map rowData = new HashMap();
        while (rs.next()) {
            rowData = new LinkedHashMap(columnCount);
            //将集合放在map中
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
        return list;
    }

    /**
     * @return the ds
     */
    public static BasicDataSource getDs() {
        return ds;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return the characterEncoding
     */
    public String getCharacterEncoding() {
        return characterEncoding;
    }

    /**
     * @return the db
     */
    public String getDb() {
        return db;
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

}
