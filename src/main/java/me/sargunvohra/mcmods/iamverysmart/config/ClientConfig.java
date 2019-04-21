package me.sargunvohra.mcmods.iamverysmart.config;

import me.sargunvohra.mcmods.autoconfig.api.ConfigData;
import me.sargunvohra.mcmods.autoconfig.api.ConfigGuiEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientConfig implements ConfigData {

    @ConfigGuiEntry
    public boolean suppressTutorialNotification = true;

    @ConfigGuiEntry
    public boolean suppressRecipeNotification = true;
}
