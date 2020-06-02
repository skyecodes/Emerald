package com.github.franckyi.emerald;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.MainController;
import com.github.franckyi.emerald.data.Configuration;
import com.github.franckyi.emerald.service.storage.InstanceStorage;
import com.github.franckyi.emerald.util.AsyncUtils;
import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.util.PreferenceManager;
import com.github.franckyi.emerald.util.UserManager;
import com.jfoenix.assets.JFoenixResources;
import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.tinylog.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public final class EmeraldApp extends Application {
    private static EmeraldApp instance;
    private final String darkThemeStylesheet;
    private final String lightThemeStylesheet;
    private Stage stage;
    private Scene scene;
    private JFXDecorator decorator;
    private MainController mainController;

    private String currentThemeStylesheet;

    public EmeraldApp() {
        this.darkThemeStylesheet = this.getClass().getResource("/view/css/dark.css").toExternalForm();
        this.lightThemeStylesheet = this.getClass().getResource("/view/css/light.css").toExternalForm();
    }

    public static void main(String[] args) {
        Logger.info("Hello world!");
        Application.launch(args);
    }

    public static EmeraldApp getInstance() {
        return instance;
    }

    public Stage getStage() {
        return stage;
    }

    public Scene getScene() {
        return scene;
    }

    public JFXDecorator getDecorator() {
        return decorator;
    }

    public MainController getMainController() {
        return mainController;
    }

    @Override
    public void init() throws Exception {
        Logger.debug("Initializing application");
        instance = this;
        Emerald.getExecutorService().submit(InstanceStorage::reloadInstances);
        Emerald.getExecutorService().submit(UserManager::load);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        Path applicationPath = this.loadApplicationPath();
        if (applicationPath == null) return;
        Logger.debug("Application path: {}", applicationPath.toString());
        Logger.info("Starting application");
        this.loadView();
    }

    @Override
    public void stop() throws Exception {
        Emerald.getExecutorService().shutdown();
        Logger.info("Goodbye world!");
    }

    private Path loadApplicationPath() {
        Path path = PreferenceManager.loadApplicationPath();
        if (path == null) {
            Logger.info("No application path found - opening dialog");
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose an installation path");
            File file = chooser.showDialog(stage);
            if (file != null && file.isDirectory()) {
                PreferenceManager.setApplicationPath(path = file.toPath());
            } else {
                Logger.debug("Invalid path - exiting application");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid path.", ButtonType.CLOSE);
                alert.showAndWait();
            }
        }
        return path;
    }

    private void loadView() {
        mainController = Controller.loadFXML("Main.fxml");
        decorator = new JFXDecorator(stage, mainController.getRoot(), false, true, true);
        decorator.setCustomMaximize(true);
        scene = new Scene(decorator, 761, 656);
        scene.getStylesheets().addAll(
                JFoenixResources.load("css/jfoenix-fonts.css").toExternalForm(),
                JFoenixResources.load("css/jfoenix-design.css").toExternalForm(),
                this.getClass().getResource("/view/css/style.css").toExternalForm());
        this.updateTheme();
        stage.setScene(scene);
        stage.setTitle("Emerald");
        stage.centerOnScreen();
        stage.fullScreenProperty().addListener(new FullScreenFixer());
        stage.focusedProperty().addListener((obs, oldVal, newVal) ->
                this.fixFocus(((HBox) decorator.getChildren().get(0)).getChildren().get(2)));
        Logger.info("Showing application");
        stage.show();
    }

    private void fixFocus(Node node) {
        node.requestFocus();
        this.fixFocus();
    }

    public void fixFocus() {
        Platform.runLater(mainController.getRoot()::requestFocus);
    }

    public void updateTheme() {
        Configuration c = Emerald.getConfiguration();
        scene.getStylesheets().remove(currentThemeStylesheet);
        if (c.getTheme() == Configuration.Theme.DARK) {
            currentThemeStylesheet = darkThemeStylesheet;
        } else if (c.getTheme() == Configuration.Theme.LIGHT) {
            currentThemeStylesheet = lightThemeStylesheet;
        } else {
            Path path = Emerald.getApplicationPath().resolve(
                    String.format("themes%s%s", File.pathSeparator, c.getCustomTheme()));
            if (Files.isRegularFile(path)) {
                currentThemeStylesheet = path.toString();
            } else {
                Logger.error("Custom style {} not found - defaulting to dark theme", c.getCustomTheme());
                currentThemeStylesheet = darkThemeStylesheet;
            }
        }
        scene.getStylesheets().add(currentThemeStylesheet);
    }

    private class FullScreenFixer implements ChangeListener<Boolean> {
        private double oldHeight;

        @Override
        public void changed(ObservableValue<? extends Boolean> obs, Boolean oldVal, Boolean newVal) {
            EmeraldApp.this.fixFocus();
            if (newVal) {
                oldHeight = stage.getHeight();
            } else {
                AsyncUtils.runAfter(320, () -> {
                    stage.setHeight(oldHeight - 1);
                    AsyncUtils.runAfter(10, () -> stage.setHeight(oldHeight));
                });
            }
        }
    }
}
