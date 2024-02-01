package com.github.txmy.event;

import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ApolloLoadEvent extends Event {

    @Getter
    private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }


}
