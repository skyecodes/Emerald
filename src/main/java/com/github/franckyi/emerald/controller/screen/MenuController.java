package com.github.franckyi.emerald.controller.screen;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.dialog.AboutDialogController;
import com.github.franckyi.emerald.controller.screen.primary.*;
import com.github.franckyi.emerald.service.init.ContextLoader;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXToolbar;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class MenuController extends ScreenController<JFXDrawer, Void> {
    @FXML
    private BorderPane sidePane;
    @FXML
    private HBox instancesButton;
    @FXML
    private JFXButton modpacksButton;
    @FXML
    private JFXButton modsButton;
    @FXML
    private JFXButton resourcePacksButton;
    @FXML
    private JFXButton worldsButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXToolbar header;
    @FXML
    private BorderPane content;
    @FXML
    private Label title;

    private InstanceListController instanceListController;
    private ModpackListController modpackListController;
    private ModListController modListController;
    private ResourcePackListController resourcePackListController;
    private WorldListController worldListController;
    private SettingsController settingsController;
    private AboutDialogController aboutDialogController;

    private PrimaryScreenController<?, ?> currentScreen;
    private Node selectedButton;

    @Override
    protected void initialize() {
        instanceListController = Controller.loadFXML("screen/primary/InstanceList.fxml", ContextLoader::loadInstances);
        modpackListController = Controller.loadFXML("screen/primary/ModpackList.fxml");
        modListController = Controller.loadFXML("screen/primary/ModList.fxml");
        resourcePackListController = Controller.loadFXML("screen/primary/ResourcePackList.fxml");
        worldListController = Controller.loadFXML("screen/primary/WorldList.fxml");
        settingsController = Controller.loadFXML("screen/primary/Settings.fxml", EmeraldUtils.getConfiguration());
        aboutDialogController = Controller.loadFXML("dialog/AboutDialog.fxml");
        // fixing node hierarchy
        this.getRoot().lookup(".jfx-drawer-overlay-pane").toFront();
        sidePane.getParent().toFront();
        this.showInstances();
    }

    @FXML
    private void toggleDrawer() {
        this.getRoot().toggle();
    }

    @FXML
    private void showInstances() {
        this.selectScreen(instanceListController, instancesButton);
    }

    @FXML
    private void showNewInstance() {
        this.toggleDrawer();
        this.getMainController().showNewInstance();
    }

    @FXML
    private void showModpacks() {
        this.selectScreen(modpackListController, modpacksButton);
    }

    @FXML
    private void showMods() {
        this.selectScreen(modListController, modsButton);
    }

    @FXML
    private void showResourcePacks() {
        this.selectScreen(resourcePackListController, resourcePacksButton);
    }

    @FXML
    private void showWorlds() {
        this.selectScreen(worldListController, worldsButton);
    }

    @FXML
    private void showSettings() {
        this.selectScreen(settingsController, settingsButton);
    }

    @FXML
    private void showAbout() {
        aboutDialogController.getRoot().show(this.getMainController().getRoot());
    }

    private void selectScreen(PrimaryScreenController<?, ?> screen, Node button) {
        if (button != selectedButton) {
            if (currentScreen != null) {
                currentScreen.onHide();
                selectedButton.getStyleClass().remove("selected");
                this.toggleDrawer();
            }
            currentScreen = screen;
            selectedButton = button;
            content.setCenter(screen.getRoot());
            header.setRight(screen.getRightHeader());
            title.setText(screen.getTitle());
            button.getStyleClass().add("selected");
            screen.onShow();
        }
    }
}
