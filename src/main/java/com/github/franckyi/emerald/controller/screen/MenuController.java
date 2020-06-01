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

import java.util.function.Supplier;

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

    public static Screen<InstanceListController> INSTANCES;
    public static Screen<ModpackListController> MODPACKS;
    public static Screen<ModListController> MODS;
    public static Screen<ResourcePackListController> RESOURCE_PACKS;
    public static Screen<WorldListController> WORLDS;
    public static Screen<TaskListController> TASKS;
    public static Screen<SettingsController> SETTINGS;

    private Screen<?> currentScreen;

    @Override
    protected void initialize() {
        INSTANCES = new Screen<>(this::getInstanceListController, this::getInstancesButton);
        MODPACKS = new Screen<>(this::getModpackListController, this::getModpacksButton);
        MODS = new Screen<>(this::getModListController, this::getModsButton);
        RESOURCE_PACKS = new Screen<>(this::getResourcePackListController, this::getResourcePacksButton);
        WORLDS = new Screen<>(this::getWorldListController, this::getWorldsButton);
        TASKS = new Screen<>(this::getTaskListController, this::getTasksButton);
        SETTINGS = new Screen<>(this::getSettingsController, this::getSettingsButton);
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
        return this.showScreen(INSTANCES);
    }

    @FXML
    public EmeraldTimeline showNewInstance() {
        return this.getMainController().showNewInstance();
    }

    @FXML
    public EmeraldTimeline showModpacks() {
        return this.showScreen(MODPACKS);
    }

    @FXML
    public EmeraldTimeline showMods() {
        return this.showScreen(MODS);
    }

    @FXML
    public EmeraldTimeline showResourcePacks() {
        return this.showScreen(RESOURCE_PACKS);
    }

    @FXML
    public EmeraldTimeline showWorlds() {
        return this.showScreen(WORLDS);
    }

    @FXML
    public EmeraldTimeline showTasks() {
        return this.showScreen(TASKS);
    }

    @FXML
    public EmeraldTimeline showSettings() {
        return this.showScreen(SETTINGS);
    }

    @FXML
    public void showAbout() {
        if (aboutDialogController == null) {
            aboutDialogController = Controller.loadFXML("dialog/AboutDialog.fxml");
        }
        aboutDialogController.getRoot().show(this.getMainController().getRoot());
    }

    public void showScreenInstant(Screen<?> screen) {
        if (currentScreen != screen) {
            if (currentScreen != null) {
                currentScreen.getButton().getStyleClass().remove("selected");
                content.getChildren().set(0, screen.getController().getRoot());
            } else {
                content.getChildren().add(screen.getController().getRoot());
            }
            currentScreen = screen;
            header.setRight(screen.getController().getRightHeader());
            title.setText(screen.getController().getTitle());
            screen.getButton().getStyleClass().add("selected");
        }
    }

    public EmeraldTimeline showScreen(Screen<?> screen) {
        EmeraldTimeline timeline = new InstantTimeline();
        if (currentScreen != screen) {
            if (currentScreen != null) {
                currentScreen.getButton().getStyleClass().remove("selected");
                this.getRoot().close();
                timeline = ScreenAnimation.changePrimaryScreen(content, currentScreen.getController(), screen.getController());
            } else {
                content.getChildren().add(screen.getController().getRoot());
            }
            currentScreen = screen;
            header.setRight(screen.getController().getRightHeader());
            title.setText(screen.getController().getTitle());
            screen.getButton().getStyleClass().add("selected");
        }
        return timeline;
    }

    public void submitNewInstanceTask(InstanceCreatorTask task) {
        task.getOnLastTaskSucceededListeners().add(() -> {
            if (currentScreen == TASKS) {
                EmeraldApp.getInstance().getMainController().getMenuController().showInstances();
            }
        });
        taskListController.submit(task);
    }

    public HBox getInstancesButton() {
        return instancesButton;
    }

    public JFXButton getModpacksButton() {
        return modpacksButton;
    }

    public JFXButton getModsButton() {
        return modsButton;
    }

    public JFXButton getResourcePacksButton() {
        return resourcePacksButton;
    }

    public JFXButton getWorldsButton() {
        return worldsButton;
    }

    public JFXButton getTasksButton() {
        return tasksButton;
    }

    public JFXButton getSettingsButton() {
        return settingsButton;
    }

    public InstanceListController getInstanceListController() {
        if (instanceListController == null) {
            instanceListController = Controller.loadFXML("screen/primary/InstanceList.fxml", Emerald.getInstances());
        }
        return instanceListController;
    }

    public ModpackListController getModpackListController() {
        if (modpackListController == null) {
            modpackListController = Controller.loadFXML("screen/primary/ModpackList.fxml");
        }
        return modpackListController;
    }

    public ModListController getModListController() {
        if (modListController == null) {
            modListController = Controller.loadFXML("screen/primary/ModList.fxml");
        }
        return modListController;
    }

    public ResourcePackListController getResourcePackListController() {
        if (resourcePackListController == null) {
            resourcePackListController = Controller.loadFXML("screen/primary/ResourcePackList.fxml");
        }
        return resourcePackListController;
    }

    public WorldListController getWorldListController() {
        if (worldListController == null) {
            worldListController = Controller.loadFXML("screen/primary/WorldList.fxml");
        }
        return worldListController;
    }

    public TaskListController getTaskListController() {
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
        return taskListController;
    }

    public SettingsController getSettingsController() {
        if (settingsController == null) {
            settingsController = Controller.loadFXML("screen/primary/Settings.fxml", Emerald.getConfiguration());
        }
        return settingsController;
    }

    private static class Screen<C extends PrimaryScreenController<?, ?>> {
        private final Supplier<C> controller;
        private final Supplier<Node> button;

        private Screen(Supplier<C> controller, Supplier<Node> button) {
            this.controller = controller;
            this.button = button;
        }

        public C getController() {
            return controller.get();
        }

        public Node getButton() {
            return button.get();
        }
    }
}
