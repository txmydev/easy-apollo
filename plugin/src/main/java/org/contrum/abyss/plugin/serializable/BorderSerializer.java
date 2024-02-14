package org.contrum.abyss.plugin.serializable;

import com.lunarclient.apollo.common.cuboid.Cuboid2D;
import com.lunarclient.apollo.module.border.Border;
import lombok.experimental.UtilityClass;

import java.awt.*;

@UtilityClass
public class BorderSerializer  {
    public String serialize(Border border) {
        return border.getId() + "," +
                border.getWorld() + "," +
                border.getBounds().getMinX() + "," +
                border.getBounds().getMinZ() + "," +
                border.getBounds().getMaxX() + "," +
                border.getBounds().getMaxZ() + "," +
                border.getColor().getRed() + "," +
                border.getColor().getGreen() + "," +
                border.getColor().getBlue() + "," +
                border.getColor().getAlpha() + "," +
                border.getDurationTicks() + "," +
                border.isCancelEntry() + "," +
                border.isCancelExit() + "," +
                border.isCanShrinkOrExpand();
    }

    public Border deserialize(String s) {
        String[] parts = s.split(",");
        Border.BorderBuilder builder = Border.builder();

        builder.id(parts[0]);
        builder.world(parts[1]);
        builder.bounds(Cuboid2D
                .builder()
                .minX(Double.parseDouble(parts[2]))
                .minZ(Double.parseDouble(parts[3]))
                .maxX(Double.parseDouble(parts[4]))
                .maxZ(Double.parseDouble(parts[5]))
                .build());
        builder.color(new Color(Integer.parseInt(parts[6]), Integer.parseInt(parts[7]), Integer.parseInt(parts[8]), Integer.parseInt(parts[9])));
        builder.durationTicks(Integer.parseInt(parts[10]));
        builder.cancelEntry(Boolean.parseBoolean(parts[11]));
        builder.cancelExit(Boolean.parseBoolean(parts[12]));
        builder.canShrinkOrExpand(Boolean.parseBoolean(parts[13]));

        return builder.build();
    }
}
