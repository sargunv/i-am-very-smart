package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.iamverysmart.config.ClientConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class IAmVerySmartClient implements ClientModInitializer {
  @Override
  public void onInitializeClient() {
    ClientConfigManager.INSTANCE.init();
  }
}
