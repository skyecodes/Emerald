package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.util.SystemUtils;
import javafx.fxml.FXML;

public class AboutDialogController extends DialogController<Void> {
    @FXML
    private void closeDialogAction() {
        this.close();
    }

    @FXML
    private void openWebsite() {
        SystemUtils.openBrowser("https://github.com/Franckyi/Emerald");
    }
}
