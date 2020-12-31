package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import fr.redsarow.phoenixCore.minecraft.events.callbacks.ServerPlayerEntityCallback;
import fr.redsarow.phoenixCore.minecraft.util.Colors;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;

import java.util.Optional;

/**
 * @author redsarow
 */
public class PlayerWorldChange implements ServerPlayerEntityCallback.WorldChanged {

    public PlayerWorldChange() {
        ServerPlayerEntityCallback.WORLD_CHANGE.register(this);
    }

    @Override
    public void onWorldChang(ServerPlayerEntity player, ServerWorld origin, ServerWorld target) {
        String worldName = target.getRegistryKey().getValue().getPath();
        Optional<WorldGroup.Group> worldGroup = WorldGroupManager.getInstance().findGroupByWorldName(worldName);
        if (!worldGroup.isPresent()) {
            player.sendMessage(
                    new LiteralText("An error has occurred contacting an administrator")
                            .formatted(Colors.ERROR),
                    false);
            return;
        }

        Team team = worldGroup.get().getTeamForWorld(worldName);
        if(team != null){
            PhoenixCore.getInstance().getServer().getScoreboard().addPlayerToTeam(player.getEntityName() ,team);
        }else{
            PhoenixCore.getInstance().getServer().getScoreboard().clearPlayerTeam(player.getEntityName());
        }
    }
}
