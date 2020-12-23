package fr.redsarow.phoenixCore.minecraft.events;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author redsarow
 */
public class Leave implements ServerPlayConnectionEvents.Disconnect {

    public Leave() {
        ServerPlayConnectionEvents.DISCONNECT.register(this);
    }

    @Override
    public void onPlayDisconnect(ServerPlayNetworkHandler serverPlayNetworkHandler, MinecraftServer minecraftServer) {
        ServerPlayerEntity player = serverPlayNetworkHandler.player;
        Team team = (Team) player.getScoreboardTeam();
        if (team != null) {
            minecraftServer.getScoreboard().removePlayerFromTeam(player.getName().asString(), team);
        }
    }
}
