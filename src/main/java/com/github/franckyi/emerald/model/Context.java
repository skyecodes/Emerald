package com.github.franckyi.emerald.model;

import java.util.List;

public class Context {
    private List<Instance> instances;
    private boolean launcherInitialized;

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

    public boolean isLauncherInitialized() {
        return launcherInitialized;
    }

    public void setLauncherInitialized(boolean launcherInitialized) {
        this.launcherInitialized = launcherInitialized;
    }
}
