package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;

import java.util.List;

public class InstanceListController implements Controller<List<Instance>> {

    @FXML
    private JFXMasonryPane instanceListPane;

    @Override
    public void initialize(List<Instance> model) {
        model.forEach(instance -> {

        });
        instanceListPane.getChildren().addAll(
                new NewInstanceButton(),
                new SettingsButton());
    }

    private static class NewInstanceButton extends JFXButton {
        public NewInstanceButton() {
            this.setPrefSize(128, 128);
            this.getStyleClass().add("new-instance-button");
            this.setButtonType(ButtonType.RAISED);
            this.setGraphic(new MaterialIconView(MaterialIcon.ADD_CIRCLE));
        }
    }

    private static class SettingsButton extends JFXButton {
        public SettingsButton() {
            this.setPrefSize(128, 128);
            this.getStyleClass().add("settings-button");
            this.setButtonType(ButtonType.RAISED);
            this.setGraphic(new MaterialIconView(MaterialIcon.SETTINGS));
            this.setOnAction(e -> EmeraldApp.getInstance().getMainViewController().getController().showSettings());
        }
    }
}
