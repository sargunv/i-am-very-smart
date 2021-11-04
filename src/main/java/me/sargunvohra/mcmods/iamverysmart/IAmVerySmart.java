package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.iamverysmart.config.ReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IAmVerySmart implements ModInitializer {

  public static final Logger LOGGER = LogManager.getLogger(IAmVerySmart.class);

  @Override
  public void onInitialize() {
    ResourceManagerHelper
      .get(PackType.SERVER_DATA)
      .registerReloadListener(ReloadListener.INSTANCE);
  }
}
