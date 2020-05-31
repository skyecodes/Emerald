package com.github.franckyi.emerald.util;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class AsyncUtils {

    public static <T> Task<T> createTask(Callable<T> callable) {
        return new Task<T>() {
            @Override
            protected T call() throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> void runAnd(Supplier<T> supplier, Consumer<T> consumer) {
        Task<T> task = new Task<T>() {
            @Override
            protected T call() {
                return supplier.get();
            }

            @Override
            protected void succeeded() {
                consumer.accept(this.getValue());
            }
        };
        Emerald.getExecutorService().submit(task);
    }

    public static void runAfter(int millis, Runnable runnable) {
        Emerald.getExecutorService().execute(new Task<Void>() {
            @Override
            protected Void call() throws InterruptedException {
                Thread.sleep(millis);
                return null;
            }

            @Override
            protected void succeeded() {
                runnable.run();
            }
        });
    }

}
