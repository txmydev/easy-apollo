package org.contrum.abyss.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class ApolloUtils extends Thread {

    public static Logger logger;


    public static String getLatestVersionDownloadLink() {

        String fallbackDownload = "https://github.com/LunarClient/Apollo/releases/download/v1.1.4/apollo-bukkit-1.1.4.jar";

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL("https://api.github.com/repos/LunarClient/Apollo/releases/latest").openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.)");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.connect();

            int statusCode = connection.getResponseCode();

            if (statusCode == 200) {
                JsonObject object = new JsonParser().parse(new InputStreamReader(connection.getInputStream())).getAsJsonObject();
                JsonArray assets = object.get("assets").getAsJsonArray();

                AtomicReference<String> downloadLink = new AtomicReference<>("");
                assets.forEach(element -> {
                    JsonObject asset = element.getAsJsonObject();
                    String name = asset.get("name").getAsString();
                    if (name.startsWith("apollo-bukkit")) {
                        downloadLink.set(asset.get("browser_download_url").getAsString());
                    }
                });

                if (downloadLink.get().isEmpty()) {
                    return fallbackDownload;
                }

                return downloadLink.get();
            }
        } catch (Exception ex) {
            logger.severe("Exception occurred while getting the download link for apollo-bukkit: " + ex.getMessage());
        }

        logger.severe("Failed to get the latest version of apollo-bukkit, using fallback v1.0.8 version.");
        return fallbackDownload;
    }

    public static CompletableFuture<String> getLatestVersionDownloadLinkAsync() {
        return CompletableFuture.supplyAsync(() -> getLatestVersionDownloadLink());
    }

}
