package com.github.franckyi.emerald.service.task.base;

import javafx.concurrent.Task;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.zip.GZIPInputStream;

public class LinuxUnzipFileTask extends Task<Void> {
    private final File tarGzFile;
    private final File outputDir;
    private final File tmp = new File("com.github.franckyi.emerald.temp");

    public LinuxUnzipFileTask(File tarGzFile, File outputDir) {
        this.tarGzFile = tarGzFile;
        this.outputDir = outputDir;
    }

    @Override
    protected Void call() throws Exception {
        this.decompress();
        this.unarchive();
        tarGzFile.delete();
        tmp.delete();
        return null;
    }

    private void decompress() throws IOException {
        FileInputStream fis = new FileInputStream(tarGzFile);
        GZIPInputStream gis = new GZIPInputStream(fis);
        FileOutputStream fos = new FileOutputStream(tmp);
        IOUtils.copy(gis, fos);
        fos.close();
        gis.close();
    }

    private void unarchive() throws IOException, ArchiveException {
        final InputStream is = new FileInputStream(tmp);
        final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
        TarArchiveEntry entry;
        while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
            final File outputFile = new File(outputDir, entry.getName());
            if (entry.isDirectory()) {
                if (!outputFile.exists()) {
                    if (!outputFile.mkdirs()) {
                        throw new IllegalStateException(String.format("Couldn't create directory %s.", outputFile.getAbsolutePath()));
                    }
                }
            } else {
                final OutputStream outputFileStream = new FileOutputStream(outputFile);
                IOUtils.copy(debInputStream, outputFileStream);
                if (entry.getMode() == 493) {
                    outputFile.setExecutable(true);
                }
                outputFileStream.close();
            }
        }
        debInputStream.close();
    }
}
