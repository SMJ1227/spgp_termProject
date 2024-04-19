package com.example.game.app;

import android.os.Bundle;

import com.example.game.framework.activity.GameActivity;
import com.example.game.game.MainScene;

public class RunningActivity extends GameActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MainScene().push();
    }
}