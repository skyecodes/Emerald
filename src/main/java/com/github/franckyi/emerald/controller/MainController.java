package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.controller.screen.MenuController;
import com.github.franckyi.emerald.controller.screen.NewInstanceController;
import com.github.franckyi.emerald.controller.screen.NewVanillaInstanceController;
import com.github.franckyi.emerald.controller.screen.ScreenController;
import com.github.franckyi.emerald.util.Minecraft;
import com.github.franckyi.emerald.view.animation.EmeraldTimeline;
import com.github.franckyi.emerald.view.animation.ScreenAnimation;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;

public class MainController extends Controller<StackPane, Void> {
    private MenuController menuController;
    private NewInstanceController newInstanceController;
    private NewVanillaInstanceController newVanillaInstanceController;

    private LinkedList<ScreenController<?, ?>> flow;

    @Override
    protected void initialize() {
        flow = new LinkedList<>();
        menuController = Controller.loadFXML("screen/Menu.fxml");
        flow.add(menuController);
        this.getRoot().getChildren().add(menuController.getRoot());
    }

    public MenuController getMenuController() {
        return menuController;
    }

    public EmeraldTimeline showNewInstance() {
        if (newInstanceController == null) {
            newInstanceController = Controller.loadFXML("screen/NewInstance.fxml");
        }
        return this.showNext(newInstanceController);
    }

    public EmeraldTimeline showNewVanillaInstance() {
        if (newVanillaInstanceController == null) {
            newVanillaInstanceController = Controller.loadFXML("screen/NewVanillaInstance.fxml", Minecraft::getVersionManifest);
        }
        return this.showNext(newVanillaInstanceController);
    }

    private EmeraldTimeline showNext(ScreenController<?, ?> to) {
        ScreenController<?, ?> from = flow.getLast();
        EmeraldTimeline timeline = ScreenAnimation.nextScreen(this.getRoot(), from, to);
        timeline.addListener(() -> flow.add(to));
        timeline.play();
        return timeline;
    }

    public EmeraldTimeline showPrevious() {
        ScreenController<?, ?> from = flow.getLast();
        ScreenController<?, ?> to = flow.get(flow.size() - 2);
        EmeraldTimeline timeline = ScreenAnimation.previousScreen(this.getRoot(), from, to);
        timeline.addListener(flow::removeLast);
        timeline.play();
        return timeline;
    }

    public EmeraldTimeline showHome() {
        flow.subList(1, flow.size() - 1).forEach(c -> {
            flow.remove(c);
            c.getRoot().setTranslateX(0);
            c.getRoot().setTranslateY(0);
        });
        return this.showPrevious();
    }
}
