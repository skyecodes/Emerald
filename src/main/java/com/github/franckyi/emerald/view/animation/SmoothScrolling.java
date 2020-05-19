package com.github.franckyi.emerald.view.animation;

import com.sun.javafx.scene.control.skin.VirtualScrollBar;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import java.util.function.Function;

public final class SmoothScrolling {
    private static final double[] frictions = {0.99, 0.1, 0.05, 0.04, 0.03, 0.02, 0.01, 0.04, 0.01, 0.008, 0.008, 0.008, 0.008, 0.0006, 0.0005, 0.00003, 0.00001};
    private static final double[] pushes = {1};
    private static final double[] derivatives = new double[frictions.length];
    private static final Timeline timeline = new Timeline();
    private static final EventHandler<MouseEvent> dragHandler = event -> timeline.stop();
    private static final EventHandler<ScrollEvent> defaultScrollHandler = event -> {
        if (event.getEventType() == ScrollEvent.SCROLL) {
            int direction = event.getDeltaY() > 0 ? -1 : 1;
            for (int i = 0; i < pushes.length; i++) {
                derivatives[i] += direction * pushes[i];
            }
            if (timeline.getStatus() == Animation.Status.STOPPED) {
                timeline.play();
            }
            event.consume();
        }
    };

    public static void apply(ScrollPane scrollPane) {
        doApply(scrollPane.getContent(), scrollPane.vvalueProperty(), Bounds::getHeight, defaultScrollHandler);
    }

    public static void apply(TreeTableView<?> treeTableView) {
        treeTableView.skinProperty().addListener((obs, oldVal, newVal) -> {
            Node content = treeTableView.lookup(".clipped-container");
            VirtualScrollBar scrollBar = (VirtualScrollBar) treeTableView.lookup(".scroll-bar:vertical");
            DoubleProperty scrollDirection = scrollBar.valueProperty();
            doApply(content, scrollDirection, Bounds::getHeight, event -> {
                if (event.getEventType() == ScrollEvent.SCROLL) {
                    int direction = event.getDeltaY() > 0 ? -1 : 1;
                    if ((direction != 1 || scrollDirection.get() != 1) && (direction != -1 || scrollDirection.get() != 0)) {
                        int length = treeTableView.getRoot().getChildren().size();
                        scrollDirection.set(scrollDirection.get() - (1.8 / length) * direction);
                        for (int i = 0; i < pushes.length; i++) {
                            derivatives[i] += direction * pushes[i] * 3 / length;
                        }
                        if (timeline.getStatus() == Animation.Status.STOPPED) {
                            timeline.play();
                        }
                    }
                    event.consume();
                }
            });
        });
    }

    private static void doApply(Node content, DoubleProperty scrollDriection, Function<Bounds, Double> sizeFunc,
                                EventHandler<ScrollEvent> scrollHandler) {
        if (content.getParent() != null) {
            content.getParent().addEventHandler(MouseEvent.DRAG_DETECTED, dragHandler);
            content.getParent().addEventHandler(ScrollEvent.ANY, scrollHandler);
        }
        content.parentProperty().addListener((o, oldVal, newVal) -> {
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
            scrollDriection.set(Math.min(Math.max(scrollDriection.get() + dy / size, 0), 1));
            if (Math.abs(dy) < 0.001) {
                timeline.stop();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
    }
}
