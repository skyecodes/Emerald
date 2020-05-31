package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.model.Instance;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXRippler;
import com.jfoenix.controls.JFXScrollPane;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.StackPane;

import java.util.List;

public class InstanceListController extends PrimaryScreenController<ScrollPane, List<Instance>> {
    @FXML
    private JFXMasonryPane instanceListPane;

    @Override
    protected void initialize() {
        JFXScrollPane.smoothScrolling(this.getRoot());
    }

    @Override
    public void beforeShowing() {
        super.beforeShowing();
        this.getRoot().setVvalue(0);
    }

    @Override
    protected void modelUpdated() {
        instanceListPane.getChildren().clear();
        this.getModel().stream().map(InstanceButton::new).forEach(instanceListPane.getChildren()::add);
        for (int i = 0; i < 50; i++) {
            instanceListPane.getChildren().add(new BlankButton());
        }
        // fix required to show scrollbar
        instanceListPane.parentProperty().addListener(((obs, oldVal, newVal) -> Platform.runLater(instanceListPane::requestLayout)));
    }

    @Override
    public String getTitle() {
        return "Instances";
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

    private static class InstanceButton extends JFXButton {
        public InstanceButton(Instance instance) {

        }
    }

    private static class BlankButton extends JFXButton {
        public BlankButton() {
            this.setPrefSize(120, 120);
            this.getStyleClass().addAll("button-surface", "instance-list-button");
        }
    }

}
