package com.example.game.app;

import android.os.Bundle;

import com.example.game.BuildConfig;
import com.example.game.framework.activity.GameActivity;
import com.example.game.game.MainScene;
import com.example.game.framework.scene.Scene;
import com.example.game.framework.view.Metrics;

public class RunningActivity extends GameActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Scene.drawsDebugInfo = BuildConfig.DEBUG;
        Metrics.setGameSize(16, 9);
        super.onCreate(savedInstanceState);
        // Scene.drawsDebugInfo 변경 시점에 주의한다.
        // new GameView() 가 호출되는 super.onCreate() 보다 이전에 해야 한다.
        new MainScene().push();
    }
}