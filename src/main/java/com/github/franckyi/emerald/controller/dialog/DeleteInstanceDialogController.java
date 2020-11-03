package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import javafx.fxml.FXML;

public class DeleteInstanceDialogController extends AbstractDialogController<Instance> {
    @FXML
    private void deleteInstanceAction() {
        Emerald.getExecutorService().submit(() -> InstanceStorage.deleteInstance(this.getModel()));
        this.close();
    }
}
