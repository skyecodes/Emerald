package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.ViewController;
import com.github.franckyi.emerald.model.Context;
import com.github.franckyi.emerald.model.SettingsModel;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class MainController implements Controller<Context> {

    @FXML
    private StackPane root;

    private ViewController<InstanceListController> instanceListViewController;
    private ViewController<SettingsController> settingsViewController;

    @Override
    public void initialize(Context model) {
        instanceListViewController = ViewController.loadFXML("InstanceList.fxml", model::getInstances);
        root.getChildren().add(instanceListViewController.getView());
    }

    public void showSettings() {
        if (settingsViewController == null) {
            settingsViewController = ViewController.loadFXML("Settings.fxml", new SettingsModel(this));
            settingsViewController.getView().setTranslateX(root.getWidth());
            root.getChildren().add(settingsViewController.getView());
        }
        settingsViewController.getView().translateXProperty().unbind();
        this.nextAnimate(instanceListViewController.getView(), settingsViewController.getView());
        settingsViewController.getController().initDefaultConfiguration();
    }

    public void hideSettings() {
        this.backAnimate(settingsViewController.getView(), instanceListViewController.getView(),
                (e) -> settingsViewController.getView().translateXProperty().bind(root.widthProperty()));
    }

    public StackPane getRoot() {
        return (StackPane) root.getParent();
    }

    private void nextAnimate(Node from, Node to) {
        this.nextAnimate(from, to, null);
    }

    private void backAnimate(Node from, Node to) {
        this.backAnimate(from, to, null);
    }

    private void nextAnimate(Node from, Node to, EventHandler<ActionEvent> onFinished) {
        this.animate(from, to, -root.getWidth(), onFinished);
    }

    private void backAnimate(Node from, Node to, EventHandler<ActionEvent> onFinished) {
        this.animate(from, to, root.getWidth(), onFinished);
    }

    private void animate(Node from, Node to, double d, EventHandler<ActionEvent> onFinished) {
        from.translateXProperty().unbind();
        to.translateXProperty().unbind();
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(from.translateXProperty(), d, Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.25), kv);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(0.25), kv2);
        timeline.getKeyFrames().addAll(kf, kf2);
        if (onFinished != null) {
            timeline.setOnFinished(onFinished);
        }
        timeline.play();
    }

}
