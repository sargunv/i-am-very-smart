package me.sargunvohra.mcmods.iamverysmart.match;

import java.util.List;
import java.util.regex.Pattern;
import net.minecraft.resources.ResourceLocation;

public record SingleMatcher(List<String> strings, List<Pattern> patterns)
  implements Matcher {
  @Override
  public MatchResult match(ResourceLocation id) {
    String idStr = id.toString();
    for (String string : strings) {
      if (string.equals(idStr)) {
        return MatchResult.EXACT;
      }
    }
    for (Pattern pattern : patterns) {
      if (pattern.matcher(idStr).matches()) {
        return MatchResult.REGEX;
      }
    }
    return MatchResult.NONE;
  }
}
