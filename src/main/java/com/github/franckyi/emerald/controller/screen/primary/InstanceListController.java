package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.controller.partial.InstanceButtonController;
import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXScrollPane;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class InstanceListController extends AbstractPrimaryScreenController<ScrollPane, ObservableList<Instance>> {
    @FXML
    private JFXMasonryPane instanceListPane;

    private List<InstanceButtonController> controllerList;

    @Override
    protected void initialize() {
        controllerList = new ArrayList<>();
        JFXScrollPane.smoothScrolling(this.getRoot());
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        this.getRoot().setVvalue(0);
    }

    @Override
    protected void modelUpdated() {
        this.update();
        this.getModel().addListener((InvalidationListener) o -> this.update());
    }

    private void update() {
        int i = 0;
        instanceListPane.getChildren().clear();
        for (Instance instance : this.getModel()) {
            InstanceButtonController c;
            if (controllerList.size() == i) {
                c = AbstractController.loadFXML("partial/InstanceButton.fxml", instance);
                controllerList.add(c);
            } else {
                c = controllerList.get(i);
                c.setModel(instance);
            }
            instanceListPane.getChildren().add(c.getRoot());
            i++;
        }
        this.fixScrollBar();
    }

    private void fixScrollBar() {
        Platform.runLater(instanceListPane::requestLayout);
    }

    @Override
    public MaterialIcon getGlyphIcon() {
        return MaterialIcon.DNS;
    }

    @Override
    public ObservableValue<String> getTitle() {
        return new ReadOnlyStringWrapper("Instances");
    }

    @Override
    public Node buildRightHeader() {
        MaterialIconView icon = new MaterialIconView(MaterialIcon.ADD);
        StackPane pane = new StackPane(icon);
        JFXRippler rippler = new JFXRippler(pane);
        rippler.getStyleClass().add("navigation-button");
        rippler.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                this.getMainController().showNewInstance();
            }
        });
        return rippler;
    }
}
