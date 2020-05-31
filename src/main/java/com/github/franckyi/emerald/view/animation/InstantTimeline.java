package com.github.franckyi.emerald.view.animation;

public class InstantTimeline extends NoopTimeline {
    @Override
    public void addListener(Runnable r) {
        r.run();
    }
}
