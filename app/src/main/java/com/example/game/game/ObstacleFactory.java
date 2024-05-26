package com.example.game.game;

import com.example.game.framework.scene.RecycleBin;

public class ObstacleFactory {
    public static final int COUNT = 4;
    public static Obstacle get(int index, float left, float top) {
        Obstacle obs = null;
        switch (index) {
            case 0:
                obs = (Obstacle) RecycleBin.get(Obstacle.class);
                if (obs == null) {
                    obs = new Obstacle();
                }
                break;
            case 1: case 2:
                obs = (Obstacle) RecycleBin.get(AnimObstacle.class);
                if (obs == null) {
                    obs = new AnimObstacle();
                }
                break;
            default:
                obs = (Obstacle) RecycleBin.get(FallingObstacle.class);
                if (obs == null) {
                    obs = new FallingObstacle();
                }
                break;
        }
        // init 는 protected 이지만 같은 package 내에 있어서 호출이 가능하다
        // package 가 달라진다면 public 으로 변경해 주어야 한다
        obs.init(index, left, top);
        return obs;
    }
}

