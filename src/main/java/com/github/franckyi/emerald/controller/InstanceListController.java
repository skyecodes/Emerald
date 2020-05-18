package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.controller.dialog.AboutDialogController;
import com.github.franckyi.emerald.controller.popup.PopupController;
import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class InstanceListController extends MenuController<BorderPane, List<Instance>> {
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
        JFXScrollPane.smoothScrolling(scrollPane);
        popupController = loadFXML("popup/InstanceListPopup.fxml", this);
        aboutDialogController = loadFXML("dialog/AboutDialog.fxml");
    }

    @Override
    protected void modelUpdated() {
        popup = new JFXPopup(popupController.getRoot());
        instanceListPane.getChildren().clear();
        this.getModel().stream().map(InstanceButton::new).forEach(instanceListPane.getChildren()::add);
        instanceListPane.getChildren().add(new NewInstanceButton());
        for (int i = 0; i < 10; i++) {
            instanceListPane.getChildren().add(new BlankButton());
        }
        // fix required to show scrollbar
        instanceListPane.parentProperty().addListener(((obs, oldVal, newVal) -> Platform.runLater(instanceListPane::requestLayout)));
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

    private class NewInstanceButton extends JFXButton {
        public NewInstanceButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-success", "instance-list-button");
            this.setGraphic(new MaterialIconView(MaterialIcon.ADD_CIRCLE));
            this.setOnAction(e -> InstanceListController.this.getMainController().showNewInstance());
            JFXTooltip.install(this, new JFXTooltip("Create new instance"));
        }
    }

    private class BlankButton extends JFXButton {
        public BlankButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-surface", "instance-list-button");
        }
    }

}
