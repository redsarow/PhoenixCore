package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.WorldGroupManager;
import fr.redsarow.phoenixCore.minecraft.config.configFiles.WorldGroup;
import fr.redsarow.phoenixCore.minecraft.util.Colors;
import fr.redsarow.phoenixCore.minecraft.util.ModUtils;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.MessageType;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.util.Util;
import net.minecraft.world.GameMode;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;

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
        String playerName = player.getEntityName();

        String worldName = ModUtils.getWorldName(player.getServerWorld());
        Optional<WorldGroup.Group> group = WorldGroupManager.getInstance().findGroupByWorldName(worldName);

        if (!group.isPresent()) {
            LOGGER.warn("WorldGroup.Group not found for " + worldName);
        }else {
            Team team = group.get().getTeamForWorld(worldName);
            if (team != null) {
                minecraftServer.getScoreboard().addPlayerToTeam(playerName, team);
            }
            player.sendMessage(
                    new LiteralText("Connecter sur ")
                            .append(new LiteralText(worldName).formatted(Colors.INFO))
                            .append(" du groupe ")
                            .append(new LiteralText(group.get().name).formatted(Colors.INFO))
                    , false
            );
        }

        minecraftServer.getPlayerManager().broadcastChatMessage(
                new LiteralText(player.getName().asString() + " login"),
                MessageType.SYSTEM,
                Util.NIL_UUID);

        // new player
        UUID playerUuid = player.getUuid();
        if (PhoenixCore.getInstance().grantedPlayer.isGranted(playerUuid)) {
            player.setGameMode(GameMode.SPECTATOR);
            PhoenixCore.getInstance().waitGranted.put(playerName, playerUuid);

            // msg bot
            Bot.getInstance().ifPresent(bot -> {
                String msgDiscord = "Le joueur \"" + playerName + "\", uuid \"" + playerUuid + "\" vient de se connecter.";
                bot.sendMsg.sendNotGrantedPlayer(msgDiscord, playerName);
            });

            // msg serveur
            MutableText text = new LiteralText("Le joueur \"")
                    .append(new LiteralText(playerName).formatted(Colors.INFO))
                    .append("\", uuid \"")
                    .append(new LiteralText(playerUuid.toString()).formatted(Colors.INFO))
                    .append("\" vient de se connecter.")
                    .formatted(Colors.WARN);
            text.getStyle().withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/grant " + playerName));

            PhoenixCore.getInstance().getServer().getPlayerManager().broadcastChatMessage(text,  MessageType.SYSTEM, Util.NIL_UUID);
        }
    }
}
