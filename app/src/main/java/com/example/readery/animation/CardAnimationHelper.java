package com.example.readery.animation;

import android.view.View;
import android.view.animation.DecelerateInterpolator;

public class CardAnimationHelper {
    public static void animatePress(View view) {
        view.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    public static void animateRelease(View view) {
        view.animate()
                .scaleX(1.0f)
                .scaleY(1.0f)
                .setDuration(100)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }
}