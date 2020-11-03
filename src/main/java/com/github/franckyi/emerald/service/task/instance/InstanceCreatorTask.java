package com.github.franckyi.emerald.service.task.instance;

import com.github.franckyi.emerald.Emerald;
import com.github.franckyi.emerald.model.Instance;
import com.github.franckyi.emerald.service.task.EmeraldTask;

import java.text.Normalizer;

public abstract class InstanceCreatorTask extends EmeraldTask<Instance> {
    private String name;
    private String displayName;

    public InstanceCreatorTask(String displayName) {
        this.init(displayName);
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    private void init(String displayName) {
        String name = this.slugify(displayName);
        String originalName = name;
        String originalDisplayName = displayName;
        if (isInstanceNameAlreadyTaken(name)) {
            int i = 0;
            do {
                name = originalName + ++i;
                displayName = originalDisplayName + " (" + i + ")";
            }
            while (isInstanceNameAlreadyTaken(name));
        }
        this.name = name;
        this.displayName = displayName;
    }

    private String slugify(String s) {
        return Normalizer.normalize(s.trim().toLowerCase(), Normalizer.Form.NFD);
    }

    private boolean isInstanceNameAlreadyTaken(String name) {
        for (Instance instance : Emerald.getInstances()) {
            if (instance.getId().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void succeeded() {
        Emerald.getInstances().add(this.getValue());
    }
}
