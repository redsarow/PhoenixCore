package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.discord.Bot;
import fr.redsarow.phoenixCore.minecraft.events.callbacks.AdvancementCallback;
import fr.redsarow.phoenixCore.mixin.PlayerAdvancementTrackerAccessor;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.text.TranslatableText;

/**
 * @author redsarow
 */
public class AdvancementEvents implements AdvancementCallback.ObtainListener {

    public AdvancementEvents() {
        AdvancementCallback.OBTAIN.register(this);
    }

    @Override
    public void onDone(PlayerAdvancementTracker playerAdvancementTracker, Advancement advancement) {
        if (advancement.getDisplay().shouldAnnounceToChat()) {
            Bot.getInstance().ifPresent(bot -> bot.sendMsg.sendAdvancement(
                    new TranslatableText("chat.type.advancement." + advancement.getDisplay().getFrame().getId(),
                            ((PlayerAdvancementTrackerAccessor) playerAdvancementTracker).getOwner().getDisplayName(),
                            advancement.toHoverableText()
                    ).getString()
            ));
        }
    }
}
