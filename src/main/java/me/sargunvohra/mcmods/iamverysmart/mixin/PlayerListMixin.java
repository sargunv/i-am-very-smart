package me.sargunvohra.mcmods.iamverysmart.mixin;

import java.util.stream.Collectors;
import me.sargunvohra.mcmods.iamverysmart.IAmVerySmart;
import me.sargunvohra.mcmods.iamverysmart.config.ReloadListener;
import me.sargunvohra.mcmods.iamverysmart.match.MatchResult;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

  @Inject(at = @At("RETURN"), method = "placeNewPlayer")
  private void unlockRecipes(
    Connection con,
    ServerPlayer player,
    CallbackInfo ci
  ) {
    var logger = IAmVerySmart.LOGGER;
    var matcher = ReloadListener.INSTANCE.getMatcher();
    var recipesToUnlock = player.server
      .getRecipeManager()
      .getRecipes()
      .stream()
      .filter(recipe -> matcher.match(recipe.getId()) != MatchResult.NONE)
      .peek(recipe -> logger.debug("Matched recipe {}", recipe.getId()))
      .collect(Collectors.toList());
    logger.info(
      "Matched {} recipes to unlock for {}",
      recipesToUnlock.size(),
      player.getGameProfile().getName()
    );
    player.awardRecipes(recipesToUnlock);
  }
}
