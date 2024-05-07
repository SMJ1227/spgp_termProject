package com.example.game.game;

import android.view.MotionEvent;
import android.util.Log;

import com.example.game.R;
import com.example.game.framework.objects.Score;
import com.example.game.framework.objects.HorzScrollBackground;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.objects.Button;
import com.example.game.framework.objects.Sprite;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    private final Player player;
    Score score; // package private
    public int getScore() {
        return score.getScore();
    }
    public enum Layer {
        bg, platform, item, enemy, bullet, player, ui, touch, controller, COUNT
    }
    public MainScene() {
        initLayers(Layer.COUNT);

        add(Layer.controller, new EnemyGenerator());
        add(Layer.controller, new CollisionChecker(this));
        add(Layer.controller, new MapLoader(this));

        add(Layer.bg, new HorzScrollBackground(R.mipmap.clouds, 0.5f));
        add(Layer.bg, new HorzScrollBackground(R.mipmap.bg, 1.5f));

        this.player = new Player();
        add(Layer.player, player);

        add(Layer.touch, new Button(R.mipmap.btn_attack_n, 12.5f, 7.7f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                //Log.d(TAG, "Button: Slide " + action);
                player.attack(action == Button.Action.pressed);
                return true;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_shoot_n, 12.5f, 8.5f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                //Log.d(TAG, "Button: Slide " + action);
                player.throwing(action == Button.Action.pressed);
                return true;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_jump_n, 14.5f, 7.7f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                player.jump();
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_fall_n, 14.5f, 8.5f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                player.fall();
                return false;
            }
        }));
        this.score = new Score(R.mipmap.number_24x32, 8.5f, 0.5f, 0.6f);
        score.setScore(0);
        add(Layer.ui, score);
    }

    public void addScore(int amount) {
        score.add(amount);
    }

    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
    }

    @Override
    protected int getTouchLayerIndex() { return Layer.touch.ordinal(); }
}
