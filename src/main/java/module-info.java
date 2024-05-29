module nl.marcmanning.avoidtheballs {
    requires javafx.controls;
    requires javafx.fxml;
    requires game.library;
    requires java.desktop;

    opens nl.marcmanning.avoidtheballs to javafx.fxml;
    exports nl.marcmanning.avoidtheballs;
    exports nl.marcmanning.avoidtheballs.components;
    exports nl.marcmanning.avoidtheballs.utils;
}