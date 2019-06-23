package com.winji.stuact;

/**
 * Created by Muhammed Gamal on 03/05/2019.
 */

public class BounceInterpolator implements android.view.animation.Interpolator {

    double mAmplitude = 1;
    double mFrequency = 10;

    public BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
