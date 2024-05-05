package com.example.game.game;

import com.example.game.R;
import com.example.game.framework.res.BitmapPool;
import com.example.game.framework.scene.RecycleBin;

public class Platform extends MapObject {
    public enum Type {
        T_10x2, T_3x1, COUNT, RANDOM;
        int resId() { return resIds[this.ordinal()]; }
        int width() { return widths[this.ordinal()]; }
        int height() { return heights[this.ordinal()]; }
        static final int[] resIds = {
                R.mipmap.land_platform_1,
                R.mipmap.land_platform_2
        };
        static final int[] widths = { 10, 3 };
        static final int[] heights = { 2, 1 };
    }
    private boolean passes;
    private Platform() {}

    @Override
    protected MainScene.Layer getLayer() {
        return MainScene.Layer.platform;
    }

    public static Platform get(Type type, float left, float top) {
        if (type == Type.RANDOM) {
            type = Type.T_10x2;
        }
        Platform platform = (Platform) RecycleBin.get(Platform.class);
        if (platform == null) {
            platform = new Platform();
        }
        platform.init(type, left, top);
        return platform;
    }

    private void init(Type type, float left, float top) {
        bitmap = BitmapPool.get(type.resId());
        width = type.width();
        height = type.height();
        dstRect.set(left, top, left + width, top + height);
        passes = type == Type.T_3x1;
    }
    public boolean canPass() {
        return passes;
    }
}
