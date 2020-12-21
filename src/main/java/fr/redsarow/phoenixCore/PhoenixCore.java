package fr.redsarow.phoenixCore;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.config.ConfigManager;
import fr.redsarow.phoenixCore.minecraft.config.MainConf;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redsarow
 */
public class PhoenixCore implements DedicatedServerModInitializer {

    public static final String MOD_ID = "phoenix-core";
    private static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    public static MainConf conf;

    private MinecraftServer server;
    private ScoreboardObjective objectiveDeath;

    public static Logger getLogger(String className) {
        String suffix = ModUtils.isEmpty(className) ? "" : " - " + className;
        return LogManager.getLogger(MOD_ID + suffix);
    }

    @Override
    public void onInitializeServer() {
        conf = ConfigManager.getInstance().iniConfig("config.json", MainConf.class);

        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
        ServerLifecycleEvents.SERVER_STOPPING.register(this::onServerStoping);

        if (conf.discord) {
            Bot.initBot(this);
        }
    }

    private void onServerStarted(MinecraftServer server) {
        this.server = server;
        this.initScoreboard();
        // TODO status discod serveur OK
    }

    private void onServerStoping(MinecraftServer server) {
        // TODO off bot
    }

    private void initScoreboard() {
        if (this.server.getScoreboard().getObjective("Vie") == null) {
            TranslatableText objectiveVieName = new TranslatableText("objective.phoenix-core.vie");
            ScoreboardObjective objectiveHealth = this.server.getScoreboard().addObjective("Vie", ScoreboardCriterion.HEALTH, objectiveVieName, ScoreboardCriterion.RenderType.HEARTS);
            objectiveHealth.setDisplayName(new LiteralText(Formatting.GREEN.toString()).append(objectiveVieName));
            this.server.getScoreboard().setObjectiveSlot(Scoreboard.getDisplaySlotId("list"), objectiveHealth);
        }

        this.objectiveDeath = this.server.getScoreboard().getObjective("Mort");
        if (objectiveDeath == null) {
            TranslatableText objectiveMortName = new TranslatableText("objective.phoenix-core.mort");
            this.objectiveDeath = this.server.getScoreboard().addObjective("Mort", ScoreboardCriterion.DEATH_COUNT, objectiveMortName, ScoreboardCriterion.RenderType.INTEGER);
            this.objectiveDeath.setDisplayName(new LiteralText(Formatting.RED.toString()).append(objectiveMortName));
            this.server.getScoreboard().setObjectiveSlot(Scoreboard.getDisplaySlotId("sidebar"), objectiveDeath);
        }
    }

    public Map<String, Integer> getPlayerDeathCount() {
        Collection<ScoreboardPlayerScore> scoreColl = this.server.getScoreboard().getAllPlayerScores(this.objectiveDeath);
        return scoreColl.stream()
                .collect(HashMap::new,
                        (hashMap, playerScore) -> hashMap.put(playerScore.getPlayerName(), playerScore.getScore()),
                        HashMap::putAll);
    }
}
