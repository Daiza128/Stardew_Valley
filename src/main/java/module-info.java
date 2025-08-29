module com.example.stardewvalley {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.stardewvalley to javafx.fxml;
    exports com.example.stardewvalley;
    exports com.example.stardewvalley.controller;
    exports com.example.stardewvalley.model.achievements;
    exports com.example.stardewvalley.model.world;
    exports com.example.stardewvalley.model;
    opens com.example.stardewvalley.controller to javafx.fxml;
    opens com.example.stardewvalley.model to javafx.fxml;
}