package com.github.franckyi.emerald.view.animation;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.screen.ScreenController;
import com.github.franckyi.emerald.controller.screen.primary.PrimaryScreenController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.geometry.Insets;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public final class ScreenAnimation {
    private static EmeraldTimeline currentScreenTimeline, currentPrimaryScreenTimeline;

    public static EmeraldTimeline nextScreen(StackPane root, ScreenController<?, ?> from, ScreenController<?, ?> to) {
        if (currentScreenTimeline != null) return new NoopTimeline();
        EmeraldTimeline timeline = forward(root, from.getRoot(), to.getRoot());
        root.getChildren().add(to.getRoot());
        applyScreenTransition(timeline, root, from, to);
        return timeline;
    }

    public static EmeraldTimeline previousScreen(StackPane root, ScreenController<?, ?> from, ScreenController<?, ?> to) {
        if (currentScreenTimeline != null) return new NoopTimeline();
        EmeraldTimeline timeline = backward(root, from.getRoot(), to.getRoot());
        root.getChildren().add(0, to.getRoot());
        applyScreenTransition(timeline, root, from, to);
        return timeline;
    }

    public static EmeraldTimeline changePrimaryScreen(StackPane root, PrimaryScreenController<?, ?> from, PrimaryScreenController<?, ?> to) {
        if (currentPrimaryScreenTimeline != null) return new NoopTimeline();
        EmeraldTimeline timeline = change(root, from.getRoot(), to.getRoot());
        applyPrimaryScreenTransition(timeline, root, from, to);
        return timeline;
    }

    private static void applyScreenTransition(EmeraldTimeline timeline, StackPane root,
                                              ScreenController<?, ?> from, ScreenController<?, ?> to) {
        from.beforeHiding();
        to.beforeShowing();
        timeline.addListener(() -> {
            root.getChildren().remove(from.getRoot());
            from.afterHiding();
            to.afterShowing();
            EmeraldApp.getInstance().fixFocus();
            currentScreenTimeline = null;
        });
        currentScreenTimeline = timeline;
    }

    private static void applyPrimaryScreenTransition(EmeraldTimeline timeline, StackPane root,
                                                     PrimaryScreenController<?, ?> from, PrimaryScreenController<?, ?> to) {
        from.beforeHiding();
        to.beforeShowing();
        timeline.addListener(() -> {
            root.getChildren().remove(from.getRoot());
            from.afterHiding();
            to.afterShowing();
            currentPrimaryScreenTimeline = null;
        });
        currentPrimaryScreenTimeline = timeline;
    }

    private static BasicTimeline forward(StackPane root, Node from, Node to) {
        StackPane opacityPane = new StackPane();
        opacityPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        opacityPane.setOpacity(0);
        opacityPane.setCacheHint(CacheHint.SPEED);
        root.getChildren().add(opacityPane);
        from.setTranslateX(0);
        to.setTranslateX(root.getWidth());
        KeyValue kvFromTranslate = new KeyValue(from.translateXProperty(), -30, Interpolator.LINEAR);
        KeyValue kvOpacity = new KeyValue(opacityPane.opacityProperty(), 0.5, Interpolator.LINEAR);
        KeyValue kvToTranslate = new KeyValue(to.translateXProperty(), 0, EmeraldInterpolators.STANDARD_EASE);
        KeyFrame kf = new KeyFrame(Duration.millis(250), kvFromTranslate, kvOpacity, kvToTranslate);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> {
            root.getChildren().remove(opacityPane);
            ScreenAnimation.resetAnimations(from);
        });
        timeline.play();
        return timeline;
    }

    private static BasicTimeline backward(StackPane root, Node from, Node to) {
        StackPane opacityPane = new StackPane();
        opacityPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        opacityPane.setOpacity(0.5);
        opacityPane.setCacheHint(CacheHint.SPEED);
        root.getChildren().add(0, opacityPane);
        from.setTranslateX(0);
        to.setTranslateX(-30);
        KeyValue kvFromTranslate = new KeyValue(from.translateXProperty(), root.getWidth(), EmeraldInterpolators.ACCELERATED_EASE);
        KeyValue kvToTranslate = new KeyValue(to.translateXProperty(), 0, Interpolator.LINEAR);
        KeyValue kvOpacity = new KeyValue(opacityPane.opacityProperty(), 0, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kvFromTranslate, kvToTranslate, kvOpacity);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> {
            root.getChildren().remove(opacityPane);
            ScreenAnimation.resetAnimations(from);
        });
        timeline.play();
        return timeline;
    }

    private static void resetAnimations(Node node) {
        node.setTranslateX(0);
        node.setOpacity(1);
    }

    private static BasicTimeline change(StackPane root, Node from, Node to) {
        to.setOpacity(0);
        root.getChildren().add(to);
        KeyValue kvOpacityFrom = new KeyValue(from.opacityProperty(), 0, Interpolator.LINEAR);
        KeyValue kvOpacityTo = new KeyValue(to.opacityProperty(), 1, Interpolator.LINEAR);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kvOpacityFrom, kvOpacityTo);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> from.setOpacity(1));
        timeline.play();
        return timeline;
    }
}
