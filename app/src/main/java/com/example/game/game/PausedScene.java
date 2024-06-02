package com.example.game.game;

import com.example.game.R;
import com.example.game.framework.objects.Button;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class PausedScene extends Scene {
    public enum Layer {
        bg, title, touch, COUNT
    }
    public PausedScene() {
        initLayers(Layer.COUNT);
        float w = Metrics.width, h = Metrics.height;
        add(Layer.bg, new Sprite(R.mipmap.cookie_run_bg_1, w/2, h/2, w, h));
        add(Layer.bg, new Sprite(R.mipmap.cookie_run_title, w/2, h/2, 3.69f, 1.36f));
        add(Layer.touch, new Button(R.mipmap.btn_resume_n, 14.5f, 1.0f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                pop();
                return false;
            }
        }));
    }

    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }
}