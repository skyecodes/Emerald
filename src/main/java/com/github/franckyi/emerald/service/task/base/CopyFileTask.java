package com.github.franckyi.emerald.service.task.base;

import java.io.BufferedInputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyFileTask extends CopyTask {
    private final String source;
    private final Path destination;

    public CopyFileTask(String source, Path destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    protected Void call() throws Exception {
        URLConnection connection = this.getClass().getResource(source).openConnection();
        totalSize = connection.getContentLength();
        in = new BufferedInputStream(connection.getInputStream());
        out = Files.newOutputStream(destination);
        return super.call();
    }
}
