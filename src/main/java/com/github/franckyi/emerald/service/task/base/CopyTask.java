package com.github.franckyi.emerald.service.task.base;

import com.github.franckyi.emerald.service.task.EmeraldTask;

import java.io.InputStream;
import java.io.OutputStream;

public class CopyTask extends EmeraldTask<Void> {
    private static final int BUFFER_SIZE = 8192;
    protected InputStream in;
    protected OutputStream out;
    protected int totalSize;

    public CopyTask() {
    }

    public CopyTask(InputStream in, OutputStream out) {
        this(in, out, 0);
    }

    public CopyTask(InputStream in, OutputStream out, int totalSize) {
        this.in = in;
        this.out = out;
        this.totalSize = totalSize;
    }

    @Override
    protected Void call() throws Exception {
        byte[] dataBuffer = new byte[BUFFER_SIZE];
        int downloadedSize = 0;
        int bytesRead;
        while ((bytesRead = in.read(dataBuffer, 0, BUFFER_SIZE)) != -1) {
            out.write(dataBuffer, 0, bytesRead);
            downloadedSize += bytesRead;
            if (totalSize > 0) {
                this.updateProgress(downloadedSize, totalSize);
            }
        }
        in.close();
        out.close();
        return null;
    }
}
