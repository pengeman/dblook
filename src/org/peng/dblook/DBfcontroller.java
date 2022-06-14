package org.peng.dblook;

import dbhelp.Connection;
import dbhelp.DataSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @param
 * @version 1.0
 * @Description 住界面
 * @Author pengweitao 2022/4/28 下午11:05
 * @exception
 * @return
 */
@Log4j
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

    public void dbsource_clicked(ActionEvent event) {
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource/dbsource.fxml"));
        //DbsourceController controller =  loader.getController();
        Parent root = null;
        Stage primaryStage = new Stage();
        try {
            URL url = getClass().getResource("/resource/dbsource.fxml");
            root = FXMLLoader.load(url);
            //root = loader.load();
            primaryStage.setTitle("database look");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        toolBar.prefWidthProperty().bind(pane.widthProperty());
        splitPane.prefWidthProperty().bind(pane.widthProperty());

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

    private void bgo_clicked() {
String t_sql = this.t_u.getSelectedText();
log.debug(t_sql);
DataSet ds = Common.dataBase.query(t_sql);
ds.generateList()
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
