package com.example.game.app;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;

import com.example.game.BuildConfig;
import com.example.game.R;
import com.example.game.databinding.ActivityMainBinding;
import com.example.game.game.MainScene;
import com.example.game.game.Player;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int cookieIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCookieIndex(0);
    }
    private void runGameActivity() {
        Intent intent = new Intent(this, RunningActivity.class);
        intent.putExtra(MainScene.KEY_COOKIE_ID, Player.COOKIE_IDS[cookieIndex]);
        startActivity(intent);
    }
    private void setCookieIndex(int index) {
        this.cookieIndex = index;
        try {
            int cookieId = Player.COOKIE_IDS[index];
            AssetManager assets = getAssets();
            String fileName = cookieId + "_icon.png";
            InputStream is = assets.open(fileName);
            Bitmap bmp = BitmapFactory.decodeStream(is);
            binding.cookieImageView.setImageBitmap(bmp);
            Player.CookieInfo cookieInfo = Player.cookieInfoMap.get(cookieId);
            if (cookieInfo != null) {
                binding.KirbyNameTextView.setText(cookieInfo.name);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void onBtnStartGame(View view) {
        runGameActivity();
    }
    public void onBtnPreviousCookie(View view) {
        setCookieIndex(cookieIndex - 1);
    }
    public void onBtnNextCookie(View view) {
        setCookieIndex(cookieIndex + 1);
    }
}