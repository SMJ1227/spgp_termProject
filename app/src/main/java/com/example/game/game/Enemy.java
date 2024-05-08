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
import com.example.game.framework.util.Gauge;

public class Enemy extends SheetSprite implements IBoxCollidable, IRecyclable {
    private static final String TAG = Sprite.class.getSimpleName();
    private static final float SPEED = 3.0f;
    private static final float RADIUS = 0.5f;
    public static final int MAX_LEVEL = 20;
    protected RectF collisionRect = new RectF();
    private int level;
    private int life, maxLife;
    protected Gauge gauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);

    public enum State {
        alive, die
    }
    protected State state = State.alive;
    protected static Rect[][] srcRectsArray = {
            makeRects(0, 1, 2), // State.alive
            makeRects(0, 1, 2, 3), // State.die
    };
    protected static float[][] edgeInsetRatios = {
            { 0.0f, 0.0f, 0.0f, 0.0f }, // State.alive
            { 0.0f, 0.0f, 0.0f, 0.0f }, // State.die
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
    private Enemy(int level, int index) {
        super(R.mipmap.bees, 20);
        init(level, index);
        dx = -SPEED;
    }
    private void init(int level, int index) {
        this.level = level;
        this.life = this.maxLife = (level + 1) * 10;
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
    public static Enemy get(int level, int index) {
        Enemy enemy = (Enemy) RecycleBin.get(Enemy.class);
        if (enemy != null) {
            enemy.init(level, index);
            return enemy;
        }
        return new Enemy(level, index);
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
        gauge.draw(canvas, (float)life / maxLife);
        canvas.restore();
    }
    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }

    @Override
    public void onRecycle() {

    }

    public int getScore() {
        return (level + 1) * 100;
    }
    public boolean decreaseLife(int power) {
        life -= power;
        return life <= 0;
    }
}

