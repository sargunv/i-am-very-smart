package me.sargunvohra.mcmods.iamverysmart;

import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig.api.AutoConfig;
import me.sargunvohra.mcmods.autoconfig.api.serializer.JanksonConfigSerializer;
import me.sargunvohra.mcmods.iamverysmart.config.ClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Screen;

import java.util.Optional;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ModMenuCompat implements ModMenuApi {

    @Override
    public String getModId() {
        return "iamverysmart";
    }

    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen("iamverysmart", screen));
    }
}
