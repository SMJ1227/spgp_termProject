package com.example.game.game;

import android.view.MotionEvent;

import com.example.game.R;
import com.example.game.framework.objects.Score;
import com.example.game.framework.objects.HorzScrollBackground;
import com.example.game.framework.scene.Scene;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    private final Player player;
    Score score; // package private
    public int getScore() {
        return score.getScore();
    }
    public enum Layer {
        bg, platform, item, enemy, bullet, player, ui, controller, COUNT
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
        return player.onTouch(event);
    }
}
