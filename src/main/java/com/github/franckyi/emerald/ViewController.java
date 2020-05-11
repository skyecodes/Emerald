package com.github.franckyi.emerald;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.util.EmeraldUtils;
import com.github.franckyi.emerald.util.MagicTask;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.tinylog.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;

public class ViewController<C extends Controller<?>> {
    private final Node view;
    private final C controller;

    protected ViewController(Node view, C controller) {
        this.view = view;
        this.controller = controller;
    }

    public static <C extends Controller<Void>> ViewController<C> loadFXML(String file) {
        return loadFXML(file, (Void) null);
    }

    public static <C extends Controller<M>, M> ViewController<C> loadFXML(String file, M model) {
        return loadFXML(file, url -> {
            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();
            C controller = loader.getController();
            controller.initialize(model);
            return new ViewController<>(view, controller);
        });
    }

    public static <C extends Controller<M>, M> ViewController<C> loadFXML(String file, Callable<M> model) {
        return loadFXML(file, url -> {
            MagicTask<M> task = new MagicTask<>(model);
            EmeraldUtils.getExecutorService().submit(task);
            FXMLLoader loader = new FXMLLoader(url);
            Node view = loader.load();
            C controller = loader.getController();
            task.then(controller::initialize);
            return new ViewController<>(view, controller);
        });
    }

    private static <C extends Controller<M>, M> ViewController<C> loadFXML(String file, Loader<C> loader) {
        URL resource = ViewController.class.getResource("/view/fxml/" + file);
        Logger.debug("Loading FXML view {}", resource.toExternalForm());
        try {
            return loader.load(resource);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Couldn't load FXML view %s", resource.toExternalForm()), e);
        }
    }

    public Node getView() {
        return view;
    }

    public C getController() {
        return controller;
    }

    @FunctionalInterface
    private interface Loader<C extends Controller<?>> {
        ViewController<C> load(URL url) throws IOException;
    }
}
