package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.ActivityPrivacyBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PrivacyActivity extends AppCompatActivity {

    private AdsManager adsManager;
    private ActivityPrivacyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            androidx.core.graphics.Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        adsManager = new AdsManager();
        setupAds();
        allContents();
    }

    private void setupAds() {
        adsManager.loadCollapsibleBanner(this, binding.adViewContainer);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void allContents() {
        binding.ibBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.titleColor));
        binding.progressHorizontal.setVisibility(View.GONE);

        binding.tvTitle.setText(R.string.privacy_title);
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.titleColor));

        binding.webViewPrivacy.loadUrl("https://yoosaad.com/privacy/");
        binding.webViewPrivacy.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webViewPrivacy.getSettings().getLoadsImagesAutomatically();
        binding.webViewPrivacy.getSettings().setJavaScriptEnabled(true);
        binding.webViewPrivacy.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.progressHorizontal.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.progressHorizontal.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                binding.webViewPrivacy.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, request, error);
            }
        });
    }
}