package com.example.game.game;

import android.graphics.RectF;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IRecyclable;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.RecycleBin;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Attack extends Sprite implements IBoxCollidable, IRecyclable {
    private static final float EFFECT_WIDTH = 1f;
    private static final float EFFECT_HEIGHT = EFFECT_WIDTH;
    private int power;
    private Attack(float x, float y, int power) {
        super(R.mipmap.sword_effect);
        setPosition(x, y, EFFECT_WIDTH, EFFECT_HEIGHT);
        this.power = power;
    }
    public static Attack get(float x, float y, int power) {
        Attack attack = (Attack) RecycleBin.get(Bullet.class);
        if (attack != null) {
            attack.setPosition(x, y, EFFECT_WIDTH, EFFECT_HEIGHT);
            attack.power = power;
            return attack;
        }
        return new Attack(x, y, power);
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
