package com.github.franckyi.emerald.controller.screen;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.controller.dialog.AboutDialogController;
import com.github.franckyi.emerald.controller.dialog.DeleteInstanceDialogController;
import com.github.franckyi.emerald.controller.dialog.LoginDialogController;
import com.github.franckyi.emerald.controller.screen.primary.*;
import com.github.franckyi.emerald.data.User;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.storage.Cache;
import com.github.franckyi.emerald.service.task.instance.InstanceCreatorTask;
import com.github.franckyi.emerald.util.UserManager;
import com.github.franckyi.emerald.view.animation.EmeraldTimeline;
import com.github.franckyi.emerald.view.animation.InstantTimeline;
import com.github.franckyi.emerald.view.animation.ScreenAnimation;
import com.jfoenix.controls.JFXBadge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXToolbar;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import org.tinylog.Logger;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MenuController extends AbstractScreenController<JFXDrawer, Void> {
    @FXML
    private BorderPane sidePane;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userStatusLabel;
    @FXML
    private JFXButton userButton;
    @FXML
    private MaterialIconView userButtonGlyph;
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
    public static Screen<InstanceSettingsController> INSTANCE_SETTINGS;
    @FXML
    private JFXButton settingsButton;
    @FXML
    private JFXToolbar header;
    @FXML
    private JFXBadge badge;
    private static Image DEFAULT_AVATAR;
    @FXML
    private Label title;
    @FXML
    private HBox instanceSettingsButton;

    private InstanceListController instanceListController;
    private ModpackListController modpackListController;
    private ModListController modListController;
    private ResourcePackListController resourcePackListController;
    private WorldListController worldListController;
    private TaskListController taskListController;
    @FXML
    private MaterialIconView icon;
    private SettingsController settingsController;

    private AboutDialogController aboutDialogController;
    private LoginDialogController loginDialogController;
    @FXML
    private StackPane content;

    public static Screen<InstanceListController> INSTANCES;
    public static Screen<ModpackListController> MODPACKS;
    public static Screen<ModListController> MODS;
    public static Screen<ResourcePackListController> RESOURCE_PACKS;
    public static Screen<WorldListController> WORLDS;
    public static Screen<TaskListController> TASKS;
    private InstanceSettingsController instanceSettingsController;
    public static Screen<SettingsController> SETTINGS;

    private Screen<?> currentScreen;
    private DeleteInstanceDialogController deleteInstanceDialogController;
    private Map<String, Image> avatars;

    @Override
    protected void initialize() {
        INSTANCES = new Screen<>(this::getInstanceListController, this::getInstancesButton);
        MODPACKS = new Screen<>(this::getModpackListController, this::getModpacksButton);
        MODS = new Screen<>(this::getModListController, this::getModsButton);
        RESOURCE_PACKS = new Screen<>(this::getResourcePackListController, this::getResourcePacksButton);
        WORLDS = new Screen<>(this::getWorldListController, this::getWorldsButton);
        TASKS = new Screen<>(this::getTaskListController, this::getTasksButton);
        INSTANCE_SETTINGS = new Screen<>(this::getInstanceSettingsController, this::getInstanceSettingsButton);
        SETTINGS = new Screen<>(this::getSettingsController, this::getSettingsButton);
        DEFAULT_AVATAR = new Image(this.getClass().getResourceAsStream("/view/img/steve.png"));
        avatars = new HashMap<>();
        avatars.put(null, DEFAULT_AVATAR);
        Emerald.getUser().addListener(obs -> Platform.runLater(this::updateUser));
        this.updateUser();
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

    private void updateUser() {
        User user = Emerald.getUser().get();
        userStatusLabel.getGraphic().getStyleClass().removeAll("fill-error", "fill-success");
        userButton.getStyleClass().removeAll("button-success", "button-error");
        if (UserManager.isUserLoggedIn()) {
            if (!avatars.containsKey(user.getProfileId())) {
                Emerald.getExecutorService().submit(() -> {
                    InputStream is = Cache.getOrDefault(Cache.AVATARS, user.getProfileId(), "https://crafatar.com/avatars/" + user.getProfileId() + "?size=50&default=MHF_Steve");
                    if (is != null) {
                        avatars.put(user.getProfileId(), new Image(is));
                    }
                    Platform.runLater(() -> userImageView.setImage(avatars.getOrDefault(user.getProfileId(), DEFAULT_AVATAR)));
                });
            }
            usernameLabel.setText(user.getDisplayName());
            userStatusLabel.setText("Logged in");
            userStatusLabel.getGraphic().getStyleClass().add("fill-success");
            userButton.getStyleClass().add("button-error");
            userButtonGlyph.setIcon(MaterialIcon.PERSON_OUTLINE);
        } else {
            userImageView.setImage(avatars.get(null));
            usernameLabel.setText("");
            userStatusLabel.setText("Not logged in");
            userStatusLabel.getGraphic().getStyleClass().add("fill-error");
            userButton.getStyleClass().add("button-success");
            userButtonGlyph.setIcon(MaterialIcon.PERSON_ADD);
        }
        userButton.setDisable(false);
    }

    @Override
    public void afterHiding() {
        this.getRoot().close();
    }

    @FXML
    private void userAction() {
        if (UserManager.isUserLoggedIn()) {
            userButton.setDisable(true);
            EmeraldApp.getInstance().fixFocus();
            Logger.info("Logging out...");
            UserManager.invalidate();
        } else {
            this.showLogin();
        }
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

    public EmeraldTimeline showInstanceSettings(Instance model) {
        if (instanceSettingsController == null) {
            instanceSettingsController = AbstractController.loadFXML("screen/primary/InstanceSettings.fxml", model);
        } else {
            instanceSettingsController.setModel(model);
        }
        return this.showInstanceSettings();
    }

    @FXML
    public EmeraldTimeline showInstanceSettings() {
        instanceSettingsButton.setVisible(true);
        return this.showScreen(INSTANCE_SETTINGS);
    }

    @FXML
    public void closeInstanceSettings() {
        instanceSettingsButton.setVisible(false);
        EmeraldApp.getInstance().fixFocus();
        if (currentScreen == INSTANCE_SETTINGS) {
            this.showInstances();
        }
    }

    @FXML
    public EmeraldTimeline showSettings() {
        return this.showScreen(SETTINGS);
    }

    @FXML
    public void showAbout() {
        if (aboutDialogController == null) {
            aboutDialogController = AbstractController.loadFXML("dialog/AboutDialog.fxml");
        }
        aboutDialogController.open();
    }

    public void showLogin() {
        this.showLogin(null, null);
    }

    public void showLogin(Consumer<User> callback) {
        this.showLogin(callback, null);
    }

    public void showLogin(String errorText) {
        this.showLogin(null, errorText);
    }

    public void showLogin(Consumer<User> callback, String errorText) {
        if (loginDialogController == null) {
            loginDialogController = AbstractController.loadFXML("dialog/LoginDialog.fxml");
        }
        loginDialogController.setLoginAction(callback);
        loginDialogController.setErrorText(errorText);
        loginDialogController.open();
    }

    public void showDeleteInstanceDialog(Instance instance) {
        if (deleteInstanceDialogController == null) {
            deleteInstanceDialogController = AbstractController.loadFXML("dialog/DeleteInstanceDialog.fxml");
        }
        deleteInstanceDialogController.setModel(instance);
        deleteInstanceDialogController.open();
    }

    public void showScreenInstant(Screen<?> screen) {
        showScreenImpl(screen, true);
    }

    public EmeraldTimeline showScreen(Screen<?> screen) {
        return showScreenImpl(screen, false);
    }

    private EmeraldTimeline showScreenImpl(Screen<?> screen, boolean instant) {
        EmeraldTimeline timeline = new InstantTimeline();
        if (currentScreen != screen) {
            if (currentScreen != null) {
                currentScreen.getButton().getStyleClass().remove("selected");
                if (instant) {
                    content.getChildren().set(0, screen.getController().getRoot());
                } else {
                    this.getRoot().close();
                    timeline = ScreenAnimation.changePrimaryScreen(content, currentScreen.getController(), screen.getController());
                }
            } else {
                content.getChildren().add(screen.getController().getRoot());
            }
            currentScreen = screen;
            icon.setIcon(screen.getController().getGlyphIcon());
            title.textProperty().bind(screen.getController().getTitle());
            header.setRight(screen.getController().getRightHeader());
            screen.getButton().getStyleClass().add("selected");
        }
        return timeline;
    }

    public void submitNewInstanceTask(InstanceCreatorTask task) {
        task.getOnLastTaskSucceededListeners().add(() -> {
            if (currentScreen == TASKS) {
                showInstances();
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

    public HBox getInstanceSettingsButton() {
        return instanceSettingsButton;
    }

    public JFXButton getSettingsButton() {
        return settingsButton;
    }

    public InstanceListController getInstanceListController() {
        if (instanceListController == null) {
            instanceListController = AbstractController.loadFXML("screen/primary/InstanceList.fxml", Emerald.getInstances());
        }
        return instanceListController;
    }

    public ModpackListController getModpackListController() {
        if (modpackListController == null) {
            modpackListController = AbstractController.loadFXML("screen/primary/ModpackList.fxml");
        }
        return modpackListController;
    }

    public ModListController getModListController() {
        if (modListController == null) {
            modListController = AbstractController.loadFXML("screen/primary/ModList.fxml");
        }
        return modListController;
    }

    public ResourcePackListController getResourcePackListController() {
        if (resourcePackListController == null) {
            resourcePackListController = AbstractController.loadFXML("screen/primary/ResourcePackList.fxml");
        }
        return resourcePackListController;
    }

    public WorldListController getWorldListController() {
        if (worldListController == null) {
            worldListController = AbstractController.loadFXML("screen/primary/WorldList.fxml");
        }
        return worldListController;
    }

    public TaskListController getTaskListController() {
        if (taskListController == null) {
            taskListController = AbstractController.loadFXML("screen/primary/TaskList.fxml", FXCollections.observableArrayList());
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

    public InstanceSettingsController getInstanceSettingsController() {
        return instanceSettingsController;
    }

    public SettingsController getSettingsController() {
        if (settingsController == null) {
            settingsController = AbstractController.loadFXML("screen/primary/Settings.fxml", Emerald.getConfiguration());
        }
        return settingsController;
    }

    private static class Screen<C extends AbstractPrimaryScreenController<?, ?>> {
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
