package org.peng.dblook;
/**
 * 主程序，启动从这里开始
 */

import org.peng.dblook.Common;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class DBhome extends Application {

    public static void main(String[] args) {
        Properties p = System.getProperties();
        Common.userdir = p.getProperty("user.dir");
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

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    private static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
