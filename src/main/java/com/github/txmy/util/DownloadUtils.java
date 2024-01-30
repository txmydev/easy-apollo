package com.github.txmy.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.CompletableFuture;

@UtilityClass
public class DownloadUtils {

    public CompletableFuture<?> download(DownloadFileThread thread) {
        return CompletableFuture.runAsync(thread);
    }

}
