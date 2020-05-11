package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.model.SettingsModel;
import com.github.franckyi.emerald.util.ConfigManager;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SettingsController implements Controller<SettingsModel> {

    @FXML
    private BorderPane root;
    @FXML
    private JFXDialog dialog;
    @FXML
    private JFXToggleButton darkThemeToggle;

    private MainController mainController;
    private Configuration defaultConfiguration;

    @Override
    public void initialize(SettingsModel model) {
        mainController = model.getMainController();
        root.getChildren().remove(dialog);
        darkThemeToggle.setSelected(EmeraldUtils.getConfiguration().isDarkTheme());
        darkThemeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            EmeraldUtils.getConfiguration().setDarkTheme(newVal);
            EmeraldApp.getInstance().updateTheme();
        });
    }

    public void initDefaultConfiguration() {
        defaultConfiguration = (Configuration) EmeraldUtils.getConfiguration().clone();
    }

    @FXML
    private void backAction() throws IOException {
        if (!EmeraldUtils.getConfiguration().equals(defaultConfiguration)) {
            ConfigManager.save(EmeraldUtils.getConfiguration());
        }
        mainController.hideSettings();
    }

    @FXML
    private void infoAction() {
        dialog.show(mainController.getRoot());
    }

    @FXML
    private void closeDialogAction() {
        dialog.close();
        EmeraldApp.getInstance().fixFocus();
    }
}
