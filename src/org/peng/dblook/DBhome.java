package org.peng.dblook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class DBhome extends Application {

    public static void main(String[] args) {
        launch(args);
        System.out.println("main.........");
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start....");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resource/DBf.fxml"));
        DBfcontroller controller =  loader.getController();
        Parent root = null;
        try {
            //root = FXMLLoader.load(getClass().getResource("/resource/DBf.fxml"));
            root = loader.load();
            primaryStage.setTitle("database look");
            primaryStage.setScene(new Scene(root, 600, 600));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
