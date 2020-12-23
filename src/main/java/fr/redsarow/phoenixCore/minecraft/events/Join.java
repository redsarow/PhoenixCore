package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.PhoenixCore;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.apache.logging.log4j.Logger;

/**
 * @author redsarow
 */
public class Join implements ServerPlayConnectionEvents.Join {

    private static final Logger LOGGER = PhoenixCore.getLogger(Join.class.getSimpleName());

    public Join() {
        ServerPlayConnectionEvents.JOIN.register(this);
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        // TODO
        LOGGER.info("-- Test login player --");
    }
}
