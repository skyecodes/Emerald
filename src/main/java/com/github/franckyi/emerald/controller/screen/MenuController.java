package com.github.franckyi.emerald.controller.screen;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.dialog.AboutDialogController;
import com.github.franckyi.emerald.controller.screen.primary.*;
import com.github.franckyi.emerald.service.task.instance.InstanceCreatorTask;
import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.view.animation.EmeraldTimeline;
import com.github.franckyi.emerald.view.animation.InstantTimeline;
import com.github.franckyi.emerald.view.animation.ScreenAnimation;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXToolbar;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

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
    private JFXButton tasksButton;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXToolbar header;
    @FXML
    private JFXBadge badge;
    @FXML
    private StackPane content;
    @FXML
    private Label title;

    private InstanceListController instanceListController;
    private ModpackListController modpackListController;
    private ModListController modListController;
    private ResourcePackListController resourcePackListController;
    private WorldListController worldListController;
    private TaskListController taskListController;
    private SettingsController settingsController;
    private AboutDialogController aboutDialogController;

    private PrimaryScreenController<?, ?> currentScreen;
    private Node selectedButton;

    @Override
    protected void initialize() {
        // fixing node hierarchy
        this.getRoot().lookup(".jfx-drawer-overlay-pane").toFront();
        sidePane.getParent().toFront();
        this.showInstances();
        Group group = (Group) badge.getChildren().get(1);
        group.setMouseTransparent(true);
        group.getChildren().addListener((ListChangeListener<Node>) change -> {
            if (!change.getList().isEmpty()) {
                StackPane stackPane = (StackPane) change.getList().get(0);
                stackPane.setMouseTransparent(true);
                stackPane.getChildren().get(0).setMouseTransparent(true);
            }
        });
        badge.setEnabled(false);
        badge.refreshBadge();
    }

    @Override
    public void afterHiding() {
        this.getRoot().close();
    }

    @FXML
    private void openDrawer() {
        this.getRoot().open();
    }

    @FXML
    public EmeraldTimeline showInstances() {
        if (instanceListController == null) {
            instanceListController = Controller.loadFXML("screen/primary/InstanceList.fxml", Emerald::getInstances);
        }
        return this.selectScreen(instanceListController, instancesButton);
    }

    @FXML
    private EmeraldTimeline showNewInstance() {
        return this.getMainController().showNewInstance();
    }

    @FXML
    private EmeraldTimeline showModpacks() {
        if (modpackListController == null) {
            modpackListController = Controller.loadFXML("screen/primary/ModpackList.fxml");
        }
        return this.selectScreen(modpackListController, modpacksButton);
    }

    @FXML
    private EmeraldTimeline showMods() {
        if (modListController == null) {
            modListController = Controller.loadFXML("screen/primary/ModList.fxml");
        }
        return this.selectScreen(modListController, modsButton);
    }

    @FXML
    private EmeraldTimeline showResourcePacks() {
        if (resourcePackListController == null) {
            resourcePackListController = Controller.loadFXML("screen/primary/ResourcePackList.fxml");
        }
        return this.selectScreen(resourcePackListController, resourcePacksButton);
    }

    @FXML
    private EmeraldTimeline showWorlds() {
        if (worldListController == null) {
            worldListController = Controller.loadFXML("screen/primary/WorldList.fxml");
        }
        return this.selectScreen(worldListController, worldsButton);
    }

    @FXML
    public EmeraldTimeline showTasks() {
        if (taskListController == null) {
            taskListController = Controller.loadFXML("screen/primary/TaskList.fxml", FXCollections.observableArrayList());
            taskListController.getModel().addListener((ListChangeListener<Task<?>>) change -> {
                int size = change.getList().size();
                badge.setEnabled(size > 0);
                badge.setText(Integer.toString(size));
                tasksButton.setVisible(size > 0);
                tasksButton.setText(String.format("Tasks (%d)", size));
            });
        }
        return this.selectScreen(taskListController, tasksButton);
    }

    @FXML
    private EmeraldTimeline showSettings() {
        if (settingsController == null) {
            settingsController = Controller.loadFXML("screen/primary/Settings.fxml", Emerald.getConfiguration());
        }
        return this.selectScreen(settingsController, settingsButton);
    }

    @FXML
    private void showAbout() {
        if (aboutDialogController == null) {
            aboutDialogController = Controller.loadFXML("dialog/AboutDialog.fxml");
        }
        aboutDialogController.getRoot().show(this.getMainController().getRoot());
    }

    private EmeraldTimeline selectScreen(PrimaryScreenController<?, ?> screen, Node button) {
        EmeraldTimeline timeline = new InstantTimeline();
        if (button != selectedButton) {
            if (currentScreen != null) {
                selectedButton.getStyleClass().remove("selected");
                this.getRoot().close();
                timeline = ScreenAnimation.changePrimaryScreen(content, currentScreen, screen);
            } else {
                content.getChildren().add(screen.getRoot());
            }
            currentScreen = screen;
            selectedButton = button;
            header.setRight(screen.getRightHeader());
            title.setText(screen.getTitle());
            button.getStyleClass().add("selected");
        }
        return timeline;
    }

    public void submitNewInstanceTask(InstanceCreatorTask task) {
        task.getOnLastTaskSucceededListeners().add(() -> {
            if (currentScreen == taskListController) {
                EmeraldApp.getInstance().getMainController().getMenuController().showInstances();
            }
        });
        taskListController.submit(task);
    }
}
