package dbhelp;

import dbhelpx.DataSet;
import dbhelpx.QuerySet;
import oracle.net.ano.AuthenticationService;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.sql.Connection;
import java.util.*;

/**
 * @param
 * @version 1.0
 * @Description 数据库对象,用于执行sql语句
 * @Author pengweitao 2022/5/8 上午12:12
 * @exception
 * @return
 */


public class DataBase {
private dbhelp.Connection connection;


    /**
     * 正在链接的数据库的名称
     */
    private String databaseName;
    /**
     * 正在连接的数据库的用户名
     */
    private String useName;
    /**
     * 链接数据库时的链接参数
     **/
    private String url;


    public DataBase(String useName, String url, dbhelp.Connection con) {
        this.useName = useName;
        this.url = url;
        this.connection = con;
    }




    /**
     * 执行insert语句
     *
     * @param sql
     */
    public int insert(String sql) {
        // TODO: implement
        return 0;
    }

    /**
     * 执行update语句
     *
     * @param sql
     */
    public int update(String sql) {
        // TODO: implement
        return 0;
    }

    /**
     * 执行delete语句
     *
     * @param sql
     */
    public int delete(String sql) {
        // TODO: implement
        return 0;
    }

    /**
     * 执行select语句
     *
     * @param sql
     */
    public dbhelp.DataSet query(String sql) {
        dbhelp.DataSet dataSet = new dbhelp.DataSet();
        boolean hasmore = true;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        QuerySet qs = null;
        //dbhelpx.DataSet ds = null;
        try {
            conn = this.connection.getSqlConnection();
            stat = conn.prepareStatement(sql);

            hasmore = stat.execute();
            if (hasmore) {
                rs = stat.getResultSet();
                // 便利rs,取得数据,生成DataSet
                List<Map<String,Object>> dtable = this.fetchResultSet(rs);
                dataSet.setDataTable(dtable);
                return dataSet;
            }else{
                int i = stat.getUpdateCount();
                dataSet = null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            dataSet = null;
        } finally {
            close(stat, conn);
        }
        return dataSet;
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
     * 将查询到的结果集保存到属性dataTable中
     * List<Map<String, Object>> dataTable;
     * @param rs
     */
    private List<Map<String, Object>> fetchResultSet(ResultSet rs) throws SQLException {
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
     * 通过url获取数据库名称
     * @param jdbcUrl
     * @return
     */
    private static String findDataBaseNameByUrl(String jdbcUrl) {
        String database = null;
        int pos, pos1;
        String connUri;

        if (StringUtils.isBlank(jdbcUrl)) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        jdbcUrl = jdbcUrl.toLowerCase();

        if (jdbcUrl.startsWith("jdbc:impala")) {
            jdbcUrl = jdbcUrl.replace(":impala", "");
        }

        if (!jdbcUrl.startsWith("jdbc:")
                || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        connUri = jdbcUrl.substring(pos1 + 1);

        if (connUri.startsWith("//")) {
            if ((pos = connUri.indexOf('/', 2)) != -1) {
                database = connUri.substring(pos + 1);
            }
        } else {
            database = connUri;
        }

        if (database.contains("?")) {
            database = database.substring(0, database.indexOf("?"));
        }

        if (database.contains(";")) {
            database = database.substring(0, database.indexOf(";"));
        }

        if (StringUtils.isBlank(database)) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }
        return database;
    }



}