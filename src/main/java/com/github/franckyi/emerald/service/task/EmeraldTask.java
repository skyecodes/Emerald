package com.github.franckyi.emerald.service.task;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import org.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class EmeraldTask<V> extends Task<V> {
    private final List<EventHandler<WorkerStateEvent>> onScheduledListeners;
    private final List<EventHandler<WorkerStateEvent>> onRunningListeners;
    private final List<EventHandler<WorkerStateEvent>> onSucceededListeners;
    private final List<EventHandler<WorkerStateEvent>> onCancelledListeners;
    private final List<EventHandler<WorkerStateEvent>> onFailedListeners;

    private final List<Runnable> onLastTaskSucceededListeners;

    protected EmeraldTask() {
        onScheduledListeners = new ArrayList<>();
        onRunningListeners = new ArrayList<>();
        onSucceededListeners = new ArrayList<>();
        onCancelledListeners = new ArrayList<>();
        onFailedListeners = new ArrayList<>();
        onLastTaskSucceededListeners = new ArrayList<>();
        this.setOnScheduled(e -> onScheduledListeners.forEach(listener -> listener.handle(e)));
        this.setOnRunning(e -> onRunningListeners.forEach(listener -> listener.handle(e)));
        this.setOnSucceeded(e -> onSucceededListeners.forEach(listener -> listener.handle(e)));
        this.setOnCancelled(e -> onCancelledListeners.forEach(listener -> listener.handle(e)));
        this.setOnFailed(e -> onFailedListeners.forEach(listener -> listener.handle(e)));
        onFailedListeners.add(e -> Logger.error(this.getException(), "Error in task {}", this.getClass().getSimpleName()));
    }

    public List<EventHandler<WorkerStateEvent>> getOnScheduledListeners() {
        return onScheduledListeners;
    }

    public List<EventHandler<WorkerStateEvent>> getOnRunningListeners() {
        return onRunningListeners;
    }

    public List<EventHandler<WorkerStateEvent>> getOnSucceededListeners() {
        return onSucceededListeners;
    }

    public List<EventHandler<WorkerStateEvent>> getOnCancelledListeners() {
        return onCancelledListeners;
    }

    public List<EventHandler<WorkerStateEvent>> getOnFailedListeners() {
        return onFailedListeners;
    }

    public List<Runnable> getOnLastTaskSucceededListeners() {
        return onLastTaskSucceededListeners;
    }
}
