package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.rest.util.Color;
import fr.redsarow.phoenixCore.discord.Bot;

/**
 * @author redsarow
 */
public class Info extends ACommand {

    public Info() {
        super("info", "Les info du bot", Bot.getInstance().prefix + "info", null);
    }

    @Override
    public boolean run(Message message) {
        User client = Bot.getInstance().getClient().getSelf().block();

        message.getChannel().block().createEmbed(embed ->
                embed.setAuthor(client.getUsername(), null, client.getAvatarUrl())
                        .setThumbnail(client.getAvatarUrl())
                        .setDescription(client.getMention() + " créer par redsarow")
                        .addField("Link", "[github](https://github.com/redsarow)", false)
                        .setFooter(Bot.getInstance().prefix + "help", null)
                        .setColor(Color.WHITE)
        ).block();
        return true;
    }
}
