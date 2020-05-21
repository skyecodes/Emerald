package com.github.franckyi.emerald.controller.popup;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.InstanceListController;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController extends Controller<JFXListView<?>, InstanceListController> {
    @FXML
    private Label settingsButton;
    @FXML
    private Label aboutButton;

    @Override
    protected void initialize() {
        settingsButton.parentProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) newVal.setOnMouseClicked(e -> {
                this.getModel().getPopup().hide();
                this.getModel().getMainController().showSettings();
            });
        });
        aboutButton.parentProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) newVal.setOnMouseClicked(e -> {
                this.getModel().getPopup().hide();
                this.getModel().getAboutDialogController().getRoot().show(
                        this.getModel().getMainController().getRoot());
            });
        });
    }
}
