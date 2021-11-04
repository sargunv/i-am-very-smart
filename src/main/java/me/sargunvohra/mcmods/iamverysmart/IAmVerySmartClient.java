package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class IAmVerySmartClient implements ClientModInitializer {

  @Override
  public void onInitializeClient() {
    AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);
  }
}
