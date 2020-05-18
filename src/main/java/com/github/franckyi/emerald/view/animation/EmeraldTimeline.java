package com.github.franckyi.emerald.view.animation;

public interface EmeraldTimeline {
    void addListener(Runnable r);

    void removeListener(Runnable r);

    void play();
}
