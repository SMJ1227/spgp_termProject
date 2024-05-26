package com.example.game.game;

import com.example.game.R;
import com.example.game.framework.res.BitmapPool;

public class Obstacle extends MapObject {
    protected Obstacle() {
        super(MainScene.Layer.obstacle);
    }

    protected void init(int index, float left, float top) {
        init(left, top, R.mipmap.epn01_tm01_jp1a);
    }
    protected void init(float left, float top, int resId) {
        bitmap = BitmapPool.get(resId);
        float width = bitmap.getWidth() / 80.0f;
        float height = bitmap.getHeight() / 80.0f;
        dstRect.set(left, top + 1 - height, left + width, top + 1);
    }
}
