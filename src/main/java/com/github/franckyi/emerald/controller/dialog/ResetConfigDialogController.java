package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.controller.screen.primary.SettingsController;
import javafx.fxml.FXML;

public class ResetConfigDialogController extends DialogController<SettingsController> {
    @FXML
    private void closeDialogAction() {
        this.close();
    }

    @FXML
    private void resetAction() {
        this.close();
        this.getModel().resetConfig();
    }
}
