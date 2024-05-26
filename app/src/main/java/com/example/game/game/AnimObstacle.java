package com.example.game.game;

import android.graphics.RectF;
import com.example.game.R;
import com.example.game.framework.res.BitmapPool;

import android.util.Log;
public class AnimObstacle extends Obstacle {

    private static final int[][] RES_ID_ARRAYS = new int[][] {
            new int[] {
                    R.mipmap.trans_00p,
                    R.mipmap.epn01_tm01_jp1up_01,
                    R.mipmap.epn01_tm01_jp1up_02,
                    R.mipmap.epn01_tm01_jp1up_03,
                    R.mipmap.epn01_tm01_jp1up_04,
            },
            new int[]{
                    R.mipmap.trans_00p,
                    R.mipmap.epn01_tm01_jp2up_01,
                    R.mipmap.epn01_tm01_jp2up_02,
                    R.mipmap.epn01_tm01_jp2up_03,
                    R.mipmap.epn01_tm01_jp2up_04,
                    R.mipmap.epn01_tm01_jp2up_05,
            },
    };
    private static final float[] TRANSPARENT_RATIO = { 0.49f, 0.33f };
    private static final float FPS = 8.0f;
    private int resIndex;
    private float time = 0;
    private final RectF collisionRect = new RectF();

    protected void init(int resIndex, float left, float top) {
        Log.d("AnimObstacle", "Initializing with resIndex: " + resIndex);

        // 유효한 resIndex 값인지 확인
        if (resIndex < 0 || resIndex >= RES_ID_ARRAYS.length) {
            Log.e("AnimObstacle", "Invalid resIndex: " + resIndex + ". Setting resIndex to default value 0.");
            resIndex = 0; // 기본값으로 설정
        }
        this.resIndex = resIndex;

        int defResId = RES_ID_ARRAYS[this.resIndex][1];
        Log.d("AnimObstacle", "Default resource ID: " + defResId);

        // 이미지의 크기를 결정할 때는 0번이 아닌 1번 이미지 기준으로 하도록 한다.
        init(left, top, defResId);

        time = 0;
        bitmap = BitmapPool.get(R.mipmap.trans_00p);
        // 처음에는 완전투명한 이미지를 사용하여 안 보이게 한다.

        fixCollisionRect();
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        if (dstRect.left >= 10.0f) return;

        time += elapsedSeconds;
        int[] resIds = RES_ID_ARRAYS[resIndex];
        int frameIndex = Math.round(time * FPS);
        if (frameIndex >= resIds.length) {
            frameIndex = resIds.length - 1;
        }
        int resId = resIds[frameIndex];
        bitmap = BitmapPool.get(resId);
        fixCollisionRect();
    }

    private void fixCollisionRect() {
        float ratio = TRANSPARENT_RATIO[resIndex];
        collisionRect.set(dstRect);
        collisionRect.top += dstRect.height() * ratio;
    }

    @Override
    public RectF getCollisionRect() {
        return collisionRect;
    }
}
