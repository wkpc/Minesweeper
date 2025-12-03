package minesweeper.minesweeper6;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);  //width and height
        stage.setTitle("Minesweeper");
        stage.initStyle(StageStyle.DECORATED);        //removes the windows bar at the top
        stage.setMinWidth(700);
        stage.setMinHeight(300);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}