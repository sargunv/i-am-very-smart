package me.sargunvohra.mcmods.iamverysmart.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.prospector.modmenu.api.ModMenuApi;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import me.shedaniel.cloth.gui.ClothConfigScreen;
import me.shedaniel.cloth.gui.entries.BooleanListEntry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.resource.language.I18n;

@Environment(EnvType.CLIENT)
public class ClientConfigManager {

  public static final ClientConfigManager INSTANCE = new ClientConfigManager();

  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
  private final File file =
      new File(FabricLoader.getInstance().getConfigDirectory(), "iamverysmart.json");

  private ClientConfig config = new ClientConfig();

  private ClientConfigManager() {}

  public void init() {
    try {
      if (!file.exists()) {
        //noinspection ResultOfMethodCallIgnored
        file.createNewFile();
      } else {
        this.load();
      }
      this.save();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    this.registerConfigButton();
  }

  private void load() {
    try {
      ClientConfig config = gson.fromJson(new FileReader(file), ClientConfig.class);
      if (config != null) {
        config.validate();
        this.config = config;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void save() {
    try {
      FileWriter writer = new FileWriter(file);
      gson.toJson(config, writer);
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void registerConfigButton() {
    if (!FabricLoader.getInstance().isModLoaded("modmenu")) return;
    if (!FabricLoader.getInstance().isModLoaded("cloth")) return;

    ModMenuApi.addConfigOverride(
        "iamverysmart", () -> openConfigScreen(MinecraftClient.getInstance().currentScreen));
  }

  private void openConfigScreen(Screen parent) {
    if (!FabricLoader.getInstance().isModLoaded("cloth")) return;

    ClientConfig defaults = new ClientConfig();

    ClothConfigScreen.Builder builder =
        new ClothConfigScreen.Builder(
            parent, I18n.translate("text.iamverysmart.config.title"), savedConfig -> save());

    builder
        .addCategory(I18n.translate("text.iamverysmart.config.category.general"))
        .addOption(
            new BooleanListEntry(
                I18n.translate("text.iamverysmart.config.option.suppressTutorialNotification"),
                config.suppressTutorialNotification,
                "text.cloth.reset_value",
                () -> defaults.suppressTutorialNotification,
                value -> config.suppressTutorialNotification = value))
        .addOption(
            new BooleanListEntry(
                I18n.translate("text.iamverysmart.config.option.suppressRecipeNotification"),
                config.suppressRecipeNotification,
                "text.cloth.reset_value",
                () -> defaults.suppressRecipeNotification,
                value -> config.suppressRecipeNotification = value));

    MinecraftClient.getInstance().openScreen(builder.build());
  }

  public ClientConfig getConfig() {
    return config;
  }
}
