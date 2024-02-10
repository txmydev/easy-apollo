package org.contrum.abyss.plugin;

import com.lunarclient.apollo.module.border.Border;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.SerializeWith;
import lombok.Getter;
import org.contrum.abyss.plugin.serializable.BorderSerializer;
import org.contrum.abyss.plugin.serializable.WaypointSerializer;
import org.contrum.abyss.plugin.wrapper.KothWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
@Getter
public class AbyssConfig {

    @SerializeWith(serializer = BorderSerializer.class, nesting = 1)
    private List<Border> borders = new ArrayList<>();
    @SerializeWith(serializer = WaypointSerializer.class, nesting = 1)
    private List<Waypoint> waypoints = new ArrayList<>();
    @SerializeWith(serializer = KothWrapper.class, nesting = 1)
    private List<KothWrapper> kothDisplays = new ArrayList<>();

    @Comment({
            "Here you can configure the /rally command permission",
            "you can also disable the permission by leaving it empty"
    })
    private String rallyPermission = "clan.rally.command";
    @Comment({
            "Here you can configure the message that all the clan members",
            "will receive when a member triggers /rally."
    })
    private String rallyMessage = "&eA new clan rally was set at &6{location} &eby &6{player}. &7(Region: {region})";
    @Comment({
            "Here you can specify how long (in minutes) will the rally waypoint last",
            "set -1 to make it permanent until another rally waypoint is",
            "performed."
    })
    private int rallyDurationMinutes = 10;
    @Comment({
            "Here you can configure how the rally waypoint will look",
            "you can use {player} to represent the player that made the ",
            "rally."
    })
    private String rallyWaypointFormat = "Clan Rally";
    @Comment({
            "Set the color of the rally waypoint (in hex format)"
    })
    private String rallyWaypointColorHex = "#FF2D00";

    private boolean rallyOnlyLeader = true;

    public Optional<KothWrapper> getKothWrapper(String name) {
        return kothDisplays.stream().filter(wrapper -> wrapper.getKothName().equalsIgnoreCase(name)).findFirst();
    }
}
