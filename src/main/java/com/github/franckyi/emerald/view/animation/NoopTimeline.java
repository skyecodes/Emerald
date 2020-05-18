package com.github.franckyi.emerald.view.animation;

public class NoopTimeline implements EmeraldTimeline {
    @Override
    public void addListener(Runnable r) {
    }

    @Override
    public void removeListener(Runnable r) {
    }

    @Override
    public void play() {
    }
}
