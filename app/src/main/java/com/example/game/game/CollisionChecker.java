package com.example.game.game;

import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;

import com.example.game.framework.interfaces.IBoxCollidable;
import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.util.CollisionHelper;

public class CollisionChecker implements IGameObject {
    private static final String TAG = CollisionChecker.class.getSimpleName();
    private final MainScene scene;
    private final Player player;

    public CollisionChecker(MainScene scene, Player player) {
        this.scene = scene;
        this.player = player;
    }

    @Override
    public void update(float elapsedSeconds) {
        // bullet enemy 충돌체크
        ArrayList<IGameObject> enemies = scene.objectsAt(MainScene.Layer.enemy);
        for (int e = enemies.size() - 1; e >= 0; e--) {
            Enemy enemy = (Enemy)enemies.get(e);
            ArrayList<IGameObject> bullets = scene.objectsAt(MainScene.Layer.bullet);
            for (int b = bullets.size() - 1; b >= 0; b--) {
                Bullet bullet = (Bullet)bullets.get(b);
                if (CollisionHelper.collides(enemy, bullet)) {
                    Log.d(TAG, "Collision !!");
                    scene.remove(MainScene.Layer.bullet, bullet);
                    scene.remove(MainScene.Layer.enemy, enemy);
                    scene.addScore(enemy.getScore());
                    break;
                }
            }
        }
        // sword enemy 충돌체크
        for (int e = enemies.size() - 1; e >= 0; e--) {
            Enemy enemy = (Enemy)enemies.get(e);
            ArrayList<IGameObject> swordEffects = scene.objectsAt(MainScene.Layer.swordEffect);
            for (int b = swordEffects.size() - 1; b >= 0; b--) {
                Attack swordEffect = (Attack)swordEffects.get(b);
                if (CollisionHelper.collides(enemy, swordEffect)) {
                    Log.d(TAG, "Collision !!");
                    scene.remove(MainScene.Layer.enemy, enemy);
                    scene.addScore(enemy.getScore());
                    break;
                }
            }
        }
        // player item 충돌체크
        ArrayList<IGameObject> items = scene.objectsAt(MainScene.Layer.item);
        for (int i = items.size() - 1; i >= 0; i--) {
            IGameObject gobj = items.get(i);
            if (!(gobj instanceof IBoxCollidable)) {
                continue;
            }
            if (CollisionHelper.collides(player, (IBoxCollidable) gobj)) {
                scene.remove(MainScene.Layer.item, gobj);
                scene.addScore(JellyItem.getScore());
            }
        }
        // player Obstacle 충돌체크
        ArrayList<IGameObject> obstacles = scene.objectsAt(MainScene.Layer.obstacle);
        if(!Player.isInvincibility) {
            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obstacle = (Obstacle) obstacles.get(i);
                if (CollisionHelper.collides(player, obstacle)) {
                    player.hurt(obstacle);
                }
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {

    }
}
