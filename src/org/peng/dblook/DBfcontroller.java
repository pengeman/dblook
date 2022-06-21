package org.peng.dblook;


import dbhelp.DataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @param
 * @version 1.0
 * @Description 住界面
 * @Author pengweitao 2022/4/28 下午11:05
 * @exception
 * @return
 */

public class DBfcontroller implements Initializable {
    @FXML
    private ToolBar toolBar;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button b_dbsource;
    @FXML
    private Button b_go;
    @FXML
    private Button b_notebook;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TextArea t_u;
    @FXML
    private TextArea t_d;
    @FXML
    private TableView table_rs;

    public void dbsource_clicked(ActionEvent event) {
        Parent root = null;
        Stage primaryStage = new Stage();
        try {
            URL url = getClass().getResource("/resource/dbsource.fxml");
            root = FXMLLoader.load(url);
            //root = loader.load();
            primaryStage.setTitle("database look");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        toolBar.prefWidthProperty().bind(pane.widthProperty());
        splitPane.prefWidthProperty().bind(pane.widthProperty());
        splitPane.prefHeightProperty().bind(pane.heightProperty());
        t_u.prefWidthProperty().bind(pane.widthProperty());
        t_u.prefHeightProperty().bind(pane.heightProperty());
        t_d.prefWidthProperty().bind(pane.widthProperty());
        t_d.prefHeightProperty().bind(pane.heightProperty());

        b_go.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bgo_clicked();
            }
        });
        b_dbsource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBfcontroller.this.dbsource_clicked(event);
            }
        });
        b_notebook.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                bnotebook_clicked();
            }
        });
    }

    private void bnotebook_clicked() {
        //org.peng.dblook.NoteDia(null,true)
        org.peng.dblook.NoteDia.main();
    }

    // 执行sql语句
    private void bgo_clicked() {
        String t_sql = this.t_u.getSelectedText();
        System.out.println(t_sql);

        DataSet ds = Common.dataBase.query(t_sql);
        List dsls = ds.generateList();

        // 显示到table里
        showtable(ds);
    }

    /**
     * 将dataset数据显示到table里
     *
     * @return
     */
    private void showtable(DataSet ds) {
        ObservableList<Map> data = FXCollections.observableArrayList();
        ;
        TableColumn t_col[];
        List<String> colNames = ds.getColNameSet();
        t_col = new TableColumn[colNames.size()];
        for (int i = 0; i < t_col.length; i++) {
            String colName = colNames.get(i);
            t_col[i] = new TableColumn(colName);
            t_col[i].setCellValueFactory(new MapValueFactory<String>(colName));
        }

        // 将ds中的数据写入ObservableList
        List<Map<String, Object>> rss = ds.getDataTable(); // 源头
        for (int i = 0; i < rss.size(); i++) {
            data.add(rss.get(i));
        }

        this.table_rs.getColumns().addAll(t_col);
        this.table_rs.setItems(data);
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    public void setToolBar(ToolBar toolBar) {
        this.toolBar = toolBar;
    }

    public AnchorPane getPane() {
        return pane;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }
}
