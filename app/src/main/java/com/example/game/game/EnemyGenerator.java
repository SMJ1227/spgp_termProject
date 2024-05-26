package com.example.game.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.Random;

import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.scene.Scene;

public class EnemyGenerator implements IGameObject {
    private static final String TAG = EnemyGenerator.class.getSimpleName();
    public static float GEN_INTERVAL = 5.0f;
    private final Random random = new Random();
    private float enemyTime = 0;
    private int wave;
    @Override
    public void update(float elapsedSeconds) {
        enemyTime -= elapsedSeconds;
        if (enemyTime < 0) {
            generate();
            enemyTime = GEN_INTERVAL;
            if(GEN_INTERVAL > 2.0f && wave%5 == 0){
                GEN_INTERVAL -= 0.3f;
                //Log.v(TAG, String.valueOf(GEN_INTERVAL));
            }
        }
    }

    private void generate() {
        Scene scene = Scene.top();
        if (scene == null) return;

        wave++;

        int[] what = new int[3];
        for (int i = 0; i < 3; i++) {
            what[i] = random.nextInt(2);
        }
        //Log.v(TAG, "Generating: wave " + wave);
        for (int i = 0; i < 3; i++) {
            int level = (wave + 15) / 10 - random.nextInt(3);
            if (level < 0) level = 0;
            if (level > Enemy.MAX_LEVEL) level = Enemy.MAX_LEVEL;
            if(what[i] == 0){
                scene.add(MainScene.Layer.enemy, Enemy.get(level, i));
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
