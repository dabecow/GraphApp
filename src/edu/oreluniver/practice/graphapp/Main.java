package edu.oreluniver.practice.graphapp;

import edu.oreluniver.practice.graphapp.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/MainView.fxml"));

        Parent root = loader.load();



        primaryStage.setTitle("Graph app");
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure? " +
                    "All the unsaved data will be lost.", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.NO)
                event.consume();
        });

        MainViewController controller = loader.getController();
        controller.setMainStage(primaryStage);

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
