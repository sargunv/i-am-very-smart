package me.sargunvohra.mcmods.iamverysmart.config;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mcp.MethodsReturnNonnullByDefault;
import me.sargunvohra.mcmods.iamverysmart.match.ComposedMatcher;
import me.sargunvohra.mcmods.iamverysmart.match.SingleMatcher;
import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import org.apache.logging.log4j.LogManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RecipeMatcherManager extends ReloadListener<ComposedMatcher> {

    public static final RecipeMatcherManager INSTANCE = new RecipeMatcherManager();

    private final Gson gson = new Gson();
    private ComposedMatcher matcher = new ComposedMatcher(new SingleMatcher(), new SingleMatcher());

    private RecipeMatcherManager() {
    }

    private SingleMatcher buildMatcher(IResourceManager resourceManager, String fileName) {
        SingleMatcher ret = new SingleMatcher();
        resourceManager
            .getAllResourceLocations("iamverysmart", path -> path.endsWith(fileName))
            .stream()
            .map(resourceId -> {
                IResource resource;
                try {
                    resource = resourceManager.getResource(resourceId);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null; // skip the ones we can't read
                }
                return gson.<List<String>>fromJson(
                    new InputStreamReader(resource.getInputStream()),
                    new TypeToken<List<String>>() {
                    }.getType()
                );
            })
            .filter(Objects::nonNull)
            .forEach(list -> list.forEach(ret::add));
        return ret;
    }

    @Override
    protected ComposedMatcher prepare(IResourceManager resourceManager, IProfiler profiler) {
        return new ComposedMatcher(
            buildMatcher(resourceManager, "include_recipes.json"),
            buildMatcher(resourceManager, "exclude_recipes.json")
        );
    }

    @Override
    protected void apply(ComposedMatcher matcher, IResourceManager resourceManager, IProfiler profiler) {
        LogManager.getLogger().info("Loaded {}", matcher);
        this.matcher = matcher;
    }

    public ComposedMatcher getMatcher() {
        return matcher;
    }
}
