package fr.redsarow.phoenixCore;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author redsarow
 */
public class Main implements DedicatedServerModInitializer {

    public static final String MOD_ID = "phoenix-core";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeServer() {
        System.out.println("Hello Fabric world!");

        // TODO init config
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStoping);
    }


    private void onServerStarted(MinecraftServer server) {

    }

    private void onServerStoping(MinecraftServer server) {
    }
}
