package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.model.Context;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.function.Function;

public class MainController extends Controller<StackPane, Context> {
    private InstanceListController instanceListController;
    private SettingsController settingsController;
    private NewInstanceController newInstanceController;
    private NewVanillaInstanceController newVanillaInstanceController;

    private LinkedList<MenuController<?, ?>> flow;
    private LinkedList<TransitionDirection> transitionFlow;

    @Override
    protected void initialize() {
        flow = new LinkedList<>();
        transitionFlow = new LinkedList<>();
    }

    @Override
    protected void modelUpdated() {
        instanceListController = Controller.loadFXML("InstanceList.fxml", this.getModel()::getInstances);
        flow.add(instanceListController);
        this.getRoot().getChildren().add(instanceListController.getRoot());
    }

    public void showSettings() {
        if (settingsController == null)
            settingsController = Controller.loadFXML("Settings.fxml");
        this.showNext(settingsController, TransitionDirection.RIGHT);
    }

    public void showNewInstance() {
        if (newInstanceController == null)
            newInstanceController = Controller.loadFXML("NewInstance.fxml");
        this.showNext(newInstanceController, TransitionDirection.TOP);
    }

    public void showNewVanillaInstance() {
        if (newVanillaInstanceController == null)
            newVanillaInstanceController = Controller.loadFXML("NewVanillaInstance.fxml");
        this.showNext(newVanillaInstanceController, TransitionDirection.LEFT);
    }

    public void showHome() {
        flow.subList(1, flow.size() - 1).forEach(c -> {
            flow.remove(c);
            c.getRoot().setTranslateX(0);
            c.getRoot().setTranslateY(0);
        });
        transitionFlow.subList(1, flow.size()).clear();
        showPrevious();
    }

    public void showPrevious() {
        MenuController<?, ?> from = flow.getLast();
        MenuController<?, ?> to = flow.get(flow.size() - 2);
        this.getRoot().getChildren().add(to.getRoot());
        TransitionDirection direction = transitionFlow.getLast().getOpposite();
        Timeline timeline = this.animate(from, to, direction);
        timeline.setOnFinished(e -> {
            this.getRoot().getChildren().remove(from.getRoot());
            from.afterHiding();
            to.afterShowing();
            flow.removeLast();
            transitionFlow.removeLast();
            EmeraldApp.getInstance().fixFocus();
        });
    }

    private void showNext(MenuController<?, ?> to, TransitionDirection direction) {
        MenuController<?, ?> from = flow.getLast();
        this.getRoot().getChildren().add(to.getRoot());
        Timeline timeline = this.animate(from, to, direction);
        timeline.setOnFinished(e -> {
            this.getRoot().getChildren().remove(from.getRoot());
            from.afterHiding();
            to.afterShowing();
            flow.add(to);
            transitionFlow.add(direction);
            EmeraldApp.getInstance().fixFocus();
        });
    }

    private Timeline animate(MenuController<?, ?> from, MenuController<?, ?> to, TransitionDirection direction) {
        direction.initToPos(to.getRoot(), this.getRoot());
        from.beforeHiding();
        to.beforeShowing();
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(direction.translateProperty(from.getRoot()), direction.fromKeyValue(this.getRoot()), Interpolator.EASE_IN);
        KeyValue kv2 = new KeyValue(direction.translateProperty(to.getRoot()), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(.225), kv);
        KeyFrame kf2 = new KeyFrame(Duration.seconds(.225), kv2);
        timeline.getKeyFrames().addAll(kf, kf2);
        timeline.play();
        return timeline;
    }

    private enum TransitionDirection {
        LEFT(Node::translateXProperty, Region::getWidth, root -> -root.getWidth()),
        RIGHT(Node::translateXProperty, region -> -region.getWidth(), Region::getWidth),
        TOP(Node::translateYProperty, Region::getHeight, root -> -root.getHeight()),
        BOTTOM(Node::translateYProperty, region -> -region.getHeight(), Region::getHeight);

        private final Function<Node, DoubleProperty> translateProperty;
        private final Function<Region, Double> fromKeyValue;
        private final Function<Region, Double> initTranslate;

        TransitionDirection(Function<Node, DoubleProperty> translateProperty, Function<Region, Double> fromKeyValue,
                            Function<Region, Double> initTranslate) {
            this.translateProperty = translateProperty;
            this.fromKeyValue = fromKeyValue;
            this.initTranslate = initTranslate;
        }

        public double fromKeyValue(Region region) {
            return fromKeyValue.apply(region);
        }

        public DoubleProperty translateProperty(Node node) {
            return translateProperty.apply(node);
        }

        public void initToPos(Parent to, Region root) {
            translateProperty.apply(to).setValue(initTranslate.apply(root));
        }

        public TransitionDirection getOpposite() {
            switch (this) {
                case LEFT:
                    return RIGHT;
                case RIGHT:
                    return LEFT;
                case TOP:
                    return BOTTOM;
                case BOTTOM:
                    return TOP;
            }
            return null;
        }
    }
}
