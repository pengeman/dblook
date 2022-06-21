package dbhelp;

/**
 * @param
 * @version 1.0
 * @Description
 * @Author pengweitao 2022/5/8 上午12:16
 * @exception
 * @return
 ***********************************************************************/


import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 数据集，保存查询到的数据，包括数据集，列集，记录数，可选换成List形式的数据集
 */
public class DataSet {
    /**
     * 查询到的数据的列名称
     */
    private List colNameSet;
    /**
     * 保存查询到的数据
     */
    private List<Map<String, Object>> dataTable;
    /**
     * 查询的数据记录数
     */
    private int rowNum;


    public List generateList() {
        // TODO: implement
        return null;
    }

    /**
     * 得到列名称集合
     * @return
     */
    public List getColNameSet() {
        return colNameSet;
    }

    public void setColNameSet(List colNameSet) {
        this.colNameSet = colNameSet;
    }

    public List<Map<String, Object>> getDataTable() {
        return dataTable;
    }

    public void setDataTable(List<Map<String, Object>> dataTable) {
        this.dataTable = dataTable;
        if (dataTable.size() > 0){
            Set colnames = dataTable.get(0).keySet();
            this.setColNameSet(new ArrayList(colnames));  //  生成列名集合
            this.setRowNum(dataTable.size());
        }else{
            this.setColNameSet(null);
            this.setRowNum(0);
        }
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowNum(int rowNum) {
        this.rowNum = rowNum;
    }
}