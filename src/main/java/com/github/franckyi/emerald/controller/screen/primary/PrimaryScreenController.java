package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.EmeraldApp;
import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.MainController;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class PrimaryScreenController<V extends Region, M> extends Controller<V, M> {
    private MainController mainController;
    private Node rightHeader;

    public MainController getMainController() {
        if (mainController == null) {
            mainController = EmeraldApp.getInstance().getMainController();
        }
        return mainController;
    }

    public Node getRightHeader() {
        if (rightHeader == null) {
            rightHeader = this.buildRightHeader();
        }
        return rightHeader;
    }

    public void onShow() {
    }

    public void onHide() {
    }

    public abstract String getTitle();

    protected abstract Node buildRightHeader();
}
