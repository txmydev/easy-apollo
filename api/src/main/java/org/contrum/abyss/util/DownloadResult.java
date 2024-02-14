package org.contrum.abyss.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class DownloadResult {

    private long bytes;
    private boolean success;

}
