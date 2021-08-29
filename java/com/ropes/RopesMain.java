package com.ropes;

import com.ropes.controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.sql.SQLException;

public class RopesMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
        } catch (Exception ex){
            System.out.println(ex);
        }
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        System.out.println(fxmlLoader);
        Parent root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        Scene scene = new Scene(root);
        KeyCombination keyCombinationMacSave = new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN);
        KeyCombination keyCombinationMacNew = new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN);
        KeyCombination keyCombinationMacCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN);
        KeyCombination keyCombinationMacDate = new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN);
        KeyCombination keyCombinationMacSearch = new KeyCodeCombination(KeyCode.F, keyCombinationMacDate.SHORTCUT_DOWN);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if(keyCombinationMacSave.match(event)){
                try {
                    controller.handleSavePost();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(keyCombinationMacNew.match(event)){
                try {
                    controller.handleInsertPost();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(keyCombinationMacCopy.match(event)){
                try {
                    controller.handleClip();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(keyCombinationMacDate.match(event)){
                try {
                    controller.handleInsertDate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else if(keyCombinationMacSearch.match(event)){
                controller.handleVisibleSearchArea();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String ...args){
        launch(args);
    }
}
