package com.github.franckyi.emerald.view.animation;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractTimeline implements EmeraldTimeline {
    protected final Set<Runnable> listeners;

    protected AbstractTimeline() {
        listeners = new HashSet<>();
    }

    @Override
    public void addListener(Runnable r) {
        listeners.add(r);
    }

    public void removeListener(Runnable r) {
        listeners.remove(r);
    }
}
