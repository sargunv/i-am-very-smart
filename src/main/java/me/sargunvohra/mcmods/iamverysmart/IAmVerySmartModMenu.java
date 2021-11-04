package me.sargunvohra.mcmods.iamverysmart;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class IAmVerySmartModMenu implements ModMenuApi {

  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return screen ->
      AutoConfig.getConfigScreen(ClientConfig.class, screen).get();
  }
}
