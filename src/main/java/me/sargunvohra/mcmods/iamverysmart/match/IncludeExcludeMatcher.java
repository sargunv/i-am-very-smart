package me.sargunvohra.mcmods.iamverysmart.match;

import net.minecraft.resources.ResourceLocation;

public record IncludeExcludeMatcher(Matcher includes, Matcher excludes)
  implements Matcher {
  @Override
  public MatchResult match(ResourceLocation id) {
    MatchResult excludesMatch = excludes.match(id);
    if (excludesMatch == MatchResult.EXACT) return MatchResult.NONE;
    MatchResult includesMatch = includes.match(id);
    if (includesMatch == MatchResult.EXACT) return MatchResult.EXACT;
    if (excludesMatch == MatchResult.REGEX) return MatchResult.NONE;
    return includesMatch;
  }
}
