package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.beckytech.mathematicsgrade10thteacherbook.R;

public class PrivacyActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        allContents();
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void allContents() {
        ImageButton ib_back = findViewById(R.id.ib_back);
        ib_back.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        ib_back.setColorFilter(ContextCompat.getColor(this, R.color.titleColor));
        ProgressBar progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setVisibility(View.GONE);

        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.privacy_title);
        tv_title.setTextColor(ContextCompat.getColor(this, R.color.titleColor));

        webView = findViewById(R.id.webView_privacy);
        webView.loadUrl("https://yoosaad.com/privacy/");
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().getLoadsImagesAutomatically();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webView.loadUrl("file:///android_asset/error.html");
                super.onReceivedError(view, request, error);
            }
        });
    }

}