package fr.redsarow.phoenixCore.minecraft.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;

/**
 * @author redsarow
 */
public class AdvancementCallback {

    public static final Event<AdvancementCallback.ObtainListener> OBTAIN = EventFactory.createArrayBacked(AdvancementCallback.ObtainListener.class
            , listeners -> (playerAdvancementTracker, advancement) -> {
                for (AdvancementCallback.ObtainListener listener : listeners) {
                    listener.onDone(playerAdvancementTracker, advancement);
                }
            });

    public static final Event<AdvancementCallback.ProgressListener> PROGRESS = EventFactory.createArrayBacked(AdvancementCallback.ProgressListener.class
            , listeners -> () -> {
                for (AdvancementCallback.ProgressListener listener : listeners) {
                    listener.onProgress();
                }
            });

    @FunctionalInterface
    public interface ObtainListener {
        void onDone(PlayerAdvancementTracker playerAdvancementTracker, Advancement advancement);
    }

    @FunctionalInterface
    public interface ProgressListener {
        void onProgress();
    }
}
