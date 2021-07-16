/*
 * Copyright (c) 2021  Dinusha Jayawardena. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.prefs.Preferences;

public class AppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        AnchorPane root = FXMLLoader.load(this.getClass().getResource("/view/EditorForm.fxml"));
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Simple Text Editor");
        //primaryStage.setMaximized(false);
       // primaryStage.setMaximized(true);
        // primaryStage.setResizable(false);
        //primaryStage.centerOnScreen();
        primaryStage.show();

        double width = Preferences.userRoot().node("simple-text-editor").getDouble("width", -1);
        double height = Preferences.userRoot().node("simple-text-editor").getDouble("height", -1);

        if (width == -1 && height == -1){
            primaryStage.setMaximized(true);
        }else {
            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
        }

        double xPos = Preferences.userRoot().node("simple-text-editor").getDouble("xPos", -1);
        double yPos = Preferences.userRoot().node("simple-text-editor").getDouble("yPos", -1);

        if (xPos == -1 && yPos == -1){
            primaryStage.centerOnScreen();
        }else {
            primaryStage.setX(xPos);
            primaryStage.setY(yPos);
        }

        primaryStage.setOnCloseRequest(event -> {
            Preferences.userRoot().node("simple-text-editor").putDouble("xPos", primaryStage.getX());
            Preferences.userRoot().node("simple-text-editor").putDouble("yPos", primaryStage.getY());
            Preferences.userRoot().node("simple-text-editor").putDouble("width", primaryStage.getWidth());
            Preferences.userRoot().node("simple-text-editor").putDouble("height", primaryStage.getHeight());
        });
    }
}
