package me.sargunvohra.mcmods.iamverysmart.match;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SingleMatcher {

    private List<String> strings = new ArrayList<>();
    private List<Pattern> patterns = new ArrayList<>();

    MatchResult match(Identifier id) {
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

    public void add(String matchString) {
        if (matchString.length() >= 2 && matchString.startsWith("/") && matchString.endsWith("/")) {
            patterns.add(Pattern.compile(matchString.substring(1, matchString.length() - 1)));
        } else {
            strings.add(matchString);
        }
    }

    @Override
    public String toString() {
        return "SingleMatcher{" + "strings=" + strings + ", patterns=" + patterns + '}';
    }
}
