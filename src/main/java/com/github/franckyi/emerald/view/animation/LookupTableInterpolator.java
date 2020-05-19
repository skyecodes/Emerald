package com.github.franckyi.emerald.view.animation;

import javafx.animation.Interpolator;

public class LookupTableInterpolator extends Interpolator {
    private final double[] values;
    private final double stepSize;

    public LookupTableInterpolator(double[] values) {
        this.values = values;
        this.stepSize = 1.0 / (double) (this.values.length - 1);
    }

    @Override
    protected double curve(double v) {
        if (v >= 1.0) {
            return 1.0;
        } else if (v <= 0.0) {
            return 0.0;
        } else {
            int position = Math.min((int) (v * (double) (this.values.length - 1)), this.values.length - 2);
            double quantized = (double) position * this.stepSize;
            double diff = v - quantized;
            double weight = diff / this.stepSize;
            return this.values[position] + weight * (this.values[position + 1] - this.values[position]);
        }
    }
}
