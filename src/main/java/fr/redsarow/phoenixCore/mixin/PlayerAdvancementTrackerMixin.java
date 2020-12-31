package fr.redsarow.phoenixCore.mixin;


import fr.redsarow.phoenixCore.minecraft.events.callbacks.AdvancementCallback;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author redsarow
 */
@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementTrackerMixin {

    @Inject(method = "grantCriterion", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/advancement/Advancement;getRewards()Lnet/minecraft/advancement/AdvancementRewards;"))
    public void onObtain(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
        AdvancementCallback.OBTAIN.invoker().onDone((PlayerAdvancementTracker) (Object) this, advancement);
    }
}
