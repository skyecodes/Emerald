package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.controller.screen.primary.SettingsController;
import javafx.fxml.FXML;

public class ResetConfigDialogController extends AbstractDialogController<SettingsController> {
    @FXML
    private void resetAction() {
        this.close();
        this.getModel().resetConfig();
    }
}
