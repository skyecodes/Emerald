package com.github.franckyi.emerald;

import com.github.franckyi.emerald.controller.MainController;
import com.github.franckyi.emerald.model.Context;
import com.github.franckyi.emerald.service.init.ContextLoader;
import com.github.franckyi.emerald.util.AsyncUtils;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.github.franckyi.emerald.util.PreferenceManager;
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

public final class EmeraldApp extends Application {
    private static EmeraldApp instance;
    private final String darkThemeStylesheet;
    private final String lightThemeStylesheet;
    private Stage stage;
    private Scene scene;
    private JFXDecorator decorator;
    private ViewController<MainController> mainViewController;

    public EmeraldApp() {
        this.darkThemeStylesheet = this.getClass().getResource("/view/css/dark.css").toExternalForm();
        this.lightThemeStylesheet = this.getClass().getResource("/view/css/light.css").toExternalForm();
    }

    public static void main(String[] args) {
        Logger.debug("Hello world!");
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

    public ViewController<MainController> getMainViewController() {
        return mainViewController;
    }

    @Override
    public void init() throws Exception {
        Logger.debug("Initializing application");
        instance = this;
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        File applicationPath = this.loadApplicationPath();
        if (applicationPath == null) return;
        Logger.debug("Application path: {}", applicationPath.getAbsolutePath());
        EmeraldUtils.init();
        this.loadContext();
        Logger.debug("Starting application");
        this.loadView();
    }

    @Override
    public void stop() throws Exception {
        EmeraldUtils.getExecutorService().shutdown();
        Logger.debug("Goodbye world!");
    }

    private File loadApplicationPath() {
        File path = PreferenceManager.getApplicationPath();
        if (path == null) {
            Logger.info("No application path found - opening dialog");
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Choose an installation path");
            path = chooser.showDialog(stage);
            if (path == null || !path.isDirectory()) {
                Logger.debug("Invalid path - exiting application");
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a valid path.", ButtonType.CLOSE);
                alert.showAndWait();
            } else {
                PreferenceManager.setApplicationPath(path);
            }
        }
        return path;
    }

    private void loadContext() {
        Context context = new Context();
    }

    private void loadView() {
        mainViewController = ViewController.loadFXML("Main.fxml", new ContextLoader());
        decorator = new JFXDecorator(stage, mainViewController.getView());
        decorator.setCustomMaximize(true);
        scene = new Scene(decorator, 720, 480);
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
        Platform.runLater(mainViewController.getView()::requestFocus);
    }

    public void updateTheme() {
        if (EmeraldUtils.getConfiguration().isDarkTheme()) {
            scene.getStylesheets().remove(lightThemeStylesheet);
            scene.getStylesheets().add(darkThemeStylesheet);
        } else {
            scene.getStylesheets().remove(darkThemeStylesheet);
            scene.getStylesheets().add(lightThemeStylesheet);
        }
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
