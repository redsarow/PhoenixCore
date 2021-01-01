package fr.redsarow.phoenixCore;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.ScoreboardManager;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.GrantedPlayer;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.MainConf;
import fr.redsarow.phoenixCore.minecraft.events.*;
import fr.redsarow.phoenixCore.minecraft.util.Colors;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author redsarow
 */
public class PhoenixCore implements DedicatedServerModInitializer {

    public static final String MOD_ID = "phoenix-core";
    public static final String MOD_PREFIX = "PC";
    private static final Logger LOGGER = LogManager.getLogger(MOD_PREFIX);
    private static PhoenixCore INSTANCE;

    public final HashMap<String, UUID> waitGranted = new HashMap<>();
    public MainConf conf;
    public GrantedPlayer grantedPlayer;
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
        new PlayerWorldChange();
    }

    private void onServerStarted(MinecraftServer server) {

        ScoreboardManager.init(server);

        // Init conf
        LOGGER.info("init worldGroup conf");
        WorldGroupManager.getInstance();
        LOGGER.info("init grantedPlayer conf");
        grantedPlayer = ConfigManager.getInstance().iniConfig("grantedPlayer.json", GrantedPlayer.class);

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

    public boolean addGrant(String sender, String newPlayer) {
        UUID uuid = waitGranted.get(newPlayer);
        if (uuid == null) {
            return false;
        }

        grantedPlayer.addGranted(uuid, newPlayer);

        LiteralText msg = (LiteralText) new LiteralText("Le joueur \"")
                .append(new LiteralText(newPlayer).formatted(Colors.INFO))
                .append("\" a été ajouté par \"")
                .append(new LiteralText(sender).formatted(Colors.INFO))
                .append("\"")
                .formatted(Colors.OK);
        server.getPlayerManager().broadcastChatMessage(msg, MessageType.SYSTEM, Util.NIL_UUID);

        Bot.getInstance().ifPresent(bot -> bot.sendMsg
                .sendNewGrantedPlayer("Le joueur \"" + newPlayer + "\" a été ajouté par \"" + sender + "\""));

        return true;
    }
}
