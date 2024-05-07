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
    private static final float ATTACK_INTERVAL = 1.0f;
    private static final float BULLET_OFFSET = 0f;
    public enum State {
        running, jump, doubleJump, throwing, attack, falling,  COUNT
    }
    private float jumpSpeed;
    private static final float JUMP_POWER = 9.0f;
    private static final float GRAVITY = 20.0f;
    private float fireCoolTime = FIRE_INTERVAL;
    private float attackCoolTime = ATTACK_INTERVAL;
    private float  attackTime = 0.40f;
    private final RectF collisionRect = new RectF();
    protected State state = State.running;
    protected static Rect[][] srcRectsArray = {
            makeRects(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), // State.running
            makeRects(200, 201, 202, 203, 204, 205, 206, 207, 208),    // State.jump
            makeRects(200, 201, 202, 203, 204, 205, 206, 207, 208),    // State.doublejump
            makeRects(300, 301, 300),              // State.throwing
            makeRects(300, 301, 302, 303),              // State.attack
            makeRects(400, 401),                  // State.falling
    };
    protected static float[][] edgeInsetRatios = {
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.running
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.jump
            { 0.2f, 0.5f, 0.2f, 0.0f }, // State.doubleJump
            { 0.2f, 0.5f, 0.0f, 0.0f }, // throwing
            { 0.2f, 0.5f, 0.0f, 0.0f }, // attack
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
        setPosition(1.0f, 2.0f, 1.0f, 2.0f);
        srcRects = srcRectsArray[state.ordinal()];
        fixCollisionRect();
    }
    @Override
    public void update(float elapsedSeconds) {
        fireCoolTime -= elapsedSeconds;
        attackCoolTime -= elapsedSeconds;
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
            case throwing:
                fireBullet(elapsedSeconds);
                break;
            case attack:
                attackTime -= elapsedSeconds;
                swordEffect(elapsedSeconds);
                if(attackTime < 0) {
                    attackTime = 0.40f;
                    setState(State.running);
                }
                break;
        }
        fixCollisionRect();
    }
    private float findNearestPlatformTop(float foot) {
        Platform platform = findNearestPlatform(foot);
        if (platform == null) return Metrics.height;
        return platform.getCollisionRect().top;
    }

    private Platform findNearestPlatform(float foot) {
        Platform nearest = null;
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return null;
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
                nearest = platform;
            }
            //Log.d(TAG, "top=" + top + " gotcha:" + platform);
        }
        return nearest;
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
            // jumpSpeed -= JUMP_POWER;

            setState(State.doubleJump);
        }
    }
    public void fall() {
        if (state != State.running) return;
        float foot = collisionRect.bottom;
        Platform platform = findNearestPlatform(foot);
        if (platform == null) return;
        if (!platform.canPass()) return;
        setState(State.falling);
        dstRect.offset(0, 0.001f);
        collisionRect.offset(0, 0.001f);
        jumpSpeed = 0;
    }
    public void throwing(boolean startsSlide) {
        if (state == State.running && startsSlide) {
            setState(State.throwing);
            return;
        }
        if (state == State.throwing && !startsSlide) {
            setState(State.running);
            //return;
        }
    }
    public void attack(boolean startsSlide) {
        if (state == State.running && startsSlide) {
            setState(State.attack);
            return;
        }
    }
    public boolean onTouch(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            jump();
        }
        return false;
    }
    private void swordEffect(float elapsedSeconds) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;
        if (attackCoolTime > 0) return;

        attackCoolTime = FIRE_INTERVAL;
        attackTime = 0.40f;

        int score = scene.getScore();
        int power = 10 + score / 1000;
        Attack attack = Attack.get(x, y - BULLET_OFFSET, power);

        scene.add(MainScene.Layer.swordEffect, attack);
    }
    private void fireBullet(float elapsedSeconds) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;
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

