package com.github.franckyi.emerald.view.animation;

public class InstantTimeline implements EmeraldTimeline {
    @Override
    public void addListener(Runnable r) {
        r.run();
    }

    @Override
    public void removeListener(Runnable r) {

    }

    @Override
    public void play() {

    }

    @Override
    public void stop() {

    }
}
