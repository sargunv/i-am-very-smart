package me.sargunvohra.mcmods.iamverysmart.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import me.sargunvohra.mcmods.iamverysmart.IAmVerySmart;
import me.sargunvohra.mcmods.iamverysmart.match.IncludeExcludeMatcher;
import me.sargunvohra.mcmods.iamverysmart.match.MatchResult;
import me.sargunvohra.mcmods.iamverysmart.match.Matcher;
import me.sargunvohra.mcmods.iamverysmart.match.SingleMatcher;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class ReloadListener implements SimpleSynchronousResourceReloadListener {

  public static final ReloadListener INSTANCE = new ReloadListener();

  private final Gson gson = new Gson();

  private Matcher matcher = _id -> MatchResult.NONE;

  private ReloadListener() {}

  @Override
  public ResourceLocation getFabricId() {
    return new ResourceLocation("i-am-very-smart", "reload_listener");
  }

  private SingleMatcher loadSingleMatcher(
    ResourceManager resourceManager,
    String fileName
  ) {
    var strings = new ArrayList<String>();
    var patterns = new ArrayList<String>();
    resourceManager
      .listResources("i-am-very-smart", path -> path.endsWith(fileName))
      .stream()
      .map(resourceId -> {
        try {
          var input = resourceManager.getResource(resourceId).getInputStream();
          return gson.<List<String>>fromJson(
            new InputStreamReader(input),
            new TypeToken<List<String>>() {}.getType()
          );
        } catch (IOException e) {
          e.printStackTrace();
          return null; // skip the ones we can't read
        }
      })
      .filter(Objects::nonNull)
      .flatMap(List::stream)
      .forEach(matchString -> {
        if (matchString.matches("^/.*/$")) {
          patterns.add(matchString.substring(1, matchString.length() - 1));
        } else {
          strings.add(matchString);
        }
      });

    return new SingleMatcher(
      strings,
      patterns.stream().map(Pattern::compile).collect(Collectors.toList())
    );
  }

  @Override
  public void onResourceManagerReload(ResourceManager resourceManager) {
    this.matcher =
      new IncludeExcludeMatcher(
        loadSingleMatcher(resourceManager, "include_recipes.json"),
        loadSingleMatcher(resourceManager, "exclude_recipes.json")
      );
    IAmVerySmart.LOGGER.info("Loaded {}", matcher);
  }

  public Matcher getMatcher() {
    return this.matcher;
  }
}
