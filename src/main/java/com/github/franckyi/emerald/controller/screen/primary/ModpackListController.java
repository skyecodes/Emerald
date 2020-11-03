package com.github.franckyi.emerald.controller.screen.primary;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ModpackListController extends AbstractPrimaryScreenController<StackPane, Void> {
    @Override
    public MaterialIcon getGlyphIcon() {
        return MaterialIcon.WIDGETS;
    }

    @Override
    public ObservableValue<String> getTitle() {
        return new ReadOnlyStringWrapper("Modpacks");
    }

    @Override
    protected Node buildRightHeader() {
        return new Pane();
    }
}
