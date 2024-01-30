package com.github.txmy.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.chat.ChatModule;
import com.lunarclient.apollo.recipients.Recipients;
import net.kyori.adventure.text.Component;
import com.github.txmy.ApolloLoader;;
import org.bukkit.plugin.java.JavaPlugin;

public class ApolloChatModule {

    private final ApolloLoader loader;
    private final JavaPlugin plugin;
    private final ChatModule module;

    public ApolloChatModule(ApolloLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(ChatModule.class);
    }

    public void sendLiveMessage(int id, Component message)
    {
        module.displayLiveChatMessage(Recipients.ofEveryone(), message, id);
    }

    public void resetLiveMessage(int id) {
        module.removeLiveChatMessage(Recipients.ofEveryone(), id);
    }

}
