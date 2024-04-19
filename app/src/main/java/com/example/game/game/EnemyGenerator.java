package com.example.game.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.scene.Scene;

public class EnemyGenerator implements IGameObject {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    public static final float GEN_INTERVAL = 5.0f;
    private final Random random = new Random();
    private float enemyTime = 0;
    private int wave;
    @Override
    public void update(float elapsedSeconds) {
        enemyTime -= elapsedSeconds;
        if (enemyTime < 0) {
            generate();
            enemyTime = GEN_INTERVAL;
        }
    }

    private void generate() {
        Scene scene = Scene.top();
        if (scene == null) return;

        wave++;
        Log.v(TAG, "Generating: wave " + wave);
        for (int i = 0; i < 5; i++) {
            int level = (wave + 15) / 10 - random.nextInt(3);
            if (level < 0) level = 0;
            if (level > Enemy.MAX_LEVEL) level = Enemy.MAX_LEVEL;
            scene.add(MainScene.Layer.enemy, Enemy.get(level, i));
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
