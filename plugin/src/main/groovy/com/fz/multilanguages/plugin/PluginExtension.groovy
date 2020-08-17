package com.fz.multilanguages.plugin

class PluginExtension {
    boolean enable = true
    boolean enableService = true
    boolean enableIntentService = true
    boolean enableApplication = true
    List<String> overwriteClass = new ArrayList<>()
    Map<String, List<String>> hookPoint = new HashMap<>()
    Map<String, String> exceptionHandler = new HashMap<>()

    @Override
    public String toString() {
        return "PluginExtension{" +
                "enable=" + enable +
                ", enableService=" + enableService +
                ", enableIntentService=" + enableIntentService +
                ", enableApplication=" + enableApplication +
                ", overwriteClass=" + overwriteClass +
                ", hookPoint=" + hookPoint +
                ", exceptionHandler=" + exceptionHandler +
                '}';
    }
}