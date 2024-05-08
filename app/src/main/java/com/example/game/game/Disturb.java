package com.example.game.game;

import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Canvas;
import android.util.Log;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IRecyclable;
import com.example.game.framework.objects.SheetSprite;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.RecycleBin;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Disturb extends SheetSprite implements IBoxCollidable, IRecyclable {
    private static final String TAG = Sprite.class.getSimpleName();
    private static final float SPEED = 3.0f;
    private static final float RADIUS = 0.9f;
    protected RectF collisionRect = new RectF();
    public enum State {
        alive, die
    }
    protected State state = State.alive;
    protected static Rect[][] srcRectsArray = {
            makeRects(0)
    };
    protected static float[][] edgeInsetRatios = {
            { 0.0f, 0.0f, 0.0f, 0.0f }
    };
    protected static Rect[] makeRects(int... indices) {
        Rect[] rects = new Rect[indices.length];
        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];
            int l = (idx % 100) * 100;
            int t = (idx / 100) * 100;
            rects[i] = new Rect(l, t, l + 100, t + 100);
        }
        return rects;
    }
    private void fixCollisionRect() {
        float[] insets = edgeInsetRatios[state.ordinal()];
        collisionRect.set(
                dstRect.left + width * insets[0],
                dstRect.top + height * insets[1],
                dstRect.right - width * insets[2],
                dstRect.bottom - height * insets[3]);
    }
    private void setState(State state) {
        this.state = state;
        srcRects = srcRectsArray[state.ordinal()];
    }
    public Disturb(int index) {
        super(R.mipmap.disturb, 1);
        init(index);
        dx = -SPEED;
    }
    private void init(int index) {
        if(index == 0){
            setPosition(Metrics.width, Metrics.height / 4 * (index+1) - 0.8f, RADIUS);
        }
        if(index == 1){
            setPosition(Metrics.width, Metrics.height / 4 * (index+1) - 1.05f, RADIUS);
        }
        if(index == 2){
            setPosition(Metrics.width, Metrics.height / 4 * (index+1) - 1.3f, RADIUS);
        }
        srcRects = srcRectsArray[state.ordinal()];
    }
    public static Disturb get(int index) {
        Disturb disturb = (Disturb) RecycleBin.get(Disturb.class);
        if (disturb != null) {
            disturb.init(index);
            return disturb;
        }
        return new Disturb(index);
    }
    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        if (dstRect.right < 0) {
            Scene.top().remove(MainScene.Layer.enemy, this);
        }
        fixCollisionRect();
    }
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.save();
        canvas.translate(dstRect.left, dstRect.bottom);
        canvas.scale(dstRect.width(), dstRect.height());
        canvas.restore();
    }
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
    public void onRecycle() {

    }
}