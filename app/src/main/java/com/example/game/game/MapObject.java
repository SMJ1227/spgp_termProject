package com.example.game.game;

import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Random;

import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IRecyclable;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.Scene;

public abstract class MapObject extends Sprite implements IBoxCollidable, IRecyclable {
    public MapObject() {
        super(0);
    }

    private static final String TAG = MapObject.class.getSimpleName();
    protected static Random random = new Random();
    public static float SPEED = -2.0f;
    public static float speedTime = 5.0f;

    @Override
    public void update(float elapsedSeconds) {
        speedTime -= elapsedSeconds;
        if (speedTime < 0) {
            speedTime = 5.0f;
            if(SPEED > -4.0f){
                SPEED -= 0.1f;
                Log.v(TAG, "mapobj" + String.valueOf(SPEED));
            }
        }
        float dx = SPEED * elapsedSeconds;
        dstRect.offset(dx, 0);
        if (dstRect.right < 0) {
            //Log.d(TAG, "Removing:" + this);
            removeFromScene();
        }
    }

    @NonNull
    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + System.identityHashCode(this) + "(" + width + "x" + height + ")";
    }

    abstract protected MainScene.Layer getLayer();
    public void addToScene() {
        Scene scene = Scene.top();
        if (scene == null) {
            //Log.e(TAG, "Scene stack is empty in addToScene() " + this.getClass().getSimpleName());
            return;
        }
        scene.add(getLayer(), this);
    }
    public void removeFromScene() {
        Scene scene = Scene.top();
        if (scene == null) {
            //Log.e(TAG, "Scene stack is empty in removeFromScene() " + this.getClass().getSimpleName());
            return;
        }
        scene.remove(getLayer(), this);
    }

    @Override
    public RectF getCollisionRect() {
        return dstRect;
    }

    @Override
    public void onRecycle() {

    }
}
