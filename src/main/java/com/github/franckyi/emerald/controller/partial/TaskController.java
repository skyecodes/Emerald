package com.github.franckyi.emerald.controller.partial;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.service.task.EmeraldTask;
import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class TaskController extends Controller<BorderPane, EmeraldTask<?>> {
    @FXML
    private Label titleLabel;
    @FXML
    private Label messageLabel;
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private Label progressLabel;

    @Override
    protected void modelUpdated() {
        titleLabel.textProperty().bind(this.getModel().titleProperty());
        messageLabel.textProperty().bind(this.getModel().messageProperty());
        progressBar.progressProperty().bind(this.getModel().progressProperty());
        progressLabel.textProperty().bind(this.getModel().progressProperty().multiply(100).asString("%.0f").concat("%"));
        progressLabel.visibleProperty().bind(this.getModel().progressProperty().greaterThan(0));
    }
}
