package com.github.franckyi.emerald.view.animation;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import java.util.HashSet;
import java.util.Set;

public class EmeraldTimeline {
    private final Set<Runnable> listeners;
    private final Timeline timeline;

    public EmeraldTimeline(KeyFrame... keyFrames) {
        listeners = new HashSet<>();
        timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrames);
        timeline.setOnFinished(e -> listeners.forEach(Runnable::run));
    }

    public void addListener(Runnable r) {
        listeners.add(r);
    }

    public void play() {
        timeline.play();
    }
}
