package org.contrum.abyss.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.concurrent.ThreadFactory;

public class TaskUtil {

    public static void run(final Plugin plugin, final Runnable runnable) {
        plugin.getServer().getScheduler().runTask(plugin, runnable);
    }

    public static void runTimer(final Plugin plugin, final Runnable runnable, final long delay, final long timer) {
        plugin.getServer().getScheduler().runTaskTimer(plugin, runnable, delay, timer);
    }

    public static void runTimer(final Plugin plugin, final BukkitRunnable runnable, final long delay, final long timer) {
        runnable.runTaskTimer(plugin, delay, timer);
    }

    public static void runLater(final Plugin plugin, final Runnable runnable, final long delay) {
        plugin.getServer().getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static void runAsync(final Plugin plugin, final Runnable runnable) {
        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    public static void runAsyncTimer(final Plugin plugin, final Runnable runnable, long delay1, long delay2) {
        plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, runnable, delay1, delay2);
    }

    public static void runAsyncLater(final Plugin plugin, final Runnable runnable, long delay) {
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, runnable, delay);
    }

    public static ThreadFactory newThreadFactory(String name) {
        return new ThreadFactoryBuilder().setNameFormat(name).build();
    }

    public static ThreadFactory newThreadFactory(String name, Thread.UncaughtExceptionHandler handler) {
        return new ThreadFactoryBuilder().setNameFormat(name).setUncaughtExceptionHandler(handler).build();
    }

    public interface Callable {
        void call();
    }
}