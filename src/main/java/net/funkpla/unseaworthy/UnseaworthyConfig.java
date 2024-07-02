package net.funkpla.unseaworthy;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Gui.EnumHandler.EnumDisplayOption;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;


@SuppressWarnings("CanBeFinal")
@Config(name = "unseaworthy")
public class UnseaworthyConfig implements ConfigData {

    public enum BoatFate {
        DESTROY,
        BREAK,
        SINK
    }

    @Comment("Interval between boat destruction checks (ticks)")
    public int interval = 100;

    @Comment("Chance that a boat will break each check")
    @ConfigEntry.BoundedDiscrete(max = 100)
    public int breakChance = 100;

    @Comment("What happens to wrecked boats?")
    @ConfigEntry.Gui.EnumHandler(option = EnumDisplayOption.BUTTON)
    public BoatFate fate = BoatFate.DESTROY;
}
