package me.sargunvohra.mcmods.iamverysmart.mixin;

import java.util.ArrayList;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

  @Inject(at = @At("RETURN"), method = "onPlayerConnect")
  private void unlockRecipes(ClientConnection con, ServerPlayerEntity player, CallbackInfo ci) {
    player.unlockRecipes(new ArrayList<>(player.server.getRecipeManager().values()));
  }
}
