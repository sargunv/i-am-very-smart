package me.sargunvohra.mcmods.iamverysmart;

import me.sargunvohra.mcmods.iamverysmart.config.RecipeMatcherManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Mod("iamverysmart")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IAmVerySmart {
    @SubscribeEvent
    public static void onServerAboutToStart(FMLServerAboutToStartEvent event) {
        event.getServer().getResourceManager().addReloadListener(RecipeMatcherManager.INSTANCE);
    }

    @SubscribeEvent
    public static void onPlayerConnect(PlayerEvent.PlayerLoggedInEvent event) {
        PlayerEntity player = event.getPlayer();
        MinecraftServer server = Objects.requireNonNull(player.getServer());
        player.unlockRecipes(
            server.getRecipeManager().getRecipes().stream()
                .filter(recipe -> RecipeMatcherManager.INSTANCE.getMatcher().match(recipe.getId()))
                .collect(Collectors.toList()));
    }
}
