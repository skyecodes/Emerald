package com.github.franckyi.emerald.controller.screen.primary;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ResourcePackListController extends PrimaryScreenController<StackPane, Void> {
    @Override
    public String getTitle() {
        return "Resource Packs";
    }

    @Override
    protected Node buildRightHeader() {
        return new Pane();
    }
}
