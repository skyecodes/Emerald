package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTooltip;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.util.List;

public class InstanceListController extends MenuController<BorderPane, List<Instance>> {

    @FXML
    public ScrollPane scrollPane;
    @FXML
    private Pane fillerPane;
    @FXML
    private JFXRippler menuButton;
    @FXML
    private JFXMasonryPane instanceListPane;

    @Override
    protected void initialize() {
        fillerPane.prefWidthProperty().bind(menuButton.widthProperty());
    }

    @Override
    protected void modelUpdated() {
        instanceListPane.getChildren().clear();
        this.getModel().stream().map(InstanceButton::new).forEach(instanceListPane.getChildren()::add);
        instanceListPane.getChildren().addAll(
                new NewInstanceButton(),
                new SettingsButton());
        for (int i = 0; i < 50; i++) {
            instanceListPane.getChildren().add(new BlankButton());
        }
    }

    @FXML
    private void menuAction() {
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

    private class SettingsButton extends JFXButton {
        public SettingsButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-surface", "instance-list-button");
            this.setGraphic(new MaterialIconView(MaterialIcon.SETTINGS));
            this.setOnAction(e -> InstanceListController.this.getMainController().showSettings());
            JFXTooltip.install(this, new JFXTooltip("Settings"));
        }

    }

    private class BlankButton extends JFXButton {
        public BlankButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-surface", "instance-list-button");
        }
    }
}
