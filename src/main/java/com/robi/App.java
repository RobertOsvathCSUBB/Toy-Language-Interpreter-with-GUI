package com.robi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import com.robi.models.state.PrgState;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Toy language program executor");
        stage.show();
    }

    public static void openProgramRunWindow(PrgState program) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("program.fxml"));
        Parent root = fxmlLoader.load();
        ProgramViewController newController = fxmlLoader.getController();
        newController.setProgram(program);
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("Program execution");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}