package org.contrum.abbys.util;

import lombok.Builder;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

@Builder
public class DownloadFileThread extends Thread {

    private final String urlString;
    private final Path path;
    private final ProgressReportHandler progressReportHandler;
    private final ErrorHandler errorHandler;
    private final FinishedHandler finishedHandler;

    private DownloadFileThread(String urlString, Path path, ProgressReportHandler progressReportHandler, ErrorHandler errorHandler, FinishedHandler finishedHandler) {
        this.urlString = urlString;
        this.path = path;
        this.progressReportHandler = progressReportHandler;
        this.errorHandler = errorHandler;
        this.finishedHandler = finishedHandler;
    }

    @Override
    public void run() {
        long reportTimer = System.currentTimeMillis() + 1500L;

        HttpURLConnection connection;
        BufferedInputStream input;
        FileOutputStream output;
        try {
            File file = path.toFile();
            long downloadedBytes = file.exists() ? file.length() : 0;

            URL url = new URL(urlString);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            long totalFileLength = connection.getContentLengthLong();
            connection.disconnect();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Range", "bytes=" + downloadedBytes + "-" + totalFileLength);

            input = new BufferedInputStream(connection.getInputStream());
            output = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read;
            long totalRead = downloadedBytes;

            while ((read = input.read(buffer, 0, 1024)) != -1) {
                totalRead += read;

                if (System.currentTimeMillis() > reportTimer) {
                    progressReportHandler.report((int) (totalRead * 100 / totalFileLength));
                    reportTimer = System.currentTimeMillis() + 1500L;
                }

                output.write(buffer, 0, read);
            }

            connection.disconnect();
            input.close();
            output.close();

            finishedHandler.finished(totalRead);
        } catch (Exception ex) {
            if (errorHandler != null)
                errorHandler.error(ex);
            else
                ex.printStackTrace();
        }
    }

    public interface ProgressReportHandler {
        void report(int progress);
    }

    public interface FinishedHandler {
        void finished(long bytesDownloaded);
    }

    public interface ErrorHandler {
        void error(Throwable throwable);
    }


}
