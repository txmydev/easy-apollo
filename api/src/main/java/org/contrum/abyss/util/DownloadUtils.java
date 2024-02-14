package org.contrum.abyss.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;

public class DownloadUtils {

    public static DownloadResult download(
            String urlString,
            Path path,
            DownloadReportHandler downloadReportHandler,
            DownloadErrorHandler downloadErrorHandler
    ) {

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
                    downloadReportHandler.report((int) (totalRead * 100 / totalFileLength));
                    reportTimer = System.currentTimeMillis() + 1500L;
                }

                output.write(buffer, 0, read);
            }

            connection.disconnect();
            input.close();
            output.close();

            return new DownloadResult(totalRead, true);
        } catch (Exception ex) {
            if (downloadErrorHandler != null)
                downloadErrorHandler.error(ex);
            else
                ex.printStackTrace();

            return new DownloadResult(-1, false);
        }
    }

}
