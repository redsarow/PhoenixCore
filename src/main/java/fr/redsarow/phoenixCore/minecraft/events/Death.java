package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import fr.redsarow.phoenixCore.minecraft.events.callbacks.ServerPlayerEntityCallback;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Optional;

/**
 * @author redsarow
 */
public class Death implements ServerPlayerEntityCallback.DeathListener {

    public Death(){
        ServerPlayerEntityCallback.DEATH.register(this);
    }

    @Override
    public void onDeath(ServerPlayerEntity player, DamageSource source) {
        String worldName = ModUtils.getWorldName(player.getServerWorld());
        Optional<WorldGroup.Group> worldGroup = WorldGroupManager.getInstance().findGroupByWorldName(worldName);
        if (worldGroup.isPresent() && !worldGroup.get().deadCount) {
            PhoenixCore.getInstance().getServer()
                    .getScoreboard()
                    .forEachScore(ScoreboardCriterion.DEATH_COUNT
                            , player.getEntityName()
                            , scoreboardPlayerScore ->  scoreboardPlayerScore.incrementScore(-1));
        }else {
            Bot.getInstance().ifPresent(bot -> bot.sendMsg.sendDeath(worldName + "\n" + player.getDamageTracker().getDeathMessage().getString()));
        }
    }
}
