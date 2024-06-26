package com.example.game.game;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.util.Log;
import android.graphics.Canvas;

import com.example.game.R;
import com.example.game.framework.activity.GameActivity;
import com.example.game.framework.objects.Score;
import com.example.game.framework.objects.HorzScrollBackground;
import com.example.game.framework.res.BitmapPool;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.interfaces.IGameObject;
import com.example.game.framework.objects.Button;
import com.example.game.framework.objects.Sprite;
import com.example.game.framework.util.Gauge;
import com.example.game.framework.res.Sound;

public class MainScene extends Scene {
    private static final String TAG = MainScene.class.getSimpleName();
    public static final String KEY_COOKIE_ID = "cookieId";

    public enum Layer {
        bg, platform, item, enemy, obstacle, bullet, swordEffect, player, ui, touch, gauge, controller, COUNT
    }
    private final Player player;
    private int cookieId = 107566; // default ID
    Score score;
    Item item;
    public int getScore() {
        return score.getScore();
    }
    protected Gauge attackGauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);
    protected Gauge fireGauge = new Gauge(0.1f, R.color.enemy_gauge_fg, R.color.enemy_gauge_bg);

    protected boolean bgChanged = false;
    public MainScene() {
        Intent intent = GameActivity.activity.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            cookieId = extras.getInt(KEY_COOKIE_ID);
        }
        initLayers(Layer.COUNT);

        add(Layer.bg, new HorzScrollBackground(R.mipmap.clouds, 0.5f));
        add(Layer.bg, new HorzScrollBackground(R.mipmap.bg, 1.5f));

        player = new Player(cookieId);
        add(Layer.player, player);

        add(Layer.controller, new EnemyGenerator());
        add(Layer.controller, new CollisionChecker(this, player));
        add(Layer.controller, new MapLoader(this));

        if(Player.getCookieId() == 107566){
            add(Layer.touch, new Button(R.mipmap.btn_attack_n, 12.5f, 7.7f, 2.0f, 0.75f, new Button.Callback() {
                @Override
                public boolean onTouch(Button.Action action) {
                    player.attack();
                    return true;
                }
            }));
        }
        add(Layer.touch, new Button(R.mipmap.btn_shoot_n, 14.5f, 7.7f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                player.throwing();
                return true;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_jump_n, 2.0f, 7.5f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                player.jump();
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_fall_n, 13.5f, 8.5f, 2.0f, 0.75f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                player.fall();
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_right_n, 3.0f, 8.3f, 1.0f, 1.0f, new Button.Callback() {
            @Override   // 오
            public boolean onTouch(Button.Action action) {
                player.running(action);
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_left_n, 1.0f, 8.3f, 1.0f, 1.0f, new Button.Callback() {
            @Override   // 왼
            public boolean onTouch(Button.Action action) {
                player.goBack(action);
                return false;
            }
        }));
        add(Layer.touch, new Button(R.mipmap.btn_pause, 15.0f, 1.0f, 1.0f, 1.0f, new Button.Callback() {
            @Override
            public boolean onTouch(Button.Action action) {
                new PausedScene().push();
                return false;
            }
        }));
        this.score = new Score(R.mipmap.number_24x32, 8.5f, 0.5f, 0.6f);
        score.setScore(0);
        add(Layer.ui, score);
        add(Layer.item, new Item(R.mipmap.throw_sword, 15.3f, 7.0f, 0.6f, 0));
        add(Layer.item, new Item(R.mipmap.heart, 0.1f, 0.3f, 1.0f, 1));
    }

    protected void onStart() {
        Sound.playMusic(R.raw.main);
    }

    @Override
    protected void onPause() {
        Sound.pauseMusic();
        pauseAnimations();
    }

    @Override
    protected void onResume() {
        resumeAnimations();
        Sound.resumeMusic();
    }
    @Override
    protected void onEnd() {
        Sound.stopMusic();
    }
    public void addScore(int amount) {
        score.add(amount);
        if(score.getScore() % 3000 == 0){
            Player.setLife(Player.getLife() + 1);
        }
    }
    @Override
    public void update(float elapsedSeconds) {
        super.update(elapsedSeconds);
        if(!bgChanged && MapObject.SPEED < -6.0f){
            clearLayer(Layer.bg);
            add(Layer.bg, new HorzScrollBackground(R.mipmap.cookie_run_bg_1, 0.5f));
            add(Layer.bg, new HorzScrollBackground(R.mipmap.cookie_run_bg_2, 1.0f));
            add(Layer.bg, new HorzScrollBackground(R.mipmap.cookie_run_bg_3, 1.5f));

            int[] newResIds = {
                    R.mipmap.cookierun_platform_480x48,
                    R.mipmap.cookierun_platform_120x40,
            };
            Platform.Type.setResIds(newResIds);
            bgChanged = true;
        }
    }

    @Override
    protected int getTouchLayerIndex() { return Layer.touch.ordinal(); }
    private void pauseAnimations() {
        for (IGameObject obj : objectsAt(Layer.obstacle)) {
            if (!(obj instanceof MapObject)) {
                continue;
            }
            ((MapObject)obj).pause();
        }
    }
    private void resumeAnimations() {
        for (IGameObject obj : objectsAt(Layer.obstacle)) {
            if (!(obj instanceof MapObject)) {
                continue;
            }
            ((MapObject)obj).resume();
        }
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        // 공격 게이지 그리기
        canvas.save();
        canvas.translate(12.0f, 7.9f); // 공격 게이지 위치 설정 (btn_attack_n 버튼 위)
        float attackCoolTime = Player.attackCoolTime;
        float attackInterval = Player.ATTACK_INTERVAL;
        if (attackCoolTime > 0) {
            attackGauge.draw(canvas, attackCoolTime/attackInterval);  // 공격 게이지 값을 직접 전달
        }
        canvas.restore();

        // 화염 게이지 그리기
        canvas.save();
        canvas.translate(14.0f, 7.9f); // 화염 게이지 위치 설정 (btn_attack_n 버튼 아래)
        float fireCoolTime = Player.fireCoolTime;
        float fireInterval = player.getFireInterval();
        if (fireCoolTime > 0) {
            fireGauge.draw(canvas, fireCoolTime/fireInterval);  // 화염 게이지 값을 직접 전달
        }
        canvas.restore();
    }
}
