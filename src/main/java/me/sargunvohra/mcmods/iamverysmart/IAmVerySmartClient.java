package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1.serializer.Toml4jConfigSerializer;
import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;

@SuppressWarnings("unused")
public class IAmVerySmartClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        AutoConfig.register(ClientConfig.class, Toml4jConfigSerializer::new);
    }
}
