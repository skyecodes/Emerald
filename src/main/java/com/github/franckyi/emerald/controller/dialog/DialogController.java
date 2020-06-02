package com.github.franckyi.emerald.controller.dialog;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.screen.ScreenController;
import com.jfoenix.controls.JFXDialog;

public abstract class DialogController<M> extends ScreenController<JFXDialog, M> {
    @Override
    protected void initialize() {
        this.getRoot().setOnDialogOpened(e -> this.afterShowing());
        this.getRoot().setOnDialogClosed(e -> this.afterHiding());
    }

    public void open() {
        this.beforeShowing();
        this.getRoot().show(this.getMainController().getRoot());
    }

    public void close() {
        this.beforeHiding();
        this.getRoot().close();
    }

    @Override
    public void afterHiding() {
        super.afterHiding();
        EmeraldApp.getInstance().fixFocus();
    }
}
