package com.example.game.game;

import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.objects.Button;
import com.example.game.framework.objects.SheetSprite;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.util.CollisionHelper;
import com.example.game.framework.view.Metrics;

public class Player extends SheetSprite implements IBoxCollidable {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private static final float INVINCIBILITY_DURATION = 3.0f;
    private float invincibilityTime = 2.0f;
    public static boolean isInvincibility = true;
    private static final float BULLET_OFFSET = 0f;
    public static float FIRE_INTERVAL = 10.0f;
    public static float fireCoolTime = 0;
    private float  throwTime = 0.20f;
    public static float ATTACK_INTERVAL = 5.0f;
    public static float attackCoolTime = 0;
    private float  attackTime = 0.40f;
    private static final float SPEED = 3.0f;
    public static int bullets = 5;
    private static float dx, dy, foot, floor;
    public enum State {
        walking, goBack, running, jump, throwing, attack, falling, hurt, COUNT
    }
    private float jumpSpeed;
    private static final float JUMP_POWER = 10.0f;
    private static final float GRAVITY = 25.0f;
    private final RectF collisionRect = new RectF();
    protected State state = State.walking;
    protected Obstacle obstacle;
    protected static Rect[][] srcRectsArray = {
            makeRects(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), // State.walking
            makeRects(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), // State.goBack
            makeRects(100, 101, 102, 103, 104, 105, 106, 107), // State.running
            makeRects(200, 201, 202, 203, 204, 205, 206, 207, 208),    // State.jump
            makeRects(300, 301, 300),              // State.throwing
            makeRects(300, 301, 302, 303),              // State.attack
            makeRects(207, 208),                  // State.falling
            makeRects(304, 206, 305, 307, 307, 307, 307, 307, 307, 307, 307, 307, 308, 309),                   // State.hurt
    };
    protected static float[][] edgeInsetRatios = {
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.walking
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.goBack
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.running
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.jump
            { 0.2f, 0.5f, 0.0f, 0.0f }, // throwing
            { 0.2f, 0.5f, 0.0f, 0.0f }, // attack
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.falling
            { 0.2f, 0.5f, 0.0f, 0.0f }, // State.hurt
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
        super(R.mipmap.kurby, 15);
        setPosition(1.0f, 2.0f, 1.0f, 2.0f);
        srcRects = srcRectsArray[state.ordinal()];
        fixCollisionRect();
    }
    @Override
    public void update(float elapsedSeconds) {
        fireCoolTime -= elapsedSeconds;
        attackCoolTime -= elapsedSeconds;
        invincibilityTime -= elapsedSeconds;
        if(invincibilityTime < 0){
            isInvincibility = false;
        }
        switch (state) {
            case jump:
            case falling:
                dy = jumpSpeed * elapsedSeconds;
                jumpSpeed += GRAVITY * elapsedSeconds;
                if (jumpSpeed >= 0) { // 낙하하고 있다면 발밑에 땅이 있는지 확인한다
                    foot = collisionRect.bottom;
                    floor = findNearestPlatformTop(foot);
                    if (foot + dy >= floor) {
                        dy = floor - foot;
                        setState(State.walking);
                    }
                }
                y += dy;
                dstRect.offset(0, dy);
                break;
            case walking:
                foot = collisionRect.bottom;
                floor = findNearestPlatformTop(foot);
                if (foot < floor) {
                    setState(State.falling);
                    jumpSpeed = 0;
                }
                break;
            case goBack:
                dx = -SPEED * elapsedSeconds;
                x += dx;
                if (x < 0) {
                    x = 0;
                    dx = 0;
                }
                dstRect.offset(dx, 0);
                break;
            case running:
                dx = SPEED * elapsedSeconds;
                x += dx;
                if (x > Metrics.width - width) {
                    x = Metrics.width - width;
                    dx = 0;
                }
                dstRect.offset(dx, 0);
                break;
            case throwing:
                throwTime -= elapsedSeconds;
                fireBullet(elapsedSeconds);
                if(throwTime < 0) {
                    throwTime = 0.20f;
                    setState(State.walking);
                }
                break;
            case attack:
                attackTime -= elapsedSeconds;
                swordEffect(elapsedSeconds);
                if(attackTime < 0) {
                    attackTime = 0.40f;
                    setState(State.walking);
                }
                break;
            case hurt:
                Log.v(TAG, String.valueOf(invincibilityTime));
                if(invincibilityTime < 2.0f){
                    setState(State.walking);
                    obstacle = null;
                }
                dy = jumpSpeed * elapsedSeconds;
                jumpSpeed += GRAVITY * elapsedSeconds;
                if (jumpSpeed >= 0) { // 낙하하고 있다면 발밑에 땅이 있는지 확인한다
                    foot = collisionRect.bottom;
                    floor = findNearestPlatformTop(foot);
                    if (foot + dy >= floor) {
                        dy = floor - foot;
                    }
                }
                y += dy;
                dstRect.offset(0, dy);
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
            if (rect.top < foot) {
                continue;
            }
            if (top > rect.top) {
                top = rect.top;
                nearest = platform;
            }
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
        if (state == State.hurt) return;
        if (state != State.walking && state != State.running) return;
        jumpSpeed = -JUMP_POWER;
        setState(State.jump);
    }
    public void fall() {
        if (state == State.hurt) return;
        if (state != State.walking) return;
        float foot = collisionRect.bottom;
        Platform platform = findNearestPlatform(foot);
        if (platform == null) return;
        if (!platform.canPass()) return;
        setState(State.falling);
        dstRect.offset(0, 0.001f);
        collisionRect.offset(0, 0.001f);
        jumpSpeed = 0;
    }
    public void throwing() {
        if(fireCoolTime > 0) return;
        if (state == State.hurt) return;
        if (state == State.walking || state == State.running) {
            setState(State.throwing);
            return;
        }
    }
    public void attack() {
        if (attackCoolTime > 0) return;
        if (state == State.hurt) return;
        if (state == State.walking || state == State.running) {
            setState(State.attack);
            return;
        }
    }
    public void running(Button.Action action) {
        if (state == State.hurt) return;
        if(action == Button.Action.pressed){
            if (state == State.walking || state == State.running) {
                setState(State.running);
            }
        }
        else if(action == Button.Action.released){
            setState(State.walking);
            dx = 0;
        }
    }
    public void hurt(Obstacle obstacle) {
        if (state == State.hurt) return;
        setState(State.hurt);
        invincibilityTime = INVINCIBILITY_DURATION;
        isInvincibility = true;
        fixCollisionRect();
        this.obstacle = obstacle;
    }
    public void goBack(Button.Action action) {
        if (state == State.hurt) return;
        if(action == Button.Action.pressed){
            if (state == State.walking || state == State.running) {
                setState(State.goBack);
            }
        }
        else if(action == Button.Action.released){
            setState(State.walking);
            dx = 0;
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

        attackCoolTime = ATTACK_INTERVAL;

        int score = scene.getScore();
        int power = 10 + score / 1000;
        Attack attack = Attack.get(x, y - BULLET_OFFSET, power);

        scene.add(MainScene.Layer.swordEffect, attack);
    }
    private void fireBullet(float elapsedSeconds) {
        MainScene scene = (MainScene) Scene.top();
        if (scene == null) return;
        if (fireCoolTime > 0) return;
        if(bullets <= 0) return;
        bullets--;

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


