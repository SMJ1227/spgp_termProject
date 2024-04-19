package com.example.game.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.game.R;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.res.BitmapPool;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Fighter extends Sprite {
    private static final float PLANE_WIDTH = 1.75f;
    private static final float PLANE_HEIGHT = PLANE_WIDTH * 80 / 72;
    private static final float TARGET_RADIUS = 0.5f;
    private static final float SPEED = 5.0f;
    private static final float FIRE_INTERVAL = 0.25f;
    private static final float SPARK_DURATION = 0.1f;
    private static final float SPARK_WIDTH = 1.125f;
    private static final float SPARK_HEIGHT = SPARK_WIDTH * 3 / 5;
    private static final float SPARK_OFFSET = 0.66f;
    private static final float BULLET_OFFSET = 0.8f;



    private RectF sparkRect = new RectF();
    private Bitmap sparkBitmap;
    private float targetX;
    private RectF targetRect = new RectF();
    private Bitmap targetBmp;
    private float fireCoolTime = FIRE_INTERVAL;

    public Fighter() {
        super(R.mipmap.fighter);
        setPosition(Metrics.width / 2, Metrics.height - 3, PLANE_WIDTH, PLANE_HEIGHT);
        setTargetX(x);

        targetBmp = BitmapPool.get(R.mipmap.fighter_target);
        sparkBitmap = BitmapPool.get(R.mipmap.laser_spark);
    }

    @Override
    public void update(float elapsedSeconds) {
        if (targetX < x) {
            dx = -SPEED;
        } else if (x < targetX) {
            dx = SPEED;
        } else {
            dx = 0;
        }
        super.update(elapsedSeconds);
        float adjx = x;
        if ((dx < 0 && x < targetX) || (dx > 0 && x > targetX)) {
            adjx = targetX;
        } else {
            adjx = Math.max(radius, Math.min(x, Metrics.width - radius));
        }
        if (adjx != x) {
            setPosition(adjx, y, PLANE_WIDTH, PLANE_HEIGHT);
            dx = 0;
        }
        fireCoolTime -= elapsedSeconds;
        if (fireCoolTime <= 0) {
            fireBullet();
            fireCoolTime = FIRE_INTERVAL;
        }
    }

    private void fireBullet() {
        Scene.top().add(MainScene.Layer.bullet, Bullet.get(x, y - BULLET_OFFSET));
    }

    @Override
    public void draw(Canvas canvas) {
        if (dx != 0) {
            canvas.drawBitmap(targetBmp, null, targetRect, null);
        }
        super.draw(canvas);
        if (FIRE_INTERVAL - fireCoolTime > SPARK_DURATION) {
            sparkRect.set(x - SPARK_WIDTH/2, y - SPARK_HEIGHT/2 - SPARK_OFFSET,
                    x + SPARK_WIDTH/2, y + SPARK_HEIGHT/2 - SPARK_OFFSET
            );
            canvas.drawBitmap(sparkBitmap, null, sparkRect, null);
        }
    }

    private void setTargetX(float x) {
        targetX = Math.max(radius, Math.min(x, Metrics.width - radius));
        targetRect.set(
                targetX - TARGET_RADIUS, y - TARGET_RADIUS,
                targetX + TARGET_RADIUS, y + TARGET_RADIUS
        );
    }

    public boolean onTouch(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float[] pts = Metrics.fromScreen(event.getX(), event.getY());
                setTargetX(pts[0]);
                return true;
        }
        return false;
    }
}
