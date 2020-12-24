package fr.redsarow.phoenixCore.minecraft.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author redsarow
 */
public final class ServerPlayerEntityCallback {

    public static final Event<DeathListener> DEATH = EventFactory.createArrayBacked(DeathListener.class
            , listeners -> (player, source) -> {
                for (DeathListener listener : listeners) {
                    listener.onDeath(player, source);
                }
            });

    @FunctionalInterface
    public interface DeathListener {
        void onDeath(ServerPlayerEntity player, DamageSource source);
    }
}
