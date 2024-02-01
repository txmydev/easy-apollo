package org.contrum.abbys.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.title.Title;
import com.lunarclient.apollo.module.title.TitleModule;
import com.lunarclient.apollo.module.title.TitleType;
import com.lunarclient.apollo.player.ApolloPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.contrum.abbys.AbyssLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class AbyssTitleModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final TitleModule module;

    public AbyssTitleModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(TitleModule.class);
    }

    /**
     * Sends a lunar client title to a specified user.
     *
     * @param player The @see{@link ApolloPlayer} player to send the title to.
     * @param title  The @see{@link Title} object to send.
     */
    public void sendTitle(ApolloPlayer player, Title title) {
        module.displayTitle(player, title);
    }

    /**
     * Sends a lunar client title to a specified user.
     *
     * @param player The bukkit player to send the title to.
     * @param title  The @see{@link Title} object to send.
     */
    public void sendTitle(Player player, Title title) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.sendTitle(player, title);
        });
    }

    /**
     * Sends a lunar client title to a specified player.
     *
     * @param player             The bukkit player to send the title to.
     * @param title              The main title message.
     * @param subTitle           The subtitle message.
     * @param fadeIn             The time that the title will take to appear.
     * @param stay               The time that the title will stay in the player's screen.
     * @param fadeOut            The time that the title will take to disappear.
     * @param interpolationRate  The rate that the title will expand or shrink every tick (50ms) between the scale and the interpolation scale.
     * @param interpolationScale If the provided interpolation scale is greater than the scale, the title will expand. However, if the scale is greater than the interpolation scale, the title will shrink.
     * @param scale              Scale of the message you're displaying.
     */
    public void sendTitle(Player player,
                          String title,
                          String subTitle,
                          Duration fadeIn,
                          Duration stay,
                          Duration fadeOut,
                          float interpolationRate,
                          float interpolationScale,
                          float scale) {
        Title.TitleBuilder builder = Title.builder()
                .fadeInTime(fadeIn)
                .displayTime(stay)
                .fadeOutTime(fadeOut)
                .interpolationRate(interpolationRate)
                .interpolationScale(interpolationScale)
                .scale(scale);

        if (title != null) {
            sendTitle(player, builder.type(TitleType.TITLE).message(Component.text(title)).build());
        }

        if (subTitle != null) {
            sendTitle(player, builder.type(TitleType.SUBTITLE).message(Component.text(subTitle)).build());
        }
    }

    /**
     * Sends a lunar title to a player.
     *
     * @param player The bukkit player to send the title to.
     * @param title The message of the title.
     * @param subtitle The message of the subtitle.
     * @param fadeIn The time that the title will take to appear in the screen.
     * @param stay The time that the title will stay in the screen.
     * @param fadeOut The time that the title will take to disappear from the screen.
     * @param scale The scale of the title, default is 1.0.
     */
    public void sendTitle(Player player,
                          String title,
                          String subtitle,
                          Duration fadeIn,
                          Duration stay,
                          Duration fadeOut,
                          float scale) {
        this.sendTitle(player, title, subtitle, fadeIn, stay, fadeOut, 0.01f, 1.0f, scale);
    }

}
