package com.example.game.game;

import android.graphics.Canvas;

import java.util.Random;

import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.view.Metrics;

public class MapLoader implements IGameObject {
    private final MainScene scene;
    private final Random random = new Random();
    private float platformX, itemX;
    public MapLoader(MainScene scene) {
        this.scene = scene;
    }

    @Override
    public void update(float elapsedSeconds) {
        platformX += MapObject.SPEED * elapsedSeconds;
        while (platformX < Metrics.width) {
            Platform platform = Platform.get(Platform.Type.RANDOM, platformX, 7, 0); //Metrics.height - 2);
            platform.addToScene();
            platformX += platform.getWidth();
        }
        itemX += MapObject.SPEED * elapsedSeconds;
        while (itemX < Metrics.width) {
            int y = 2;;// = (random.nextInt(3) + 1) * 2;
            int count = 3;
            int y2;
            for(int i = 0; i < count; i++){
                Platform platform = Platform.get(Platform.Type.T_3x1, itemX, y, i);
                platform.addToScene();
                y2 = y - 1;
                JellyItem jellyItem = JellyItem.get(JellyItem.RANDOM, itemX, y2);
                jellyItem.addToScene();
                itemX += jellyItem.getWidth();
                y = y + 2;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
