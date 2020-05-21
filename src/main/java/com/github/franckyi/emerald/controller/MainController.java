package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.service.init.ContextLoader;
import com.github.franckyi.emerald.service.task.instance.InstanceCreatorTask;
import com.github.franckyi.emerald.service.task.launcher.LauncherSetupTask;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.github.franckyi.emerald.util.Minecraft;
import com.github.franckyi.emerald.util.SystemUtils;
import com.github.franckyi.emerald.view.animation.EmeraldTimeline;
import com.github.franckyi.emerald.view.animation.MenuAnimation;
import javafx.scene.layout.StackPane;

import java.util.LinkedList;

public class MainController extends Controller<StackPane, Void> {
    private InstanceListController instanceListController;
    private SettingsController settingsController;
    private NewInstanceController newInstanceController;
    private NewVanillaInstanceController newVanillaInstanceController;
    private ProgressController progressController;

    private LinkedList<MenuController<?, ?>> flow;

    @Override
    protected void initialize() {
        flow = new LinkedList<>();
        instanceListController = Controller.loadFXML("InstanceList.fxml", ContextLoader::loadInstances);
        settingsController = Controller.loadFXML("Settings.fxml", EmeraldUtils.getConfiguration());
        newInstanceController = Controller.loadFXML("NewInstance.fxml");
        newVanillaInstanceController = Controller.loadFXML("NewVanillaInstance.fxml", Minecraft::getVersionManifest);
        //progressController = Controller.loadFXML("Progress.fxml");
        flow.add(instanceListController);
        this.getRoot().getChildren().add(instanceListController.getRoot());
    }

    public EmeraldTimeline showSettings() {
        return this.showNext(settingsController);
    }

    public EmeraldTimeline showNewInstance() {
        return this.showNext(newInstanceController);
    }

    public EmeraldTimeline showNewVanillaInstance() {
        return this.showNext(newVanillaInstanceController);
    }

    public EmeraldTimeline showProgress() {
        return this.showNext(progressController);
    }

    public EmeraldTimeline showHome() {
        flow.subList(1, flow.size() - 1).forEach(c -> {
            flow.remove(c);
            c.getRoot().setTranslateX(0);
            c.getRoot().setTranslateY(0);
        });
        return this.showPrevious();
    }

    public EmeraldTimeline showPrevious() {
        MenuController<?, ?> from = flow.getLast();
        MenuController<?, ?> to = flow.get(flow.size() - 2);
        EmeraldTimeline timeline = MenuAnimation.previousMenu(this, from, to);
        timeline.addListener(flow::removeLast);
        timeline.play();
        return timeline;
    }

    private EmeraldTimeline showNext(MenuController<?, ?> to) {
        MenuController<?, ?> from = flow.getLast();
        EmeraldTimeline timeline = MenuAnimation.nextMenu(this, from, to);
        timeline.addListener(() -> flow.add(to));
        timeline.play();
        return timeline;
    }

    public void createNewInstance(InstanceCreatorTask task) {
        if (ContextLoader.isLauncherInitialized()) {
            progressController.schedule(LauncherSetupTask.create(SystemUtils.getOS()));
        }
    }
}
