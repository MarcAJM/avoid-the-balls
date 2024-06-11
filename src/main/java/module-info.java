module nl.marcmanning.avoidtheballs {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.apache.commons.math4.legacy;

    opens nl.marcmanning.avoidtheballs to javafx.fxml;
    exports nl.marcmanning.avoidtheballs;
    exports nl.marcmanning.avoidtheballs.components;
    exports nl.marcmanning.avoidtheballs.extra;
    opens nl.marcmanning.avoidtheballs.extra to javafx.fxml;
}