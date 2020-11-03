package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

public class InstanceSettingsController extends AbstractPrimaryScreenController<JFXTabPane, Instance> {
    @FXML
    private JFXTextField instanceNameField;

    private Instance currentInstance;
    private Instance oldInstance;

    @Override
    protected void modelUpdated() {
        super.modelUpdated();
        currentInstance = this.getModel();
        instanceNameField.textProperty().bindBidirectional(currentInstance.nameProperty());
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        oldInstance = currentInstance.clone();
    }

    @Override
    public void beforeHiding() {
        super.beforeHiding();
        // other stuff
        if (!currentInstance.equals(oldInstance)) {
            InstanceStorage.updateInstance(currentInstance);
        }
    }

    @Override
    public MaterialIcon getGlyphIcon() {
        return MaterialIcon.SETTINGS;
    }

    @Override
    public ObservableValue<String> getTitle() {
        return new SimpleStringProperty("Instance settings for \"").concat(currentInstance.nameProperty()).concat("\"");
    }

    @Override
    protected Node buildRightHeader() {
        MaterialIconView icon = new MaterialIconView(MaterialIcon.CLOSE);
        StackPane pane = new StackPane(icon);
        JFXRippler rippler = new JFXRippler(pane);
        rippler.getStyleClass().add("navigation-button");
        rippler.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.getMainController().getMenuController().closeInstanceSettings();
            }
        });
        return rippler;
    }
}
