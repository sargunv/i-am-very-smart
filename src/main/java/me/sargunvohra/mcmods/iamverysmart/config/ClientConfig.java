package me.sargunvohra.mcmods.iamverysmart.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "i-am-very-smart")
public class ClientConfig implements ConfigData {

  public boolean suppressTutorialNotification = true;
  public boolean suppressRecipeNotification = true;
}
