package com.example.game.game;

import android.graphics.RectF;
import android.graphics.Canvas;

import com.example.game.R;
import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IRecyclable;
import com.example.game.framework.objects.AnimSprite;
import com.example.game.framework.objects.SheetSprite;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.RecycleBin;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class Disturb extends SheetSprite implements IBoxCollidable, IRecyclable {
    private static final float SPEED = 3.0f;
    private static final float RADIUS = 0.9f;
    protected RectF collisionRect = new RectF();
    public Disturb(int index) {
        super(R.mipmap.disturb, 0);
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
        collisionRect.set(dstRect);
        collisionRect.inset(0.11f, 0.11f);
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