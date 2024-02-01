package com.github.txmy;

import lombok.Builder;
import lombok.Getter;

import java.util.function.Function;

@Builder
@Getter
public class ApolloLoaderOptions {

    private boolean downloadIfMissing = true;
    private Function<Integer, String> progressReport;
    private Function<Long, String> finishedReport;
    private Function<Throwable, String> errorReport;
    private boolean restartUponLoadingError;

    public Function<Integer, String> getProgressReportOrFallback(Function<Integer, String> fallback) {
        return progressReport != null ? progressReport : fallback;
    }

    public Function<Long, String> getFinishedReportOrFallback(Function<Long, String> fallback) {
        return finishedReport != null ? finishedReport : fallback;
    }

    public Function<Throwable, String> getErrorReportOrFallback(Function<Throwable, String> fallback) {
        return errorReport != null ? errorReport : fallback;
    }

}
