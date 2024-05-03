package com.example.game.game;

import android.view.MotionEvent;

import com.example.game.R;
import com.example.game.framework.objects.Score;
import com.example.game.framework.objects.HorzScrollBackground;
import com.example.game.framework.scene.Scene;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    private final Fighter fighter;
    Score score; // package private

    public enum Layer {
        bg, enemy, bullet, player, ui, controller, COUNT
    }
    public MainScene() {
        initLayers(Layer.COUNT);

        add(Layer.controller, new EnemyGenerator());
        add(Layer.controller, new CollisionChecker(this));

        add(Layer.bg, new HorzScrollBackground(R.mipmap.clouds, 0.5f));
        add(Layer.bg, new HorzScrollBackground(R.mipmap.bg, 2.0f));

        this.fighter = new Fighter();
        add(Layer.player, fighter);

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
    public boolean onTouch(MotionEvent event) {
        return fighter.onTouch(event);
    }
}
