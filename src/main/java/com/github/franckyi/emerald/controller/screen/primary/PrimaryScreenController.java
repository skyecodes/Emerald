package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.controller.screen.ScreenController;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class PrimaryScreenController<V extends Region, M> extends ScreenController<V, M> {
    private Node rightHeader;

    public Node getRightHeader() {
        if (rightHeader == null) {
            rightHeader = this.buildRightHeader();
        }
        return rightHeader;
    }

    public abstract String getTitle();

    protected abstract Node buildRightHeader();
}
