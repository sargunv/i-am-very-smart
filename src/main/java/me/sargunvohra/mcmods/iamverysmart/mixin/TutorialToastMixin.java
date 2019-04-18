package me.sargunvohra.mcmods.iamverysmart.mixin;

import me.sargunvohra.mcmods.iamverysmart.config.ClientConfigManager;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixin {

    @Inject(at = @At("HEAD"), method = "draw", cancellable = true)
    private void hideToastInstantly(CallbackInfoReturnable<Toast.Visibility> cir) {
        if (ClientConfigManager.INSTANCE.getConfig().suppressTutorialNotification) {
            cir.setReturnValue(Toast.Visibility.HIDE);
        }
    }
}
