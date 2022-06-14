/*
 * 查询数据库得到的数据
保存有查询的列名，和查询的数据
 */
package dbhelpx;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 
 */
public class DataSet {
   private List<String> colName;  // 列名
   private List<Type> colType; // 列类型
   private Integer rowNum;  // 行数
   private List<Map<String,Object>> result; // 查询到的数据
   
   public DataSet(){}
   public DataSet(String str){
       // 只有一行字符串构造的dataset
       colName = new ArrayList<String>(1);
       colName.add("result");
       //colType = new ArrayList<Type>(1);
       rowNum = 1;
       result = new ArrayList(1);
       Map row = new HashMap();
       row.put("result", str);
       result.add(row);
   }
   
   /**
    * 得到查询到的行数
    * @return 
    */
   public Integer getRowNum(){
       return this.rowNum;
   }
   
   public void setRowNum(Integer rowNum){
       this.rowNum = rowNum;
   }

    /**
     * @return the colName
     */
    public List<String> getColName() {
        return colName;
    }

    /**
     * @param colName the colName to set
     */
    public void setColName(List<String> colName) {
        this.colName = colName;
    }

    /**
     * @return the colType
     */
    public List<Type> getColType() {
        return colType;
    }

    /**
     * @param colType the colType to set
     */
    public void setColType(List<Type> colType) {
        this.colType = colType;
    }

    /**
     * @return the result
     */
    public List<Map<String,Object>> getResult() {
        return result;
    }

    /**
     * @param result the result to set
     */
    public void setResult(List<Map<String,Object>> result) {
        this.result = result;
    }
   
}
