package com.github.franckyi.emerald.service.task.base;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class DownloadFileTask extends CopyTask {
    private final String source;
    private final Path destination;

    public DownloadFileTask(String source, Path destination) {
        this.source = source;
        this.destination = destination;
    }

    @Override
    protected Void call() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(source).openConnection();
        totalSize = connection.getContentLength();
        in = new BufferedInputStream(connection.getInputStream());
        out = new FileOutputStream(destination.toFile());
        return super.call();
    }

}
