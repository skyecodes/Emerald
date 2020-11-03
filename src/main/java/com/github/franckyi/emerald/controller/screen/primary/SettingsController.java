package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.controller.dialog.ResetConfigDialogController;
import com.github.franckyi.emerald.controller.dialog.UpdateLauncherDialogController;
import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.service.storage.LauncherStorage;
import com.github.franckyi.emerald.service.storage.Storage;
import com.github.franckyi.emerald.util.AsyncUtils;
import com.github.franckyi.emerald.util.ConfigManager;
import com.github.franckyi.emerald.util.PathUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXToggleButton;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

public class SettingsController extends AbstractPrimaryScreenController<JFXTabPane, Configuration> {
    @FXML
    private Tab generalTab;
    @FXML
    private JFXToggleButton darkThemeToggle;
    @FXML
    private Tab minecraftTab;
    @FXML
    private JFXButton updateLauncherButton;
    @FXML
    private Tab dataStorageTab;
    @FXML
    private Label cacheSizeLabel;
    @FXML
    private Label instancesSizeLabel;
    @FXML
    private Label launcherSizeLabel;

    private ResetConfigDialogController resetConfigDialogController;
    private UpdateLauncherDialogController updateLauncherDialogController;

    private Configuration currentConfiguration;
    private Configuration oldConfiguration;

    @Override
    protected void initialize() {
        darkThemeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
            currentConfiguration.setTheme(newVal ? Configuration.Theme.DARK : Configuration.Theme.LIGHT);
            EmeraldApp.getInstance().updateTheme();
        });
    }

    @FXML
    private void updateLauncherAction() {
        if (updateLauncherDialogController == null) {
            updateLauncherDialogController = AbstractController.loadFXML("dialog/UpdateLauncherDialog.fxml", this);
        }
        updateLauncherDialogController.open();
    }

    @FXML
    private void clearCacheAction() {
        AsyncUtils.runThenUpdate(() -> Storage.deleteDirectory(PathUtils.getCachePath()), path -> this.updateCacheSizeLabel());
    }

    public void resetConfig() {
        currentConfiguration.set(ConfigManager.createDefaultConfig());
        this.updateFields();
    }

    private void updateFields() {
        if (currentConfiguration.getTheme() == Configuration.Theme.CUSTOM) {
            darkThemeToggle.setDisable(true);
        } else {
            darkThemeToggle.setSelected(currentConfiguration.getTheme() == Configuration.Theme.DARK);
        }
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        currentConfiguration = Emerald.getConfiguration();
        oldConfiguration = currentConfiguration.clone();
        this.updateCacheSizeLabel();
        AsyncUtils.runThenUpdate(() -> Storage.sizeOf(PathUtils.getInstancesPath()), s -> instancesSizeLabel.setText("Instances folder size: " + s));
        this.updateLauncherSizeLabel();
        this.updateFields();
    }

    private void updateCacheSizeLabel() {
        AsyncUtils.runThenUpdate(() -> Storage.sizeOf(PathUtils.getCachePath()), s -> cacheSizeLabel.setText("Cache folder size: " + s));
    }

    public void updateLauncherSizeLabel() {
        AsyncUtils.runThenUpdate(() -> Storage.sizeOf(PathUtils.getLauncherPath()), s -> launcherSizeLabel.setText("Launcher folder size: " + s));
        updateLauncherButton.setDisable(!LauncherStorage.isLauncherInitialized());
    }

    @Override
    public void beforeHiding() {
        super.beforeHiding();
        if (!currentConfiguration.equals(oldConfiguration)) {
            ConfigManager.save(currentConfiguration);
        }
    }

    @Override
    public void afterHiding() {
        super.afterHiding();
        this.getRoot().getSelectionModel().selectFirst();
    }

    @Override
    public MaterialIcon getGlyphIcon() {
        return MaterialIcon.TUNE;
    }

    @Override
    public ObservableValue<String> getTitle() {
        return new ReadOnlyStringWrapper("Settings");
    }

    @Override
    public Node buildRightHeader() {
        MaterialIconView icon = new MaterialIconView(MaterialIcon.SETTINGS_BACKUP_RESTORE);
        StackPane pane = new StackPane(icon);
        JFXRippler rippler = new JFXRippler(pane);
        rippler.getStyleClass().add("navigation-button");
        rippler.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                if (resetConfigDialogController == null) {
                    resetConfigDialogController = AbstractController.loadFXML("dialog/ResetConfigDialog.fxml");
                }
                resetConfigDialogController.open();
            }
        });
        return rippler;
    }
}
