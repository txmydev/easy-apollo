package org.contrum.abyss.plugin;

import com.lunarclient.apollo.module.border.Border;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.SerializeWith;
import lombok.Getter;
import org.contrum.abyss.plugin.serializable.BorderSerializer;
import org.contrum.abyss.plugin.serializable.WaypointSerializer;
import org.contrum.abyss.plugin.wrapper.KothWrapper;

import java.util.List;
import java.util.Optional;

@Configuration
@Getter
public class AbyssConfig {

    @SerializeWith(serializer = BorderSerializer.class, nesting = 1)
    private List<Border> borders;
    @SerializeWith(serializer = WaypointSerializer.class, nesting = 1)
    private List<Waypoint> waypoints;
    @SerializeWith(serializer = KothWrapper.class, nesting = 1)
    private List<KothWrapper> kothDisplays;

    public Optional<KothWrapper> getKothWrapper(String name) {
        return kothDisplays.stream().filter(wrapper -> wrapper.getKothName().equalsIgnoreCase(name)).findFirst();
    }
}
