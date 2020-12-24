package fr.redsarow.phoenixCore.minecraft.events;

import fr.redsarow.phoenixCore.minecraft.events.callbacks.ServerPlayerEntityCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * @author redsarow
 */
public class Death implements ServerPlayerEntityCallback.DeathListener {

    public Death(){
        ServerPlayerEntityCallback.DEATH.register(this);
    }

    @Override
    public void onDeath(ServerPlayerEntity player, DamageSource source) {
        // TODO
    }
}
