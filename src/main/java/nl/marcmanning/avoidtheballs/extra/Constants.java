package nl.marcmanning.avoidtheballs.extra;

import javafx.scene.paint.Color;

public final class Constants {
    public static final double STANDARD_APP_WIDTH = 640.0;
    public static final double STANDARD_APP_HEIGHT = 480.0;

    public static final int FPS = 100;
    public static final int TICK_DURATION_MILLIS = 1000 / FPS;
    public static final double TICK_DURATION = 1.0 / FPS;

    public static final String PRE_GAME_TEXT = "PRESS TO START";
    public static final String POST_GAME_TEXT = "GAME OVER";

    public static final double MIN_RADIUS = 60;
    public static final double MAX_RADIUS = 100;
    public static final double MIN_SPEED = 250;
    public static final double MAX_SPEED = 450;
    public static final Color[] COLORS = new Color[] {Color.web("#f6cab7"), Color.web("#ffc8a5"),
            Color.web("#fff4ce"), Color.web("#cdedce"), Color.web("#b9d8e4"), Color.web("#d1c5e1")};
    public static final int SPAWN_DELAY = 8000;
    public static final int SPAWN_LIMIT = 12;

    private Constants() {}
}
