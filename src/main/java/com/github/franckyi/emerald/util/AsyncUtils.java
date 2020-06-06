package com.github.franckyi.emerald.util;

import javafx.concurrent.Task;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public final class AsyncUtils {
    public static <T> void runThenUpdate(Callable<T> callable, Consumer<T> consumer) {
        Emerald.getExecutorService().submit(new Task<T>() {
            @Override
            protected T call() throws Exception {
                return callable.call();
            }

            @Override
            protected void succeeded() {
                consumer.accept(this.getValue());
            }
        });
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
