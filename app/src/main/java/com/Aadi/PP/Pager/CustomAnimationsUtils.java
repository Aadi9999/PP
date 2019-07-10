package com.Aadi.PP.Pager;




import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

public class CustomAnimationsUtils {
    public static void animateY(@NonNull View view, int startY, int endY, int duration) {
        animateY(view, startY, endY, duration, new AccelerateDecelerateInterpolator());
    }

    public static void animateY(@NonNull View view, int startY, int endY, int duration,
                                @NonNull Interpolator interpolator) {

        view.clearAnimation();

        view.setTranslationY(startY);
        ViewCompat.animate(view).translationY(endY)
                .setInterpolator(interpolator)
                .setDuration(duration)
                .withLayer()
                .setListener(null)
                .start();
    }
}