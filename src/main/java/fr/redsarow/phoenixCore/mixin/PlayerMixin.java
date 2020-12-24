package fr.redsarow.phoenixCore.mixin;

import fr.redsarow.phoenixCore.minecraft.events.callbacks.ServerPlayerEntityCallback;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
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
}
