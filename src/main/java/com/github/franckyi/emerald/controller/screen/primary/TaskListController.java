package com.github.franckyi.emerald.controller.screen.primary;

import com.github.franckyi.emerald.controller.Controller;
import com.github.franckyi.emerald.controller.partial.TaskController;
import com.github.franckyi.emerald.service.task.EmeraldTask;
import com.github.franckyi.emerald.util.Emerald;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class TaskListController extends PrimaryScreenController<ListView<EmeraldTask<?>>, ObservableList<EmeraldTask<?>>> {
    @Override
    protected void initialize() {
        this.getRoot().setCellFactory(taskListView -> new TaskCell());
    }

    @Override
    public String getTitle() {
        return "Tasks";
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
                    taskController = Controller.loadFXML("partial/Task.fxml", task);
                }
                this.setGraphic(taskController.getRoot());
            }
        }
    }
}
