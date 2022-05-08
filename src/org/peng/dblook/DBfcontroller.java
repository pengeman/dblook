package org.peng.dblook;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
public class DBfcontroller implements Initializable {
    @FXML
    private ToolBar toolBar;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button b_dbsource;
    @FXML
    private SplitPane splitPane;


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

        b_dbsource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBfcontroller.this.dbsource_clicked(event);
            }
        });
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
