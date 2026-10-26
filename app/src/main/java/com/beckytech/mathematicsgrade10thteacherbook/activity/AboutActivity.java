package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.adapter.AboutAdapter;
import com.beckytech.mathematicsgrade10thteacherbook.contents.AboutImages;
import com.beckytech.mathematicsgrade10thteacherbook.contents.AboutName;
import com.beckytech.mathematicsgrade10thteacherbook.contents.AboutUrlContents;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.ActivityAboutBinding;
import com.beckytech.mathematicsgrade10thteacherbook.model.AboutModel;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AboutActivity extends AppCompatActivity implements AboutAdapter.OnLinkClicked {
    private final AboutImages images = new AboutImages();
    private final AboutName name = new AboutName();
    private final AboutUrlContents urlContents = new AboutUrlContents();
    List<AboutModel> modelList;
    private AdsManager adsManager;
    private ActivityAboutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            androidx.core.graphics.Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        MobileAds.initialize(this, initializationStatus -> {});
        adsManager = new AdsManager();
        setupAds();
        allContents();
    }

    private void allContents() {
        binding.ibBack.setOnClickListener(v -> onBackPressed());
        binding.ibBack.setColorFilter(ContextCompat.getColor(this, R.color.titleColor));

        String str = "About us";
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, R.color.titleColor));
        binding.tvTitle.setText(str);

        binding.webView.loadUrl("file:///android_asset/about.html");

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getBaseContext().getPackageName(), 0);
            binding.versionTv.setText(String.format(Locale.ENGLISH, " %s", info.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.imageView.setOnClickListener(view -> {
            Toast.makeText(AboutActivity.this, "Share me, let other know about me!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            intent.putExtra(Intent.EXTRA_TEXT, R.string.app_name);
            startActivity(Intent.createChooser(intent, "Share me via "));
        });

        getData();
        AboutAdapter adapter = new AboutAdapter(modelList, this);
        binding.recyclerAbout.setAdapter(adapter);
    }

    private void getData() {
        modelList = new ArrayList<>();
        for (int i = 0; i < name.name.length; i++) {
            modelList.add(new AboutModel(images.images[i], name.name[i], urlContents.url[i]));
        }
    }

    private void setupAds() {
        adsManager.loadCollapsibleBanner(this, binding.adViewContainer);
    }

    @Override
    public void linkClicked(AboutModel model) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(model.getUrl()));
        startActivity(intent);
    }
}