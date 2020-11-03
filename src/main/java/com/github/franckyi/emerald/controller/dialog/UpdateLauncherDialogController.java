package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.controller.screen.primary.SettingsController;
import com.github.franckyi.emerald.service.storage.Storage;
import com.github.franckyi.emerald.util.AsyncUtils;
import com.github.franckyi.emerald.util.PathUtils;
import javafx.fxml.FXML;

public class UpdateLauncherDialogController extends AbstractDialogController<SettingsController> {
    @FXML
    private void updateLauncherAction() {
        AsyncUtils.runThenUpdate(() -> Storage.deleteDirectory(PathUtils.getLauncherPath()), path -> {
            this.close();
            this.getModel().updateLauncherSizeLabel();
        });
    }
}
