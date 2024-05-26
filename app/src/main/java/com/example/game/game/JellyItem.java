package com.example.game.game;

import android.graphics.Rect;
import android.graphics.RectF;

import com.example.game.R;
import com.example.game.framework.res.BitmapPool;
import com.example.game.framework.scene.RecycleBin;

public class JellyItem extends MapObject {
    public static final int JELLY_COUNT = 60;
    public static final int RANDOM = JELLY_COUNT + 1;
    private static final int ITEMS_IN_A_ROW = 30;
    private static final int SIZE = 66;
    private static final int BORDER = 2;
    private static final float INSET = 0.20f;
    private final RectF collisionRect = new RectF();
    private JellyItem() {
        super(MainScene.Layer.item);
        bitmap = BitmapPool.get(R.mipmap.jelly);
        srcRect = new Rect();
    }
    public static JellyItem get(int index, float left, float top) {
        if (index == RANDOM) {
            index = random.nextInt(JELLY_COUNT);
        }
        JellyItem item = (JellyItem) RecycleBin.get(JellyItem.class);
        if (item == null) {
            item = new JellyItem();
        }
        item.init(index, left, top);
        return item;
    }

    private void init(int index, float left, float top) {
        setSrcRect(index);
        width = height = 1;
        dstRect.set(left, top, left + width, top + height);
        fixCollisionRect();
    }
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        fixCollisionRect();
    }

    private void fixCollisionRect() {
        collisionRect.set(
                dstRect.left + width * INSET,
                dstRect.top + height * INSET,
                dstRect.right - width * INSET,
                dstRect.bottom - height * INSET);
    }
    private void setSrcRect(int index) {
        int x = index % ITEMS_IN_A_ROW;
        int y = index / ITEMS_IN_A_ROW;
        int left = x * (SIZE + BORDER) + BORDER;
        int top = y * (SIZE + BORDER) + BORDER;
        srcRect.set(left, top, left + SIZE, top + SIZE);
    }
    public static int getScore() {
        return 100;
    }
}
