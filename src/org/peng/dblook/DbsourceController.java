package org.peng.dblook;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import lombok.extern.log4j.Log4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @param
 * @version 1.0
 * @Description
 * @Author pengweitao 2022/5/3 下午11:10
 * @exception
 * @return
 */
@Log4j
public class DbsourceController implements Initializable {

    private  String curProject; // 当前选中的工程名称

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
    Properties pro = new Properties();

    public DbsourceController() {
        this(1);
    }

    public DbsourceController(int index) {
        // 根据数据源序号,设置数据源序号
        this.dbsource_index = index;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            pro.load(new FileInputStream(this.getClass().getResource("/resource/dblook.properties").getFile()));

            //得到数据源properties文件,并显示列表
            List<String> projectlist = this.getAllProject();
            ObservableList<String> dblist = FXCollections.observableArrayList();

            for (String db : projectlist) {
                dblist.add(db);
            }

            this.listView.setItems(dblist);
            this.textArea.setText("hahahah");

            this.listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
                @Override
                public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                    // listView item被点击
                    log.debug(newValue);
                    DbsourceController.this.curProject = (String) newValue;
                }


            });
        }catch (java.io.IOException e){
            log.error("DbsourceController 初始化发生异常");
            log.error(e);
        }




    }

    private List getAllProject() {
        //String projects = dbhelpx.DBHelp.getInstanc().getProperties().getProperty("project");
        String projects = null;  // 所有的工程名称
        List prol = null;

        if (!pro.isEmpty()) {
            projects = pro.getProperty("project");
            String[] pros = projects.split(",");
            prol = new ArrayList();
            for (String prop : pros) {
                prol.add(prop);
            }
        }
            return prol;

    }

}