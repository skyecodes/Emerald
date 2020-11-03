package com.github.franckyi.emerald.controller.partial;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.util.Minecraft;
import com.jfoenix.controls.JFXButton;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Arrays;

public class InstanceButtonController extends AbstractController<StackPane, Instance> {
    @FXML
    private VBox instanceMenu;
    @FXML
    private JFXButton playButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXButton deleteButton;
    @FXML
    private Label instanceNameLabel;

    @Override
    protected void initialize() {
        instanceMenu.toFront();
        this.getRoot().hoverProperty().addListener((obs, oldVal, newVal) -> {
            KeyValue kv = new KeyValue(instanceMenu.opacityProperty(), newVal ? 1 : 0);
            KeyFrame kf = new KeyFrame(Duration.millis(100), kv);
            Timeline timeline = new Timeline(kf);
            timeline.play();
        });
        Arrays.asList(playButton, settingsButton, deleteButton).forEach(button ->
                button.hoverProperty().addListener((obs, oldVal, newVal) -> {
                    KeyValue kv = new KeyValue(button.opacityProperty(), newVal ? 0.9 : 0.6);
                    KeyFrame kf = new KeyFrame(Duration.millis(100), kv);
                    Timeline timeline = new Timeline(kf);
                    timeline.play();
                })
        );
    }

    @Override
    public void modelUpdated() {
        instanceNameLabel.textProperty().bind(this.getModel().nameProperty());
    }

    @FXML
    private void launchInstanceAction() {
        Minecraft.launch(this.getModel());
    }

    @FXML
    private void openSettingsAction() {
        EmeraldApp.getInstance().getMainController().getMenuController().showInstanceSettings(this.getModel());
    }

    @FXML
    private void deleteInstanceAction() {
        EmeraldApp.getInstance().getMainController().getMenuController().showDeleteInstanceDialog(this.getModel());
    }
}
