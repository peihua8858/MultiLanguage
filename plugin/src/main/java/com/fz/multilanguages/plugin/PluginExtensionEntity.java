package com.fz.multilanguages.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginExtensionEntity {
    private boolean enable = true;
    private List<String> overwriteClass = new ArrayList<>();
    private Map<String, List<String>> hookPoint = new HashMap<>();
    private Map<String, String> exceptionHandler = new HashMap<>();

    boolean getEnable() {
        return enable;
    }

    List<String> getOverwriteClass() {
        return overwriteClass;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setOverwriteClass(List<String> overwriteClass) {
        this.overwriteClass = overwriteClass;
    }

    public void setHookPoint(Map<String, List<String>> hookPoint) {
        this.hookPoint = hookPoint;
    }

    public void setExceptionHandler(Map<String, String> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    Map<String, List<String>> getHookPoint() {
        return hookPoint;
    }

    Map<String, String> getExceptionHandler() {
        return exceptionHandler;
    }

    @Override
    public String toString() {
        return "PluginExtension{" +
                "enable=" + enable +
                ", overwriteClass=" + overwriteClass +
                ", hookPoint=" + hookPoint +
                ", exceptionHandler=" + exceptionHandler +
                '}';
    }
}
