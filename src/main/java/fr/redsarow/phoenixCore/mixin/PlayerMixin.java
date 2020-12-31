package fr.redsarow.phoenixCore.mixin;

import fr.redsarow.phoenixCore.minecraft.events.callbacks.ServerPlayerEntityCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * @author redsarow
 */
@Mixin(ServerPlayerEntity.class)
public class PlayerMixin {

    @Inject(method = "onDeath", at = @At("TAIL"))
    public void onDeath(DamageSource source, CallbackInfo ci) {
        ServerPlayerEntityCallback.DEATH.invoker().onDeath((ServerPlayerEntity) (Object) this, source);
    }

    @Inject(method = "worldChanged", at = @At("TAIL"))
    public void onWorldChanged(ServerWorld origin, CallbackInfo ci) {
        ServerPlayerEntityCallback.WORLD_CHANGE.invoker().onWorldChang((ServerPlayerEntity) (Object) this,
                origin,
                (ServerWorld) ((ServerPlayerEntity) (Object) this).world);
    }
}
