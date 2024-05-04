package com.example.game.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.example.game.R;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.res.BitmapPool;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Fighter extends Sprite {
    private static final String TAG = Fighter.class.getSimpleName();
    private static final float PLANE_WIDTH = 1.75f;
    private static final float PLANE_HEIGHT = PLANE_WIDTH * 80 / 72;
    private static final float FIGHTER_Y_OFFSET = 1.2f;
    private static final float TARGET_RADIUS = 0.5f;
    private static final float SPEED = 5.0f;
    private static final float FIRE_INTERVAL = 1.25f;
    private static final float SPARK_DURATION = 0.1f;
    private static final float SPARK_WIDTH = 1.125f;
    private static final float SPARK_HEIGHT = SPARK_WIDTH * 3 / 5;
    private static final float SPARK_OFFSET = 0.66f;
    private static final float BULLET_OFFSET = 0.8f;

    private static final float MAX_ROLL_TIME = 0.4f;
    private float rollTime;
    private static final Rect[] rects = new Rect[] {
            new Rect(  8, 0,   8 + 42, 80),
            new Rect( 76, 0,  76 + 42, 80),
            new Rect(140, 0, 140 + 50, 80),
            new Rect(205, 0, 205 + 56, 80),
            new Rect(270, 0, 270 + 62, 80),
            new Rect(334, 0, 334 + 70, 80),
            new Rect(406, 0, 406 + 62, 80),
            new Rect(477, 0, 477 + 56, 80),
            new Rect(549, 0, 549 + 48, 80),
            new Rect(621, 0, 621 + 42, 80),
            new Rect(689, 0, 689 + 42, 80),
    };


    private RectF sparkRect = new RectF();
    private Bitmap sparkBitmap;
    private float targetX;
    private RectF targetRect = new RectF();
    private Bitmap targetBmp;
    private float fireCoolTime = FIRE_INTERVAL;

    public Fighter() {
        super(R.mipmap.fighters);
        setPosition(Metrics.width / 2, Metrics.height - FIGHTER_Y_OFFSET, PLANE_WIDTH, PLANE_HEIGHT);
        setTargetX(x);

        targetBmp = BitmapPool.get(R.mipmap.fighter_target);
        sparkBitmap = BitmapPool.get(R.mipmap.laser_spark);

        srcRect = rects[5];
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

        fireBullet(elapsedSeconds);
        updateRoll(elapsedSeconds);
    }

    private void updateRoll(float elapsedSeconds) {
        int sign = targetX < x ? -1 : x < targetX ? 1 : 0; // roll 을 변경시킬 부호를 정한다
        if (x == targetX) {                         // 비행기가 멈췄을 때
            if (rollTime > 0) sign = -1;         // 오른쪽으로 움직이고 있었다면 감소시킨다
            else if (rollTime < 0) sign = 1;     // 왼쪽으로 움직이고 있었다면 증가시킨다
        }
        rollTime += sign * elapsedSeconds;
        if (x == targetX) {                           // 비행기가 멈췄을 때
            if (sign < 0 && rollTime < 0) rollTime = 0; // 감소중이었는데 0 을 지나쳤다면 0으로
            if (sign > 0 && rollTime > 0) rollTime = 0; // 증가중이었는데 0 을 지나쳤다면 0으로
        }
        if (rollTime < -MAX_ROLL_TIME) rollTime = -MAX_ROLL_TIME;    // 최대 MAX_ROLL_TIME 까지만
        else if (rollTime > MAX_ROLL_TIME) rollTime = MAX_ROLL_TIME;

        int rollIndex = 5 + (int)(rollTime * 5 / MAX_ROLL_TIME);
        srcRect = rects[rollIndex];
    }

    private void fireBullet(float elapsedSeconds) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;
        fireCoolTime -= elapsedSeconds;
        if (fireCoolTime > 0) return;

        fireCoolTime = FIRE_INTERVAL;

        int score = scene.getScore();
        int power = 10 + score / 1000;
        Bullet bullet = Bullet.get(x, y - BULLET_OFFSET, power);

        scene.add(MainScene.Layer.bullet, bullet);
    }

    @Override
    public void draw(Canvas canvas) {
        if (dx != 0) {
            canvas.drawBitmap(targetBmp, null, targetRect, null);
        }
        super.draw(canvas);
        if (FIRE_INTERVAL - fireCoolTime < SPARK_DURATION) {
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
