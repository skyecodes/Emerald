package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.screen.primary.SettingsController;
import com.jfoenix.controls.JFXDialog;
import javafx.fxml.FXML;

public class ResetConfigDialogController extends Controller<JFXDialog, SettingsController> {
    @FXML
    private void closeDialogAction() {
        this.getRoot().close();
        EmeraldApp.getInstance().fixFocus();
    }

    @FXML
    private void resetAction() {
        this.getRoot().close();
        this.getModel().resetConfig();
    }
}
