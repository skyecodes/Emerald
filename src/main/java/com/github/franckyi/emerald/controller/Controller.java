package com.github.franckyi.emerald.controller;

import com.github.franckyi.emerald.util.Emerald;
import com.github.franckyi.emerald.util.MagicTask;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Region;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public abstract class Controller<V extends Region, M> {

    private final ObjectProperty<M> model;
    private V root;

    protected Controller() {
        model = new SimpleObjectProperty<>();
        model.addListener((obs, oldVal, newVal) -> this.modelUpdated());
    }

    public static <C extends Controller<V, M>, V extends Region, M> C loadFXML(String file) {
        return Controller.loadFXML(file, (M) null);
    }

    public static <C extends Controller<V, M>, V extends Region, M> C loadFXML(String file, M model) {
        return loadFXML(file, url -> {
            FXMLLoader loader = new FXMLLoader(url);
            V root = loader.load();
            C controller = loader.getController();
            controller.setRoot(root);
            controller.initialize();
            controller.setModel(model);
            return controller;
        });
    }

    public static <C extends Controller<V, M>, V extends Region, M> C loadFXML(String file, Callable<M> model) {
        return loadFXML(file, url -> {
            MagicTask<M> task = new MagicTask<>(model);
            Emerald.getExecutorService().submit(task);
            FXMLLoader loader = new FXMLLoader(url);
            V root = loader.load();
            C controller = loader.getController();
            controller.setRoot(root);
            controller.initialize();
            task.then(controller::setModel);
            return controller;
        });
    }

    private static <C extends Controller<V, M>, V extends Region, M> C loadFXML(String file, Loader<C> loader) {
        URL resource = Controller.class.getResource("/view/fxml/" + file);
        if (!file.startsWith("partial/")) {
            Logger.debug("Loading FXML view \"{}\"", file);
        }
        try {
            return loader.load(resource);
        } catch (IOException e) {
            Logger.error(e, String.format("Couldn't load FXML view \"%s\"", file));
            return null;
        }
    }

    public V getRoot() {
        return root;
    }

    void setRoot(V root) {
        this.root = root;
    }

    public M getModel() {
        return model.get();
    }

    public ObjectProperty<M> modelProperty() {
        return model;
    }

    public void setModel(M model) {
        this.model.set(model);
    }

    protected void initialize() {
    }

    protected void modelUpdated() {
    }

    @FunctionalInterface
    private interface Loader<C extends Controller<?, ?>> {
        C load(URL url) throws IOException;
    }

}
