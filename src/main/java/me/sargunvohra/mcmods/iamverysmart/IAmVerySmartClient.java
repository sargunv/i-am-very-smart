package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.autoconfig.api.AutoConfig;
import me.sargunvohra.mcmods.autoconfig.api.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;

@SuppressWarnings("unused")
public class IAmVerySmartClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register("iamverysmart", ClientConfig.class, JanksonConfigSerializer::new);
    }
}
