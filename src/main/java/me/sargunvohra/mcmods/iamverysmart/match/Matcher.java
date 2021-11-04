package me.sargunvohra.mcmods.iamverysmart.match;

import net.minecraft.resources.ResourceLocation;

public interface Matcher {
  MatchResult match(ResourceLocation id);
}
