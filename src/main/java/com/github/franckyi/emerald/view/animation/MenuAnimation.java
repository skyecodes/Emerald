package com.github.franckyi.emerald.view.animation;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.MainController;
import com.github.franckyi.emerald.controller.MenuController;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public final class MenuAnimation {
    private static EmeraldTimeline currentTimeline;

    public static EmeraldTimeline nextMenu(MainController mc, MenuController<?, ?> from, MenuController<?, ?> to) {
        if (currentTimeline != null) return new NoopTimeline();
        EmeraldTimeline timeline = forward(mc.getRoot(), from.getRoot(), to.getRoot());
        applyMenuTransition(timeline, mc, from, to);
        return timeline;
    }

    public static EmeraldTimeline previousMenu(MainController mc, MenuController<?, ?> from, MenuController<?, ?> to) {
        if (currentTimeline != null) return new NoopTimeline();
        EmeraldTimeline timeline = backward(mc.getRoot(), from.getRoot(), to.getRoot());
        applyMenuTransition(timeline, mc, from, to);
        return timeline;
    }

    private static void applyMenuTransition(EmeraldTimeline timeline, MainController mc,
                                            MenuController<?, ?> from, MenuController<?, ?> to) {
        mc.getRoot().getChildren().add(to.getRoot());
        from.beforeHiding();
        to.beforeShowing();
        timeline.addListener(() -> {
            mc.getRoot().getChildren().remove(from.getRoot());
            from.afterHiding();
            to.afterShowing();
            EmeraldApp.getInstance().fixFocus();
            currentTimeline = null;
        });
        currentTimeline = timeline;
    }

    private static BasicTimeline forward(StackPane root, Node from, Node to) {
        from.setTranslateX(0);
        from.setOpacity(1); // TODO use something else other than opacity, doesn't work well with light theme
        to.setTranslateX(root.getWidth());
        KeyValue kvFromTranslate = new KeyValue(from.translateXProperty(), -30, Interpolator.EASE_IN);
        KeyValue kvFromOpacity = new KeyValue(from.opacityProperty(), 0.1, Interpolator.EASE_IN);
        KeyValue kvToTranslate = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(250), kvFromTranslate, kvFromOpacity, kvToTranslate);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> MenuAnimation.resetAnimations(from));
        timeline.play();
        return timeline;
    }

    private static BasicTimeline backward(StackPane root, Node from, Node to) {
        from.setTranslateX(0);
        to.setTranslateX(-30);
        to.setOpacity(0.1);
        KeyValue kvFromTranslate = new KeyValue(from.translateXProperty(), root.getWidth(), Interpolator.EASE_OUT);
        KeyValue kvToTranslate = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_OUT);
        KeyValue kvToOpacity = new KeyValue(to.opacityProperty(), 1, Interpolator.EASE_OUT);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kvFromTranslate, kvToTranslate, kvToOpacity);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> MenuAnimation.resetAnimations(from));
        timeline.play();
        return timeline;
    }

    private static BasicTimeline slideRight(StackPane root, Node from, Node to) {
        from.setTranslateX(0);
        to.setTranslateX(root.getWidth());
        KeyValue kvFrom = new KeyValue(from.translateXProperty(), -root.getWidth(), Interpolator.EASE_IN);
        KeyValue kvTo = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(250), kvFrom, kvTo);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> MenuAnimation.resetAnimations(from));
        timeline.play();
        return timeline;
    }

    private static BasicTimeline slideLeft(StackPane root, Node from, Node to) {
        from.setTranslateX(0);
        to.setTranslateX(-root.getWidth());
        KeyValue kvFrom = new KeyValue(from.translateXProperty(), root.getWidth(), Interpolator.EASE_IN);
        KeyValue kvTo = new KeyValue(to.translateXProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.millis(200), kvFrom, kvTo);
        BasicTimeline timeline = new BasicTimeline(kf);
        timeline.addListener(() -> MenuAnimation.resetAnimations(from));
        timeline.play();
        return timeline;
    }

    private static void resetAnimations(Node node) {
        node.setTranslateX(0);
        node.setOpacity(1);
    }
}
