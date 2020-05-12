package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;

import java.util.List;

public class InstanceListController extends MenuController<JFXMasonryPane, List<Instance>> {

    @Override
    protected void modelUpdated() {
        this.getRoot().getChildren().clear();
        this.getModel().stream().map(InstanceButton::new).forEach(this.getRoot().getChildren()::add);
        this.getRoot().getChildren().addAll(
                new NewInstanceButton(),
                new SettingsButton());
    }

    private class InstanceButton extends JFXButton {
        public InstanceButton(Instance instance) {

        }
    }

    private class NewInstanceButton extends JFXButton {
        public NewInstanceButton() {
            this.setPrefSize(128, 128);
            this.getStyleClass().add("new-instance-button");
            this.setButtonType(ButtonType.RAISED);
            this.setGraphic(new MaterialIconView(MaterialIcon.ADD_CIRCLE));
            this.setOnAction(e -> InstanceListController.this.getMainController().showNewInstance());
        }

    }

    private class SettingsButton extends JFXButton {
        public SettingsButton() {
            this.setPrefSize(128, 128);
            this.getStyleClass().add("settings-button");
            this.setButtonType(ButtonType.RAISED);
            this.setGraphic(new MaterialIconView(MaterialIcon.SETTINGS));
            this.setOnAction(e -> InstanceListController.this.getMainController().showSettings());
        }

    }
}
