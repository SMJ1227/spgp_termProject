package com.example.game.game;

import android.graphics.RectF;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IRecyclable;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.RecycleBin;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Bullet extends Sprite implements IBoxCollidable, IRecyclable {
    private static final float BULLET_WIDTH = 1f;
    private static final float BULLET_HEIGHT = BULLET_WIDTH / 2;
    private static final float SPEED = 20.0f;
    private int power;
    private Bullet(float x, float y, int power) {
        super(R.mipmap.throw_sword);
        setPosition(x, y+0.4f, BULLET_WIDTH, BULLET_HEIGHT);
        this.power = power;
        dx = SPEED;
    }
    public static Bullet get(float x, float y, int power) {
        Bullet bullet = (Bullet) RecycleBin.get(Bullet.class);
        if (bullet != null) {
            bullet.setPosition(x, y + 0.4f, BULLET_WIDTH, BULLET_HEIGHT);
            bullet.power = power;
            return bullet;
        }
        return new Bullet(x, y, power);
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        if (dstRect.left > Metrics.width) {
            Scene.top().remove(MainScene.Layer.bullet, this);
        }
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public void onRecycle() {    }
    public int getPower() {
        return power;
    }
}
