package com.fz.multilanguages.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PluginExtensionEntity {
    private boolean enable = true;
    private boolean enableService = true;
    private boolean enableIntentService = true;
    private boolean enableApplication = true;
    private List<String> overwriteClass = new ArrayList<>();
    private Map<String, List<String>> hookPoint = new HashMap<>();
    private Map<String, String> exceptionHandler = new HashMap<>();

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isEnableService() {
        return enableService;
    }

    public void setEnableService(boolean enableService) {
        this.enableService = enableService;
    }

    public boolean isEnableIntentService() {
        return enableIntentService;
    }

    public void setEnableIntentService(boolean enableIntentService) {
        this.enableIntentService = enableIntentService;
    }

    public boolean isEnableApplication() {
        return enableApplication;
    }

    public void setEnableApplication(boolean enableApplication) {
        this.enableApplication = enableApplication;
    }

    List<String> getOverwriteClass() {
        return overwriteClass;
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
        return "PluginExtensionEntity{" +
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
