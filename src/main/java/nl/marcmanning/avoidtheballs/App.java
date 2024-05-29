package nl.marcmanning.avoidtheballs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Pane pane = new Pane();
        Scene scene = createFittingScene(pane, Constants.STANDARD_APP_WIDTH, Constants.STANDARD_APP_HEIGHT);
        Game game = new Game(pane);
        pane.requestFocus();
        pane.setOnKeyPressed(e -> {
            game.start();
        });
        stage.setOnCloseRequest(e -> {
            stage.setFullScreen(false);
            game.stop();
            Platform.exit();
        });
        stage.setScene(scene);
        stage.show();
    }

    private Scene createFittingScene(Pane pane, float prefWidth, float prefHeight) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        if (bounds.getWidth() < prefWidth || bounds.getHeight() < prefHeight) {
            return createFittingScene(pane, prefWidth / 1.5f, prefHeight / 1.5f);
        } else {
            return new Scene(pane, prefWidth, prefHeight);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}