package com.github.franckyi.emerald.service.task.base;

import com.github.franckyi.emerald.service.task.EmeraldTask;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

public class LinuxUnzipFileTask extends EmeraldTask<Void> {
    private final Path tarGzFile;
    private final Path outputDir;
    private Path tmp;

    public LinuxUnzipFileTask(Path tarGzFile, Path outputDir) {
        this.tarGzFile = tarGzFile;
        this.outputDir = outputDir;
    }

    @Override
    protected Void call() throws Exception {
        tmp = Files.createTempFile("archive", null);
        this.decompress();
        this.updateProgress(1, 2);
        this.unarchive();
        Files.delete(tmp);
        this.updateProgress(1, 1);
        return null;
    }

    private void decompress() throws IOException {
        GZIPInputStream gis = new GZIPInputStream(Files.newInputStream(tarGzFile));
        OutputStream os = Files.newOutputStream(tmp);
        new CopyTask(gis, os).run();
    }

    private void unarchive() throws IOException, ArchiveException {
        InputStream is = Files.newInputStream(tmp);
        TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
        TarArchiveEntry entry;
        while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
            Path outputFile = outputDir.resolve(entry.getName());
            if (entry.isDirectory()) {
                if (!Files.exists(outputFile)) {
                    Files.createDirectories(outputFile);
                }
            } else {
                OutputStream outputFileStream = Files.newOutputStream(outputFile);
                IOUtils.copy(debInputStream, outputFileStream);
                if (entry.getMode() == 493) {
                    outputFile.toFile().setExecutable(true);
                }
                outputFileStream.close();
            }
        }
        debInputStream.close();
    }
}
