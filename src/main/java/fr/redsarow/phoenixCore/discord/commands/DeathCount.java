package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.ScoreboardManager;

import java.util.Map;

/**
 * @author redsarow
 */
public class DeathCount extends ACommand {

    private final PhoenixCore phoenixCore;

    public DeathCount(PhoenixCore phoenixCore) {
        super("DeathCount", "Commande DeathCount", Bot.getInstance().prefix + "DeathCount", null, "dc");
        this.phoenixCore = phoenixCore;
    }

    @Override
    public boolean run(Message message) {
        Map<String, Integer> allDeath = ScoreboardManager.getInstance().getPlayerDeathCount();
        StringBuilder names = new StringBuilder();
        StringBuilder death = new StringBuilder();
        allDeath.forEach((name, nb) -> {
            names.append(name).append("\n");
            death.append(nb).append("\n");
        });

        message.getChannel()
                .block()
                .createEmbed(embed ->
                        embed.setTitle(":skull: Tableau des morts :skull:")
                                .setColor(Color.BLACK)
                                .addField("Nom", names.toString().equals("") ? "N/C" : names.toString(), true)
                                .addField("Mort(s)", death.toString().equals("") ? "N/C" : death.toString(), true)
                )
                .block();

        return true;
    }

}