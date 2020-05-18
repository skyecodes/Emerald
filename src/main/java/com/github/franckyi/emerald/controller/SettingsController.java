package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.util.ConfigManager;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class SettingsController extends MenuController<BorderPane, Void> {
    @FXML
    private JFXToggleButton darkThemeToggle;

    private Configuration currentConfiguration;
    private Configuration defaultConfiguration;

    @Override
    protected void initialize() {
        currentConfiguration = EmeraldUtils.getConfiguration();
        if (currentConfiguration.getTheme() == Configuration.Theme.CUSTOM) {
            darkThemeToggle.setDisable(true);
        } else {
            darkThemeToggle.setSelected(currentConfiguration.getTheme() == Configuration.Theme.DARK);
            darkThemeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                currentConfiguration.setTheme(newVal ? Configuration.Theme.DARK : Configuration.Theme.LIGHT);
                EmeraldApp.getInstance().updateTheme();
            });
        }
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        defaultConfiguration = EmeraldUtils.getConfiguration().clone();
    }

    @FXML
    private void backAction() throws IOException {
        if (!currentConfiguration.equals(defaultConfiguration)) {
            ConfigManager.save(currentConfiguration);
        }
        this.getMainController().showPrevious();
    }
}
