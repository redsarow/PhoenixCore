package fr.redsarow.phoenixCore;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.ScoreboardManager;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.MainConf;
import fr.redsarow.phoenixCore.minecraft.events.AdvancementEvents;
import fr.redsarow.phoenixCore.minecraft.events.Death;
import fr.redsarow.phoenixCore.minecraft.events.Join;
import fr.redsarow.phoenixCore.minecraft.events.Leave;
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
        // Events
        new Join();
        new Leave();
        new Death();
        new AdvancementEvents();
    }

    private void onServerStarted(MinecraftServer server) {

        ScoreboardManager.init(server);

        // Init conf
        LOGGER.info("init worldGroup conf");
        WorldGroupManager.getInstance();

        Bot.getInstance().ifPresent(bot -> bot.serverStatus(0));
    }

    private void onServerStoping(MinecraftServer server) {
        if (conf.discord) {
            Bot.getInstance().ifPresent(Bot::disconnect);
        }
    }

    public MinecraftServer getServer() {
        return server;
    }
}
