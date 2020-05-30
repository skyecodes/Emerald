package com.github.franckyi.emerald.controller.screen.primary;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ModListController extends PrimaryScreenController<StackPane, Void> {
    @Override
    public String getTitle() {
        return "Mods";
    }

    @Override
    protected Node buildRightHeader() {
        return new Pane();
    }
}
