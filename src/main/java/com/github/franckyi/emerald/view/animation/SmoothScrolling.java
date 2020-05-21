package com.github.franckyi.emerald.view.animation;

import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import java.util.function.Function;

public final class SmoothScrolling {
    public static void apply(TreeTableView<?> treeTableView) {
        final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
        final double[] pushes = {1};
        final double[] derivatives = new double[frictions.length];

        Timeline timeline = new Timeline();
        final EventHandler<MouseEvent> dragHandler = event -> timeline.stop();

        treeTableView.skinProperty().addListener((obs0, oldVal0, newVal0) -> {
            Node content = treeTableView.lookup(".clipped-container");
            VirtualScrollBar scrollBar = (VirtualScrollBar) treeTableView.lookup(".scroll-bar:vertical");
            DoubleProperty scrollDirection = scrollBar.valueProperty();
            Function<Bounds, Double> sizeFunc = Bounds::getHeight;

            final EventHandler<ScrollEvent> scrollHandler = event -> {
                if (event.getEventType() == ScrollEvent.SCROLL) {
                    int direction = event.getDeltaY() > 0 ? -1 : 1;
                    if ((direction != 1 || scrollDirection.get() != 1) && (direction != -1 || scrollDirection.get() != 0)) {
                        int length = treeTableView.getRoot().getChildren().size();
                        scrollDirection.set(scrollDirection.get() - 1.6 / (length - 7) * direction);
                        for (int i = 0; i < pushes.length; i++) {
                            derivatives[i] += direction * pushes[i] * 3.0 / length;
                        }
                        if (timeline.getStatus() == Animation.Status.STOPPED) {
                            timeline.play();
                        }
                    }
                    event.consume();
                }
            };
            if (content.getParent() != null) {
                content.getParent().addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                content.getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
            }
            content.parentProperty().addListener((obs, oldVal, newVal) -> {
                if (oldVal != null) {
                    oldVal.removeEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                    oldVal.removeEventHandler(ScrollEvent.ANY, scrollHandler);
                }
                if (newVal != null) {
                    newVal.addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
                    newVal.addEventHandler(ScrollEvent.ANY, scrollHandler);
                }
            });
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(3), (event) -> {
                for (int i = 0; i < derivatives.length; i++) {
                    derivatives[i] *= frictions[i];
                }
                for (int i = 1; i < derivatives.length; i++) {
                    derivatives[i] += derivatives[i - 1];
                }
                double dy = derivatives[derivatives.length - 1];
                double size = sizeFunc.apply(content.getLayoutBounds());
                scrollDirection.set(Math.min(Math.max(scrollDirection.get() + dy / size, 0), 1));
                if (Math.abs(dy) < 0.001) {
                    timeline.stop();
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
        });
    }
}
