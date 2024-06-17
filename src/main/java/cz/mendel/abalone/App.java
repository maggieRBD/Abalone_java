package cz.mendel.abalone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("board.fxml"));
        Parent root = fxmlLoader.load();
        root.setId("rootPane");
        Scene scene = new Scene(root, 1182, 1024);
        stage.setTitle("AbaloneFx");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}