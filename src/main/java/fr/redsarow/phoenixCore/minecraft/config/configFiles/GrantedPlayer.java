package fr.redsarow.phoenixCore.minecraft.config.configFiles;

import com.google.gson.annotations.Expose;
import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.minecraft.ScoreboardManager;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.AConfigFile;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.GameMode;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author redsarow
 */
public class GrantedPlayer extends AConfigFile {

    @Expose
    public List<UUID> granted;

    public GrantedPlayer(File file) {
        super(file, 1.0);
    }

    public boolean isGranted(UUID uuid) {
        return granted.contains(uuid);
    }

    public void addGranted(UUID uuid, String playerName) {
        granted.add(uuid);
        ScoreboardObjective objectiveDeath = ScoreboardManager.getInstance().getObjectiveDeath();
        PhoenixCore.getInstance().getServer().getScoreboard().updatePlayerScore(playerName, objectiveDeath);

        PhoenixCore.getInstance().waitGranted.remove(playerName);

        ServerPlayerEntity player = PhoenixCore.getInstance().getServer().getPlayerManager().getPlayer(uuid);
        if (player != null) {
            Optional<WorldGroup.Group> worldGroup = WorldGroupManager.getInstance().findGroupByWorldName(ModUtils.getWorldName(player.getServerWorld()));
            if (worldGroup.isPresent()) {
                GameMode gameMode = worldGroup.get().gameMode;
                if (gameMode != null) {
                    player.setGameMode(gameMode);
                }else {
                    player.setGameMode(GameMode.SURVIVAL);
                }
            }
        }

        this.save();
    }
}
