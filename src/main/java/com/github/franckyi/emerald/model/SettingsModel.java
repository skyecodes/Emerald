package com.github.franckyi.emerald.model;

import com.github.franckyi.emerald.controller.MainController;

public class SettingsModel {
    private final MainController mainController;

    public SettingsModel(MainController mainController) {
        this.mainController = mainController;
    }

    public MainController getMainController() {
        return mainController;
    }
}
