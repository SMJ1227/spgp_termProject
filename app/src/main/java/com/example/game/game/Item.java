package com.example.game.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.res.BitmapPool;

public class Item implements IGameObject {
    private final Bitmap bitmap;
    private final float right, top, dstCharWidth, dstCharHeight;
    private final int srcCharWidth, srcCharHeight;
    private final Rect srcRect = new Rect();
    private final RectF dstRect = new RectF();

    public Item(int mipmapId, float right, float top, float width) {
        this.bitmap = BitmapPool.get(mipmapId);
        this.right = right;
        this.top = top;
        this.dstCharWidth = width;
        this.srcCharWidth = bitmap.getWidth();
        this.srcCharHeight = bitmap.getHeight();
        this.dstCharHeight = dstCharWidth * srcCharHeight / srcCharWidth;
    }

    @Override
    public void draw(Canvas canvas) {
        srcRect.set(0, 0, srcCharWidth, srcCharHeight); // 원본 비트맵의 전체 영역 설정

        // 총알의 수만큼 반복하여 비트맵을 그립니다.
        for (int i = 0; i < Player.bullets; i++) {
            float x = right - dstCharWidth * (i + 1); // bullets 수만큼 x 좌표 설정
            dstRect.set(x, top, x + dstCharWidth, top + dstCharHeight);
            canvas.drawBitmap(bitmap, srcRect, dstRect, null);
        }
    }

    @Override
    public void update(float elapsedSeconds) {
        // 필요시 업데이트 로직 추가
    }
}
