package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.controller.dialog.AboutDialogController;
import com.github.franckyi.emerald.controller.popup.PopupController;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.view.ViewUtils;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.util.List;

public class InstanceListController extends MenuController<JFXDrawer, List<Instance>> {
    @FXML
    private StackPane sidePane;
    @FXML
    private Label newInstanceLabel;
    @FXML
    private Label settingsLabel;
    @FXML
    private BorderPane content;
    @FXML
    private JFXToolbar header;
    @FXML
    private JFXRippler menuButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private JFXMasonryPane instanceListPane;

    private PopupController popupController;
    private AboutDialogController aboutDialogController;
    private JFXPopup popup;

    @Override
    protected void initialize() {
        popupController = loadFXML("popup/InstanceListPopup.fxml", this);
        aboutDialogController = loadFXML("dialog/AboutDialog.fxml");
        popup = new JFXPopup(popupController.getRoot());
        newInstanceLabel.parentProperty().addListener((obs, oldVal, newVal) -> newVal.setOnMouseClicked(e ->
                this.getMainController().showNewInstance().addListener(this::drawerAction)));
        settingsLabel.parentProperty().addListener((obs, oldVal, newVal) -> newVal.setOnMouseClicked(e ->
                this.getMainController().showSettings().addListener(this::drawerAction)));
        // fixing node hierarchy
        this.getRoot().lookup(".jfx-drawer-overlay-pane").toFront();
        sidePane.getParent().toFront();
        JFXScrollPane.smoothScrolling(scrollPane);
        // TODO find a way to show the shadow
        scrollPane.vvalueProperty().addListener((obs, oldVal, newVal) -> header.setEffect(newVal.doubleValue() < .01 ? null : ViewUtils.SHADOW_EFFECT));
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        scrollPane.setVvalue(0);
    }

    @Override
    protected void modelUpdated() {
        instanceListPane.getChildren().clear();
        this.getModel().stream().map(InstanceButton::new).forEach(instanceListPane.getChildren()::add);
        for (int i = 0; i < 50; i++) {
            instanceListPane.getChildren().add(new BlankButton());
        }
        // fix required to show scrollbar
        instanceListPane.parentProperty().addListener(((obs, oldVal, newVal) -> Platform.runLater(instanceListPane::requestLayout)));
    }

    @FXML
    private void drawerAction() {
        this.getRoot().toggle();
    }

    @FXML
    private void menuAction() {
        popup.show(menuButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT);
    }

    public AboutDialogController getAboutDialogController() {
        return aboutDialogController;
    }

    public JFXPopup getPopup() {
        return popup;
    }

    private class InstanceButton extends JFXButton {
        public InstanceButton(Instance instance) {

        }
    }

    private class BlankButton extends JFXButton {
        public BlankButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-surface", "instance-list-button");
        }
    }

}
