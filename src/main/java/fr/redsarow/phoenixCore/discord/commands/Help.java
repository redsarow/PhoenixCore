package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Message;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;
import fr.redsarow.phoenixCore.discord.Bot;

/**
 * @author redsarow
 */
public class Help extends ACommand {

    public Help() {
        super("help", "Commande help", Bot.getInstance().prefix + "help [commande]", null, "h");
    }

    @Override
    public boolean run(Message message) {
        String[] msgContent = message.getContent().split(" ");

        EmbedCreateSpec embed = new EmbedCreateSpec()
                .setTitle(":question: Help")
                .setColor(Color.RED);

        StringBuilder desc = new StringBuilder("Prefix des commande: ").append(Bot.getInstance().prefix);

        if (msgContent.length < 2) {
            desc.append("\nListe des commandes");
            helpAllCommand(embed);
        }else {
            desc.append("\nAide pour ").append(msgContent[1]);
            ACommand command = CommandManagement.getCommand(msgContent[1]);
            if (command == null) {
                message.getChannel().block().createMessage("La commande : " + msgContent[1] + "est inconnue!");
                return true;
            }
            embed.addField("Nom", command.getName(), false);
            embed.addField("Description", command.getDescription(), true);
            embed.addField("Usage", command.getUsage(), false);
            embed.addField("Exemple", command.getExemple().toString(), true);
            embed.addField("Alias", command.getAlias(), false);
        }

        embed.setDescription(desc.toString());
        message.getChannel()
                .block()
                .getRestChannel()
                .createMessage(embed.asRequest())
                .block();
        return true;
    }

    private void helpAllCommand(EmbedCreateSpec embed) {
        StringBuilder names = new StringBuilder();
        StringBuilder descriptions = new StringBuilder();
        StringBuilder usage = new StringBuilder();
        CommandManagement.getAllCommands().forEach(aCommand -> {
            names.append(aCommand.getName()).append("\n");
            descriptions.append(aCommand.getDescription()).append("\n");
            usage.append(aCommand.getUsage()).append("\n");
        });
        embed.addField("Nom", names.toString(), true);
        embed.addField("Description", descriptions.toString(), true);
        embed.addField("Usage", usage.toString(), true);
    }
}
