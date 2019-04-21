package me.sargunvohra.mcmods.iamverysmart.mixin;

import me.sargunvohra.mcmods.autoconfig.api.AutoConfig;
import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import net.minecraft.client.toast.RecipeToast;
import net.minecraft.client.toast.Toast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeToast.class)
public abstract class RecipeToastMixin {

    @Inject(at = @At("HEAD"), method = "draw", cancellable = true)
    private void hideToastInstantly(CallbackInfoReturnable<Toast.Visibility> cir) {
        ClientConfig config = AutoConfig.<ClientConfig>getConfigHolder("iamverysmart").getConfig();
        if (config.suppressRecipeNotification) {
            cir.setReturnValue(Toast.Visibility.HIDE);
        }
    }
}
