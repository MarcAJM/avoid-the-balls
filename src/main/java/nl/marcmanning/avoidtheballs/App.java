package nl.marcmanning.avoidtheballs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
    private Game game;

    @Override
    public void start(Stage stage) {
        Scene scene = createGameScene();
        stage.setOnCloseRequest(e -> {
            stage.setFullScreen(false);
            game.terminate();
            Platform.exit();
        });
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    private Scene createGameScene() {
        Pane pane = new Pane();
        Scene scene = createFittingScene(pane, Constants.STANDARD_APP_WIDTH, Constants.STANDARD_APP_HEIGHT);
        game = new Game(pane);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                game.start();
            }
        });
        return scene;
    }

    private Scene createFittingScene(Pane pane, double prefWidth, double prefHeight) {
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