package org.peng.dblook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @param
 * @version 1.0
 * @Description
 * @Author pengweitao 2022/5/3 下午11:10
 * @exception
 * @return
 */
public class DbsourceController implements Initializable {
    @FXML
    private TextArea textArea;
    @FXML
    private Button b_cancel;
    @FXML
    private Button b_confirm;
    @FXML
    private ListView listView;
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane parentPane;

    private int dbsource_index;
public DbsourceController(){
    this(1);
}
    public DbsourceController(int index){
        // 根据数据源序号,设置数据源序号
        this.dbsource_index = index;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
 //得到数据源properties文件,并显示列表
        List<String> projectlist = this.getAllProject();
        ObservableList<String> dblist = FXCollections.observableArrayList();

        for (String db : projectlist){
            dblist.add(db);
        }

        this.listView.setItems(dblist);
        this.textArea.setText("hahahah");

    }

    private List getAllProject(){
        String projects = dbhelpx.DBHelp.getInstanc().getProperties().getProperty("project");
        String[] pros = projects.split(",");
        List prol = new ArrayList();
        for(String pro : pros){
            prol.add(pro);
        }
        return prol;
    }

}
