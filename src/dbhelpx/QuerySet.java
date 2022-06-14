/*
 * executeQuery 执行后，得到的查询结果集
 */
package dbhelpx;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pengweitao
 */
public class QuerySet {

    private ResultSet resultSet;
    private DataSet dataset = null;
    
    public DataSet getDataSet() {
        if (dataset == null)
            this.dataset = this.resultSetToDataSet(resultSet);
        
        return dataset;
    }

    

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

    private DataSet resultSetToDataSet(ResultSet rs) {
        DataSet ds = new DataSet();
        List<String> colNames = null;
        int rowNum = 0;
        
        try {
            
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            List list = new ArrayList();
            List<String> colNameList = new ArrayList();
//            List<Type> colTypeList = new ArrayList();

            for (int i = 1; i <= columnCount; i++) {
                String colName = rsmd.getColumnName(i);
                colNameList.add(colName);
            }
            ds.setColName(colNameList);
            
            //将map放入集合中
            Map rowData = new HashMap();
            while (rs.next()) {
                rowNum ++;
                //将集合放在map中
                rowData = new LinkedHashMap(columnCount);
                //将集合放在map中
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(rsmd.getColumnName(i), rs.getObject(i));
                }
                list.add(rowData);
            }
            ds.setResult(list);
            ds.setRowNum(rowNum);
            this.dataset =  ds;
            
        } catch (SQLException ex) {
            Logger.getLogger(QuerySet.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        return this.dataset;
    }

     
    /**
     * @param resultSet the resultSet to set
     */
    public void setResultSet(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

}
