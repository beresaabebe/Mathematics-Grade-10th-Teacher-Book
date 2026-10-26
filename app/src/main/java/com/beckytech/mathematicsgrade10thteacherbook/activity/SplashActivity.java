package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.beckytech.mathematicsgrade10thteacherbook.MainActivity;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.ActivitySplashBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }
}