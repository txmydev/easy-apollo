package org.contrum.abyss.plugin.wrapper;

import de.exlll.configlib.Serializer;
import lombok.Getter;

import java.awt.*;

@Getter
public class KothWrapper implements Serializer<KothWrapper, String> {

    private String kothName;
    private Color color;

    public KothWrapper() {
    }

    public KothWrapper(String kothName, Color color) {
        this.kothName = kothName;
        this.color = color;
    }

    @Override
    public String serialize(KothWrapper wrapper) {
        return wrapper.kothName + ": " + wrapper.getColor().getRed() + ","+wrapper.getColor().getGreen() + ","+wrapper.getColor().getBlue()+wrapper.getColor().getAlpha();
    }

    @Override
    public KothWrapper deserialize(String s) {
        String[] firstSplit = s.split(": ");
        String name = firstSplit[0];
        String[] colorSplit = firstSplit[1].split(",");

        return new KothWrapper(name, new Color(Integer.parseInt(colorSplit[0]), Integer.parseInt(colorSplit[1]), Integer.parseInt(colorSplit[2]), Integer.parseInt(colorSplit[3])));
    }
}
