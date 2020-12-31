package fr.redsarow.phoenixCore.mixin;

import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author redsarow
 */
@Mixin(PlayerAdvancementTracker.class)
public interface PlayerAdvancementTrackerAccessor {

    @Accessor
    ServerPlayerEntity getOwner();
}
