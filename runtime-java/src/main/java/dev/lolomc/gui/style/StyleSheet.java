package dev.lolomc.gui.style;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class StyleSheet {
    private final List<CssRule> rules = new ArrayList<CssRule>();
    private final Map<String, String> variables = new LinkedHashMap<String, String>();
    public List<CssRule> getRules() { return rules; }
    public Map<String, String> getVariables() { return variables; }
}

