package fr.redsarow.phoenixCore.minecraft.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * @author redsarow
 */
public final class ServerPlayerEntityCallback {

    public static final Event<DeathListener> DEATH = EventFactory.createArrayBacked(DeathListener.class,
            listeners -> (player, source) -> {
                for (DeathListener listener : listeners) {
                    listener.onDeath(player, source);
                }
            });

    public static final Event<WorldChanged> WORLD_CHANGE = EventFactory.createArrayBacked(WorldChanged.class,
            listeners -> (player, origin, target) -> {
                for (WorldChanged listener : listeners) {
                    listener.onWorldChang(player, origin, target);
                }
            });

    @FunctionalInterface
    public interface DeathListener {
        void onDeath(ServerPlayerEntity player, DamageSource source);
    }

    @FunctionalInterface
    public interface WorldChanged {
        void onWorldChang(ServerPlayerEntity player, ServerWorld origin, ServerWorld target);
    }
}
