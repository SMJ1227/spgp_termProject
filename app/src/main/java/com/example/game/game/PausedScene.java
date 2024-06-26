package com.example.game.game;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.game.R;
import com.example.game.framework.activity.GameActivity;
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
        title = new Sprite(R.mipmap.kirby_run_title, cx, cy, 3.69f, 1.36f);
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
                new AlertDialog.Builder(GameActivity.activity)
                        .setTitle("Confirm")
                        .setMessage("Do you really want to exit the game?")
                        .setNegativeButton("No", null)
                        .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finishActivity();
                            }
                        })
                        .create()
                        .show();
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