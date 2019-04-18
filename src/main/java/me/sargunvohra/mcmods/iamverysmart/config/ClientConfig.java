package me.sargunvohra.mcmods.iamverysmart.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ClientConfig {
    public boolean suppressTutorialNotification = true;
    public boolean suppressRecipeNotification = true;

    void validate() {
    }
}
