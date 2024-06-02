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
    private final Sprite title;
    public PausedScene() {
        initLayers(Layer.COUNT);
        float w = Metrics.width, h = Metrics.height;
        float cx = w / 2, cy = h / 2;
        add(Layer.bg, new Sprite(R.mipmap.trans_50b, cx, cy, w, h));
        title = new Sprite(R.mipmap.cookie_run_title, cx, cy, 3.69f, 1.36f);
        add(Layer.title, title);
        add(Layer.touch, new Button(R.mipmap.btn_resume_n, 8.0f, 6.5f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                pop();
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_quit_n, 8f, 5.7f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                finishActivity();
                return false;
            }
        }));
    }
    @Override
    protected int getTouchLayerIndex() {
        return Layer.touch.ordinal();
    }
    public boolean isTransparent() {
        return true;
    }
}