package org.contrum.abyss.modules;

import com.lunarclient.apollo.Apollo;
import com.lunarclient.apollo.module.chat.ChatModule;
import com.lunarclient.apollo.recipients.Recipients;
import net.kyori.adventure.text.Component;
import org.contrum.abyss.AbyssLoader;
;
import org.bukkit.plugin.java.JavaPlugin;

public class AbyssChatModule {

    private final AbyssLoader loader;
    private final JavaPlugin plugin;
    private final ChatModule module;

    public AbyssChatModule(AbyssLoader loader) {
        this.loader = loader;
        this.plugin = loader.getPlugin();
        this.module = Apollo.getModuleManager().getModule(ChatModule.class);
    }

    /**
     * Sends or updates a live message to all the lunar-client players.
     *
     * @param id The id of the message
     * @param message The message to send.
     */
    public void sendLiveMessage(int id, Component message)
    {
        module.displayLiveChatMessage(Recipients.ofEveryone(), message, id);
    }

    /**
     * Removes the live message from all the lunar-client players
     *
     * @param id The id of the message.
     */
    public void resetLiveMessage(int id) {
        module.removeLiveChatMessage(Recipients.ofEveryone(), id);
    }

}
