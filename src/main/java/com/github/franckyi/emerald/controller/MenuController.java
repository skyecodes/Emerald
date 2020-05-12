package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.EmeraldApp;
import javafx.scene.layout.Region;

public abstract class MenuController<V extends Region, M> extends Controller<V, M> {
    private MainController mainController;
    private Runnable beforeShowing, afterShowing, beforeHiding, afterHiding;

    public MainController getMainController() {
        if (mainController == null) {
            mainController = EmeraldApp.getInstance().getMainController();
        }
        return mainController;
    }

    public void beforeShowing() {
        if (beforeShowing != null) beforeShowing.run();
    }

    public void setBeforeShowing(Runnable beforeShowing) {
        this.beforeShowing = beforeShowing;
    }

    public void afterShowing() {
        if (afterShowing != null) afterShowing.run();
    }

    public void setAfterShowing(Runnable afterShowing) {
        this.afterShowing = afterShowing;
    }

    public void beforeHiding() {
        if (beforeHiding != null) beforeHiding.run();
    }

    public void setBeforeHiding(Runnable beforeHiding) {
        this.beforeHiding = beforeHiding;
    }

    public void afterHiding() {
        this.getRoot().setTranslateX(0);
        this.getRoot().setTranslateY(0);
        if (afterHiding != null) afterHiding.run();
    }

    public void setAfterHiding(Runnable afterHiding) {
        this.afterHiding = afterHiding;
    }
}
