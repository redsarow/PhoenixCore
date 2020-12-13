package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import fr.redsarow.phoenixCore.discord.Bot;

import java.util.Map;
import java.util.UUID;

/**
 * @author redsarow
 */
public class DeathCount extends ACommand {

    public DeathCount() {
        super("DeathCount", "Commande DeathCount", Bot.getInstance().prefix + "DeathCount", null, "dc");
    }

    @Override
    public boolean run(Message message) {
        Map<String, Integer> allDeath = bot.getPlugin().getPlayerDeathCount();
        StringBuilder names = new StringBuilder();
        StringBuilder death = new StringBuilder();
        allDeath.forEach((s, integer) -> {
            names.append(Bukkit.getOfflinePlayer(UUID.fromString(s)).getName()).append("\n");
            death.append(integer).append("\n");
        });

        message.getChannel().block().createEmbed(embed ->
                embed.setTitle(":skull: Tableau des morts :skull:")
                        .setColor(Color.BLACK)
                        .addField("Nom", names.toString().equals("") ? "N/C" : names.toString(), true)
                        .addField("Mort(s)", death.toString().equals("") ? "N/C" : death.toString(), true)
        ).block();

        return true;
    }

}