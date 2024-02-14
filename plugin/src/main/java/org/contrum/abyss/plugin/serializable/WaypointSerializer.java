package org.contrum.abyss.plugin.serializable;

import com.lunarclient.apollo.common.location.ApolloBlockLocation;
import com.lunarclient.apollo.module.waypoint.Waypoint;
import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class WaypointSerializer {
    public String serialize(Waypoint waypoint) {
        return waypoint.getName() + "," +
                waypoint.getColor().getRed() + "," +
                waypoint.getColor().getGreen() + "," +
                waypoint.getColor().getBlue() + "," +
                waypoint.getColor().getAlpha() + "," +
                waypoint.getLocation().getWorld() + "," +
                waypoint.getLocation().getX() + "," +
                waypoint.getLocation().getY() + "," +
                waypoint.getLocation().getZ() + "," +
                waypoint.isHidden() + "," +
                waypoint.isPreventRemoval();
    }

    public Waypoint deserialize(String s) {
        String[] parts = s.split(",");
        Waypoint.WaypointBuilder builder = Waypoint.builder();

        builder.name(parts[0]);
        builder.color(new Color(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
        builder.location(ApolloBlockLocation.builder()
                .world(parts[5])
                .x(Integer.parseInt(parts[6]))
                .y(Integer.parseInt(parts[7]))
                .z(Integer.parseInt(parts[8]))
                .build());

        return builder.build();
    }
}
