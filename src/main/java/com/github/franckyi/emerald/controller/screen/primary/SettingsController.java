package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.dialog.ResetConfigDialogController;
import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.util.ConfigManager;
import com.github.franckyi.emerald.util.Emerald;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class SettingsController extends PrimaryScreenController<BorderPane, Configuration> {
    @FXML
    private JFXToggleButton darkThemeToggle;

    private ResetConfigDialogController resetConfigDialogController;

    private Configuration currentConfiguration;
    private Configuration oldConfiguration;

    @Override
    protected void initialize() {
        resetConfigDialogController = Controller.loadFXML("dialog/ResetConfigDialog.fxml", this);
        currentConfiguration = Emerald.getConfiguration();
        this.updateFields();
    }

    private void updateFields() {
        if (currentConfiguration.getTheme() == Configuration.Theme.CUSTOM) {
            darkThemeToggle.setDisable(true);
        } else {
            darkThemeToggle.setSelected(currentConfiguration.getTheme() == Configuration.Theme.DARK);
            darkThemeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                currentConfiguration.setTheme(newVal ? Configuration.Theme.DARK : Configuration.Theme.LIGHT);
                EmeraldApp.getInstance().updateTheme();
            });
        }
    }

    public void resetConfig() {
        currentConfiguration.set(ConfigManager.createDefaultConfig());
        this.updateFields();
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        oldConfiguration = Emerald.getConfiguration().clone();
    }

    @Override
    public void beforeHiding() {
        super.beforeHiding();
        if (!currentConfiguration.equals(oldConfiguration)) {
            ConfigManager.save(currentConfiguration);
        }
    }

    @Override
    public String getTitle() {
        return "Settings";
    }

    @Override
    public Node buildRightHeader() {
        MaterialIconView icon = new MaterialIconView(MaterialIcon.SETTINGS_BACKUP_RESTORE);
        StackPane pane = new StackPane(icon);
        JFXRippler rippler = new JFXRippler(pane);
        rippler.getStyleClass().add("navigation-button");
        rippler.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                resetConfigDialogController.getRoot().show(this.getMainController().getRoot());
            }
        });
        return rippler;
    }
}
