package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.title.Title;
import com.lunarclient.apollo.module.title.TitleModule;
import com.lunarclient.apollo.module.title.TitleType;
import com.lunarclient.apollo.player.ApolloPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class ApolloTitleModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final TitleModule module;

    public ApolloTitleModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(TitleModule.class);
    }

    public void sendTitle(ApolloPlayer player, Title title) {
        module.displayTitle(player, title);
    }

    public void sendTitle(Player player, Title title) {
        Apollo.getPlayerManager().getPlayer(player.getUniqueId()).ifPresent(apolloPlayer -> {
            this.sendTitle(player, title);
        });
    }

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

        if(subTitle != null) {
            sendTitle(player, builder.type(TitleType.SUBTITLE).message(Component.text(subTitle)).build());
        }
    }

}
