package nl.marcmanning.avoidtheballs;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application implements Listener {
    private Game game;
    private Scene scene;

    private Pane gamePane;
    private BorderPane postPane;


    @Override
    public void start(Stage stage) {
        BorderPane prePane = createPrePane();
        postPane = new BorderPane();
        gamePane = new Pane();
        game = new Game(gamePane, this);
        scene = createFittingScene(prePane, Constants.STANDARD_APP_WIDTH, Constants.STANDARD_APP_HEIGHT);
        initHandlers(scene, prePane, postPane);
        stage.setOnCloseRequest(e -> {
            stage.setFullScreen(false);
            game.stop();
            Platform.exit();
        });
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    @Override
    public void update(long value) {
        setPostPaneScore(postPane, value);
        scene.setRoot(postPane);
        postPane.requestFocus();
    }

    private void initHandlers(Scene scene, Pane prePane, Pane postPane) {
        prePane.requestFocus();
        prePane.setOnKeyPressed(event -> {
            scene.setRoot(gamePane);
            gamePane.requestFocus();
            game.start();
        });
        addRestarterKey(gamePane, scene, prePane);
        addRestarterKey(postPane, scene, prePane);
    }

    private void addRestarterKey(Pane pane, Scene scene, Pane prePane) {
        pane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.R) {
                game.stop();
                scene.setRoot(prePane);
                prePane.requestFocus();
                gamePane = new Pane();
                addRestarterKey(gamePane, scene, prePane);
                game = new Game(gamePane, this);
            }
        });
    }

    private BorderPane createPrePane() {
        BorderPane prePane = new BorderPane();
        Label label = new Label(Constants.PRE_GAME_TEXT);
        applyStyling(label);
        prePane.setCenter(label);
        return prePane;
    }

    private void setPostPaneScore(BorderPane postPane, long value) {
        Label label = new Label(Constants.POST_GAME_TEXT + "\n" + "SCORE: " + value);
        applyStyling(label);
        postPane.setCenter(label);
    }

    private Scene createFittingScene(Pane pane, double prefWidth, double prefHeight) {
        Rectangle2D bounds = Screen.getPrimary().getBounds();
        if (bounds.getWidth() < prefWidth || bounds.getHeight() < prefHeight) {
            return createFittingScene(pane, prefWidth / 1.5f, prefHeight / 1.5f);
        } else {
            return new Scene(pane, prefWidth, prefHeight);
        }
    }

    private void applyStyling(Label label) {
        label.setStyle("-fx-font-size: 32px; -fx-font-family: 'Verdana'; -fx-text-fill: black;");
        label.setTextAlignment(TextAlignment.CENTER);
    }

    public static void main(String[] args) {
        launch();
    }
}