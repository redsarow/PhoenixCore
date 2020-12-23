package fr.redsarow.phoenixCore;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.ScoreboardManager;
import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.MainConf;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import fr.redsarow.phoenixCore.minecraft.events.Join;
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
    public static final String MOD_PREFIX = "PC";
    private static final Logger LOGGER = LogManager.getLogger(MOD_PREFIX);
    private static PhoenixCore INSTANCE;

    public MainConf conf;
    private MinecraftServer server;

    public static Logger getLogger(String className) {
        String suffix = ModUtils.isEmpty(className) ? "" : "|" + className;
        return LogManager.getLogger(MOD_PREFIX + suffix);
    }

    public static PhoenixCore getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeServer() {
        INSTANCE = this;

        // init main conf
        LOGGER.info("init main conf");
        conf = ConfigManager.getInstance().iniConfig("config.json", MainConf.class);

        // Events server
        ServerLifecycleEvents.SERVER_STARTING.register(this::onServerStarting);
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStoping);

        if (conf.discord) {
            Bot.initBot(this);
        }
    }

    private void onServerStarting(MinecraftServer server) {
        this.server = server;
        new Join();
    }

    private void onServerStarted(MinecraftServer server) {

        LOGGER.info("Start Sleep ...");
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LOGGER.info("END Sleep");

        ScoreboardManager.init(server);

        LOGGER.info("init worldGroup conf");
        ConfigManager.getInstance().iniConfig("worldGroup.json", WorldGroup.class);

        if (conf.discord) {
            Bot.getInstance().serverStatus(0);
        }
    }

    private void onServerStoping(MinecraftServer server) {
        if (conf.discord) {
            Bot.getInstance().disconnect();
        }
    }

    public MinecraftServer getServer() {
        return server;
    }
}
