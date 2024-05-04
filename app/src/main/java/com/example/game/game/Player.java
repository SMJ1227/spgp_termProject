package com.example.game.game;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;

import java.util.ArrayList;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.objects.SheetSprite;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Player extends SheetSprite implements IBoxCollidable {
    private static final float FIRE_INTERVAL = 1.25f;
    private static final float BULLET_OFFSET = 0f;
    public enum State {
        running, jump, doubleJump, falling, COUNT
    }
    private float jumpSpeed;
    private static final float JUMP_POWER = 9.0f;
    private static final float GRAVITY = 17.0f;
    private float fireCoolTime = FIRE_INTERVAL;
    private final RectF collisionRect = new RectF();
    protected State state = State.running;
    protected static Rect[][] srcRectsArray = {
            makeRects(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), // State.running
            makeRects(7, 8),               // State.jump
            makeRects(1, 2, 3, 4),         // State.doubleJump
            makeRects(0),                  // State.falling
    };
    protected static float[][] edgeInsetRatios = {
            { 0.0f, 0.5f, 0.0f, 0.0f }, // State.running
            { 0.1f, 0.2f, 0.1f, 0.0f }, // State.jump
            { 0.2f, 0.2f, 0.2f, 0.0f }, // State.doubleJump
            { 0.2f, 0.0f, 0.2f, 0.0f }, // State.falling
    };
    protected static Rect[] makeRects(int... indices) {
        Rect[] rects = new Rect[indices.length];
        for (int i = 0; i < indices.length; i++) {
            int idx = indices[i];
            int l = 32 + (idx % 100) * 100;
            int t = 50 + (idx / 100) * 100;
            rects[i] = new Rect(l, t, l + 36, t + 50);
        }
        return rects;
    }
    public Player() {
        super(R.mipmap.kurby, 8);
        setPosition(1.0f, 5.0f, 1.0f, 2.0f);
        srcRects = srcRectsArray[state.ordinal()];
        fixCollisionRect();
    }
    @Override
    public void update(float elapsedSeconds) {
        switch (state) {
            case jump:
            case doubleJump:
            case falling:
                float dy = jumpSpeed * elapsedSeconds;
                jumpSpeed += GRAVITY * elapsedSeconds;
                if (jumpSpeed >= 0) { // 낙하하고 있다면 발밑에 땅이 있는지 확인한다
                    float foot = collisionRect.bottom;
                    float floor = findNearestPlatformTop(foot);
                    if (foot + dy >= floor) {
                        dy = floor - foot;
                        setState(State.running);
                    }
                }
                y += dy;
                dstRect.offset(0, dy);
                break;
            case running:
                float foot = collisionRect.bottom;
                float floor = findNearestPlatformTop(foot);
                if (foot < floor) {
                    setState(State.falling);
                    jumpSpeed = 0;
                }
                break;
        }
        fixCollisionRect();
        fireBullet(elapsedSeconds);
    }
    private float findNearestPlatformTop(float foot) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return Metrics.height;
        ArrayList<IGameObject> platforms = scene.objectsAt(MainScene.Layer.platform);
        float top = Metrics.height;
        for (IGameObject obj: platforms) {
            Platform platform = (Platform) obj;
            RectF rect = platform.getCollisionRect();
            if (rect.left > x || x > rect.right) {
                continue;
            }
            //Log.d(TAG, "foot:" + foot + " platform: " + rect);
            if (rect.top < foot) {
                continue;
            }
            if (top > rect.top) {
                top = rect.top;
            }
            //Log.d(TAG, "top=" + top + " gotcha:" + platform);
        }
        return top;
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

    public void jump() {
        if (state == State.running) {
            jumpSpeed = -JUMP_POWER;
            setState(State.jump);
        } else if (state == State.jump) {
            jumpSpeed = -JUMP_POWER;
//            jumpSpeed -= JUMP_POWER;

            setState(State.doubleJump);
        }
    }
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            jump();
        }
        return false;
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
    public RectF getCollisionRect() {
        return collisionRect;
    }
}

