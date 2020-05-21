package com.github.franckyi.emerald.view;

import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

public final class ViewUtils {
    public static final DropShadow SHADOW_EFFECT = new DropShadow(BlurType.GAUSSIAN, Color.color(0, 0, 0, 0.26), 15, 0.16, 0, 4);
}
