package fr.redsarow.phoenixCore;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.MainConf;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author redsarow
 */
public class PhoenixCore implements DedicatedServerModInitializer {

    public static final String MOD_ID = "phoenix-core";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static MainConf conf;

    public static Logger getLogger(String className) {
        String suffix = ModUtils.isEmpty(className) ? "" : " - " + className;
        return LogManager.getLogger(MOD_ID + suffix);
    }

    @Override
    public void onInitializeServer() {
        conf = ConfigManager.getInstance().iniConfig("config.json", MainConf.class);

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStoping);

        if(conf.discord){
            Bot.initBot();
        }
    }

    private void onServerStarted(MinecraftServer server) {
        // TODO status discod serveur OK
    }

    private void onServerStoping(MinecraftServer server) {
        // TODO off bot
    }
}
