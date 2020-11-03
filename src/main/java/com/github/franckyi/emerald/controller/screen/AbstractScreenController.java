package com.github.franckyi.emerald.controller.screen;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.controller.MainController;
import javafx.scene.CacheHint;
import javafx.scene.layout.Region;

public abstract class AbstractScreenController<V extends Region, M> extends AbstractController<V, M> {
    private MainController mainController;
    private Runnable beforeShowing, afterShowing, beforeHiding, afterHiding;

    public MainController getMainController() {
        if (mainController == null) {
            mainController = EmeraldApp.getInstance().getMainController();
        }
        return mainController;
    }

    public void beforeShowing() {
        this.getRoot().setCacheHint(CacheHint.SPEED);
        if (beforeShowing != null) beforeShowing.run();
    }

    public void setBeforeShowing(Runnable beforeShowing) {
        this.beforeShowing = beforeShowing;
    }

    public void afterShowing() {
        this.getRoot().setCacheHint(CacheHint.DEFAULT);
        if (afterShowing != null) afterShowing.run();
    }

    public void setAfterShowing(Runnable afterShowing) {
        this.afterShowing = afterShowing;
    }

    public void beforeHiding() {
        this.getRoot().setCacheHint(CacheHint.SPEED);
        if (beforeHiding != null) beforeHiding.run();
    }

    public void setBeforeHiding(Runnable beforeHiding) {
        this.beforeHiding = beforeHiding;
    }

    public void afterHiding() {
        this.getRoot().setCacheHint(CacheHint.DEFAULT);
        if (afterHiding != null) afterHiding.run();
    }

    public void setAfterHiding(Runnable afterHiding) {
        this.afterHiding = afterHiding;
    }
}
