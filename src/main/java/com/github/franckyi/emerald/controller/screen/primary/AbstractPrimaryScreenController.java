package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.controller.screen.AbstractScreenController;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.Region;

public abstract class AbstractPrimaryScreenController<V extends Region, M> extends AbstractScreenController<V, M> {
    private Node rightHeader;

    public Node getRightHeader() {
        if (rightHeader == null) {
            rightHeader = this.buildRightHeader();
        }
        return rightHeader;
    }

    public abstract MaterialIcon getGlyphIcon();

    public abstract ObservableValue<? extends String> getTitle();

    protected abstract Node buildRightHeader();
}
