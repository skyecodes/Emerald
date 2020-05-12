package com.github.franckyi.emerald.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;

public class NewVanillaInstanceController extends MenuController<Region, Void> {
    @FXML
    private JFXTextField instanceFieldName;

    @Override
    protected void initialize() {
        this.setBeforeShowing(() -> instanceFieldName.clear());
    }

    @FXML
    private void backAction() {
        this.getMainController().showPrevious();
    }

    @FXML
    private void homeAction() {
        this.getMainController().showHome();
    }
}
