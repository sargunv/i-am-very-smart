package me.sargunvohra.mcmods.iamverysmart.mixin;

import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TutorialToast.class)
public abstract class TutorialToastMixin {

  @Inject(at = @At("HEAD"), method = "render", cancellable = true)
  private void hideToastInstantly(
    CallbackInfoReturnable<Toast.Visibility> cir
  ) {
    ClientConfig config = AutoConfig
      .getConfigHolder(ClientConfig.class)
      .getConfig();
    if (config.suppressTutorialNotification) {
      cir.setReturnValue(Toast.Visibility.HIDE);
    }
  }
}
