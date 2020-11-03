package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.controller.AbstractController;
import com.github.franckyi.emerald.controller.partial.TaskController;
import com.github.franckyi.emerald.service.task.EmeraldTask;
import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.util.concurrent.FutureTask;

public class TaskListController extends AbstractPrimaryScreenController<ListView<EmeraldTask<?>>, ObservableList<EmeraldTask<?>>> {
    @Override
    protected void initialize() {
        this.getRoot().setCellFactory(taskListView -> new TaskCell());
    }

    @Override
    public MaterialIcon getGlyphIcon() {
        return MaterialIcon.GET_APP;
    }

    @Override
    public ObservableValue<String> getTitle() {
        return new ReadOnlyStringWrapper("Tasks");
    }

    @Override
    protected Node buildRightHeader() {
        return new Pane();
    }

    public void submit(EmeraldTask<?> task) {
        this.getModel().add(task);
        this.getRoot().getItems().add(task);
        task.getOnSucceededListeners().add(e -> {
            this.getModel().remove(task);
            if (this.getModel().isEmpty()) {
                task.getOnLastTaskSucceededListeners().forEach(Runnable::run);
            }
        });
        Emerald.getExecutorService().submit(task);
    }

    @Override
    public void afterHiding() {
        super.afterHiding();
        if (this.getModel().isEmpty()) {
            this.getRoot().getItems().removeIf(FutureTask::isDone);
        }
    }

    private static class TaskCell extends ListCell<EmeraldTask<?>> {
        private TaskController taskController;

        @Override
        protected void updateItem(EmeraldTask<?> task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                this.setGraphic(null);
            } else {
                if (taskController != null) {
                    taskController.setModel(task);
                } else {
                    taskController = AbstractController.loadFXML("partial/Task.fxml", task);
                }
                this.setGraphic(taskController.getRoot());
            }
        }
    }
}
