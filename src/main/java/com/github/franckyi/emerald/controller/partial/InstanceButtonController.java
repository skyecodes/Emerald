package com.github.franckyi.emerald.controller.partial;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import com.github.franckyi.emerald.util.Emerald;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class InstanceButtonController extends Controller<StackPane, Instance> {
    @FXML
    private VBox instanceMenu;
    @FXML
    private Label instanceNameLabel;

    @Override
    protected void initialize() {
        instanceMenu.toFront();
        this.getRoot().hoverProperty().addListener((obs, oldVal, newVal) -> {
            KeyValue kv = new KeyValue(instanceMenu.opacityProperty(), newVal ? 1 : 0);
            KeyFrame kf = new KeyFrame(Duration.millis(50), kv);
            Timeline timeline = new Timeline(kf);
            timeline.play();
        });
    }

    @Override
    protected void modelUpdated() {
        instanceNameLabel.setText(this.getModel().getDisplayName());
    }

    @FXML
    private void launchInstanceAction() {
    }

    @FXML
    private void openSettingsAction() {
    }

    @FXML
    private void deleteInstanceAction() {
        Emerald.getExecutorService().submit(() -> InstanceStorage.deleteInstance(this.getModel()));
    }
}
