package org.contrum.abyss.plugin;

import com.lunarclient.apollo.module.border.Border;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import lombok.Data;
import org.contrum.abyss.plugin.wrapper.KothWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class AbyssConfig {

    private List<Border> borders = new ArrayList<>();
    private List<Waypoint> waypoints = new ArrayList<>();
    private List<KothWrapper> kothDisplays = new ArrayList<>();


    private String rallyPermission = "clan.rally.command";

    private String rallyMessage = "&eA new clan rally was set at &6{location} &eby &6{player}. &7(Region: {region})";

    private int rallyDurationMinutes = 10;

    private String rallyWaypointFormat = "Clan Rally";

    private String rallyWaypointColorHex = "#FF2D00";

    private boolean rallyOnlyLeader = true;

    public Optional<KothWrapper> getKothWrapper(String name) {
        return kothDisplays.stream().filter(wrapper -> wrapper.getKothName().equalsIgnoreCase(name)).findFirst();
    }
}
