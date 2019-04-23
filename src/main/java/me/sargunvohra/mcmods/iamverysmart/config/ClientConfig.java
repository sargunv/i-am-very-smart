package me.sargunvohra.mcmods.iamverysmart.config;

import me.sargunvohra.mcmods.autoconfig1.ConfigData;
import me.sargunvohra.mcmods.autoconfig1.annotation.Config;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
@Config(name = "iamverysmart")
public class ClientConfig implements ConfigData {
    public boolean suppressTutorialNotification = true;
    public boolean suppressRecipeNotification = true;
}
