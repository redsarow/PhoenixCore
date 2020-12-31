package fr.redsarow.phoenixCore.discord.commands;

import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import fr.redsarow.phoenixCore.PhoenixCore;
import fr.redsarow.phoenixCore.discord.Bot;

/**
 * @author redsarow
 */
public class Grant extends ACommand {

    public Grant() {
        super("Grant", "Commande Grant", Bot.getInstance().get().prefix + "grant", null, "g");
    }

    @Override
    public boolean run(Message message) {
        String[] msgContent = message.getContent().split(" ");
        if (msgContent.length < 2) {
            return false;
        }
        Member mbr = message.getAuthorAsMember().block();

        boolean userOk = mbr.getRoles().any(iRole -> Bot.getInstance().get().roles.contains(iRole.getName())).block();
        if (!userOk) {
            message.getChannel().block().createMessage(":x: Non autoriser").block();
            return true;
        }

        boolean addOk = PhoenixCore.getInstance().addGrant(mbr.getMention(), msgContent[1]);

        if (!addOk) {
            message.getChannel().block().createMessage("Le joueur \"" + msgContent[1] + "\" n'a pas été trouvé.").block();
        }

        return true;
    }
}
