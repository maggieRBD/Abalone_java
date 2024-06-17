module cz.mendel.abalone {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens cz.mendel.abalone to javafx.fxml;
    exports cz.mendel.abalone;
    exports cz.mendel.abalone.game;
    opens cz.mendel.abalone.game to javafx.fxml;
}