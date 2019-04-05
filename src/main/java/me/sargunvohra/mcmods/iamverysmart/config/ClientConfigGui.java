package me.sargunvohra.mcmods.iamverysmart.config;

import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.BooleanListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;

@Environment(EnvType.CLIENT)
class ClientConfigGui {
  static void open(Screen parent, ClientConfig config, Runnable save) {
    if (!FabricLoader.getInstance().isModLoaded("cloth")) return;

    ClientConfig defaults = new ClientConfig();

    ClothConfigScreen.Builder builder =
        new ClothConfigScreen.Builder(
            parent, "text.iamverysmart.config.title", savedConfig -> save.run());

    builder
        .addCategory("text.iamverysmart.config.category.general")
        .addOption(
            new BooleanListEntry(
                "text.iamverysmart.config.option.suppressTutorialNotification",
                config.suppressTutorialNotification,
                "text.cloth.reset_value",
                () -> defaults.suppressTutorialNotification,
                value -> config.suppressTutorialNotification = value))
        .addOption(
            new BooleanListEntry(
                "text.iamverysmart.config.option.suppressRecipeNotification",
                config.suppressRecipeNotification,
                "text.cloth.reset_value",
                () -> defaults.suppressRecipeNotification,
                value -> config.suppressRecipeNotification = value));

    MinecraftClient.getInstance().openScreen(builder.build());
  }
}
