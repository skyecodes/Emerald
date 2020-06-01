package com.github.franckyi.emerald.util;

import javafx.concurrent.Task;
import org.tinylog.Logger;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class MagicTask<V> extends Task<V> {
    private final Callable<V> callable;
    private V value;

    public MagicTask(Callable<V> callable) {
        this.callable = callable;
    }

    @Override
    protected V call() throws Exception {
        try {
            return value = callable.call();
        } catch (Exception e) {
            Logger.error(e);
            throw e;
        }
    }

    public V getTaskValue() {
        return value;
    }

    public void then(Consumer<V> consumer) {
        if (value == null) {
            this.setOnSucceeded(e -> consumer.accept(this.getTaskValue()));
        } else {
            consumer.accept(value);
        }
    }


}
