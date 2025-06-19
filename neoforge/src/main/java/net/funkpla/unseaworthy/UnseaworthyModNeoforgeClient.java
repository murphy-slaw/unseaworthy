package net.funkpla.unseaworthy;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public class UnseaworthyModNeoforgeClient {
    public UnseaworthyModNeoforgeClient(IEventBus modBus) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> this::createScreen);
    }

    private Screen createScreen(ModContainer modContainer, Screen parent) {
        return AutoConfig.getConfigScreen(UnseaworthyConfig.class, parent).get();
    }

}
