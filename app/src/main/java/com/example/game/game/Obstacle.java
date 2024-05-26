package com.example.game.game;

import com.example.game.R;
import com.example.game.framework.res.BitmapPool;

public class Obstacle extends MapObject {
    protected Obstacle() {
        super(MainScene.Layer.obstacle);
    }

    protected void init(int index, float left, float top) {
        int x = random.nextInt(3);
        int mimmap = random.nextInt(2);
        if(mimmap == 0){
            if(x == 0){
                init(left, top - 1.0f, R.mipmap.epn01_tm01_jp1a);
            }
            else if(x == 1){
                init(left, top - 3.0f, R.mipmap.epn01_tm01_jp1a);
            }
            else{
                init(left, top - 5.0f, R.mipmap.epn01_tm01_jp1a);
            }
        }
        else{
            if(x == 0){
                init(left, top - 1.0f, R.mipmap.disturb);
            }
            else if(x == 1){
                init(left, top - 3.0f, R.mipmap.disturb);
            }
            else{
                init(left, top - 5.0f, R.mipmap.disturb);
            }
        }
    }
    protected void init(float left, float top, int resId) {
        bitmap = BitmapPool.get(resId);
        float width = bitmap.getWidth() / 80.0f;
        float height = bitmap.getHeight() / 80.0f;
        dstRect.set(left, top + 1 - height, left + width, top + 1);
    }
}
