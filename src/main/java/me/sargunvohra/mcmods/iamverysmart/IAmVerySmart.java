package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.iamverysmart.config.ReloadListener;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceType;

@SuppressWarnings("unused")
public class IAmVerySmart implements ModInitializer {
    @Override
    public void onInitialize() {
        ResourceManagerHelper.get(ResourceType.DATA).registerReloadListener(ReloadListener.INSTANCE);
    }
}
