package com.example.game.game;

import android.animation.ValueAnimator;
import android.view.animation.BounceInterpolator;

import com.example.game.R;

public class FallingObstacle extends Obstacle {
    private static final int RES_ID = R.mipmap.epn01_tm01_sda;
    private ValueAnimator animator;

    @Override
    protected void init(int index, float left, float top) {
        init(left, top, RES_ID);

        float destTop = dstRect.top - 3f;
        dstRect.offset(0, -dstRect.height());

        initAnimator();
        animator.setFloatValues(dstRect.top, destTop);
        animator.start();
    }

    // Lazy initialization
    private void initAnimator() {
        if (animator != null) return;
        animator = new ValueAnimator();
        animator.setDuration(1800);
        animator.setStartDelay(800);
        animator.setInterpolator(new BounceInterpolator());
        animator.addUpdateListener(animListener);
    }

    private final ValueAnimator.AnimatorUpdateListener animListener = (ValueAnimator anim) -> {
        float y = (Float)anim.getAnimatedValue();
        dstRect.offsetTo(dstRect.left, y);
    };

    @Override
    public void onRecycle() {
        super.onRecycle();
        animator.end();
    }

    @Override
    public void pause() {
        animator.pause();
    }

    @Override
    public void resume() {
        animator.resume();
    }
}
