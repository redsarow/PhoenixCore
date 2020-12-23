package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import fr.redsarow.phoenixCore.minecraft.util.Colors;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Util;
import org.apache.logging.log4j.Logger;

/**
 * @author redsarow
 */
public class Join implements ServerPlayConnectionEvents.Join {

    private static final Logger LOGGER = PhoenixCore.getLogger(Join.class.getSimpleName());

    public Join() {
        ServerPlayConnectionEvents.JOIN.register(this);
    }

    @Override
    public void onPlayReady(ServerPlayNetworkHandler serverPlayNetworkHandler, PacketSender packetSender, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        String worldName = player.getServerWorld().getRegistryKey().getValue().getPath();
        WorldGroup.Group group = WorldGroupManager.getInstance().findGroupByWorldName(worldName);
        if (group == null) {
            LOGGER.warn("WorldGroup.Group not found for " + worldName);
        }else{
            Team team = group.getTeamForWorld(worldName);
            if (team != null) {
                minecraftServer.getScoreboard().addPlayerToTeam(player.getName().asString(), team);
            }
            player.sendMessage(
                    new LiteralText("Connecter sur")
                            .append(new LiteralText(worldName).formatted(Colors.INFO))
                            .append("du groupe")
                            .append(new LiteralText(group.name).formatted(Colors.INFO))
                    , false
            );
        }

        minecraftServer.sendSystemMessage(new LiteralText(player.getName().asString() + " login"), Util.NIL_UUID);

        // TODO
    }
}
