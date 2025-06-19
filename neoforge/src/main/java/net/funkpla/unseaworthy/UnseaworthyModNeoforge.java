package net.funkpla.unseaworthy;

import net.funkpla.unseaworthy.data.SinkTimeData;
import net.funkpla.unseaworthy.network.UnseaworthyPacketHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(Constants.MOD_ID)
public class UnseaworthyModNeoforge {

    public UnseaworthyModNeoforge(IEventBus modBus) {
        UnseaworthyCommon.init();
        SinkTimeData.register(modBus);
        modBus.addListener(UnseaworthyPacketHandler::registerPackets);
    }
}
