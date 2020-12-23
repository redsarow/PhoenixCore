package fr.redsarow.phoenixCore.minecraft;

import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author redsarow
 */
public class ScoreboardManager {

    private static ScoreboardManager INSTANCE;
    private final MinecraftServer server;
    private ScoreboardObjective objectiveDeath;
    private ScoreboardObjective objectiveHealth;

    private ScoreboardManager(MinecraftServer server) {
        this.server = server;
        this.initScoreboard();
    }

    public static ScoreboardManager getInstance() {
        return INSTANCE;
    }

    public static ScoreboardManager init(MinecraftServer server) {
        if (INSTANCE == null) {
            INSTANCE = new ScoreboardManager(server);
        }
        return INSTANCE;
    }

    private void initScoreboard() {
        if (this.server.getScoreboard().getObjective("Vie") == null) {
            LiteralText objectiveVieName = new LiteralText("Vie");
            this.objectiveHealth = this.server.getScoreboard().addObjective("Vie", ScoreboardCriterion.HEALTH, objectiveVieName, ScoreboardCriterion.RenderType.HEARTS);
            this.objectiveHealth.setDisplayName(objectiveVieName.formatted(Formatting.GREEN));
            this.server.getScoreboard().setObjectiveSlot(Scoreboard.getDisplaySlotId("list"), this.objectiveHealth);
        }

        this.objectiveDeath = this.server.getScoreboard().getObjective("Mort");
        if (objectiveDeath == null) {
            LiteralText objectiveMortName = new LiteralText("Mort");
            this.objectiveDeath = this.server.getScoreboard().addObjective("Mort", ScoreboardCriterion.DEATH_COUNT, objectiveMortName, ScoreboardCriterion.RenderType.INTEGER);
            this.objectiveDeath.setDisplayName(objectiveMortName.formatted(Formatting.RED));
            this.server.getScoreboard().setObjectiveSlot(Scoreboard.getDisplaySlotId("sidebar"), this.objectiveDeath);
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
