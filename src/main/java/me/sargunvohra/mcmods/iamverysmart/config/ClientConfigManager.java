package me.sargunvohra.mcmods.iamverysmart.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;

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
    try {
      Class.forName("io.github.prospector.modmenu.api.ModMenuApi")
          .getDeclaredMethod("addConfigOverride", String.class, Runnable.class)
          .invoke(
              null,
              "iamverysmart",
              (Runnable)
                  () ->
                      ClientConfigGui.open(
                          MinecraftClient.getInstance().currentScreen, config, this::save));
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  public ClientConfig getConfig() {
    return config;
  }
}
