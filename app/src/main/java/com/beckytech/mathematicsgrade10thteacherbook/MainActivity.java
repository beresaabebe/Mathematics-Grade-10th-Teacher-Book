package com.beckytech.mathematicsgrade10thteacherbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.activity.AboutActivity;
import com.beckytech.mathematicsgrade10thteacherbook.activity.BookDetailActivity;
import com.beckytech.mathematicsgrade10thteacherbook.activity.PrivacyActivity;
import com.beckytech.mathematicsgrade10thteacherbook.adapter.Adapter;
import com.beckytech.mathematicsgrade10thteacherbook.contents.ContentEndPage;
import com.beckytech.mathematicsgrade10thteacherbook.contents.ContentStartPage;
import com.beckytech.mathematicsgrade10thteacherbook.contents.SubTitleContents;
import com.beckytech.mathematicsgrade10thteacherbook.contents.TitleContents;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.onBookClicked {

    private InterstitialAd mInterstitialAd;
    private final List<Model> list = new ArrayList<>();
    private final ContentStartPage startPage = new ContentStartPage();
    private final TitleContents titleContents = new TitleContents();
    private final ContentEndPage endPage = new ContentEndPage();
    private final SubTitleContents subTitleContents = new SubTitleContents();
    private DrawerLayout drawerLayout;
    private AdView adView;
    private UpdateManager updateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        updateManager = new UpdateManager(this);
        updateManager.checkForUpdates();

        AppRate.app_launched(this);
        MobileAds.initialize(this, initializationStatus -> {});
        setAds();
        allContents();
        adaptiveAds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateManager.onResume();
    }

    private void allContents() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        drawerLayout.addDrawerListener(drawerToggle);

        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(item -> {
            MenuOptions(item);
            return true;
        });

        View menu = navigationView.getHeaderView(0);
        ImageView back_btn = menu.findViewById(R.id.back_btn_image);
        back_btn.setOnClickListener(view -> drawerLayout.closeDrawer(GravityCompat.START));
        back_btn.setColorFilter(ContextCompat.getColor(this,R.color.black));

        ImageView share_btn = menu.findViewById(R.id.share_btn_image);
        share_btn.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.app_name))
                    .putExtra(Intent.EXTRA_TEXT, "Download "+getString(R.string.app_name)+"\n"+url);
            startActivity(new Intent(Intent.createChooser(intent, "Share with")));
        });
        share_btn.setColorFilter(ContextCompat.getColor(this, R.color.black));

        TextView nav_title = menu.findViewById(R.id.tv_title);
        nav_title.setTextColor(Color.BLACK);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        getData();
        Adapter adapter = new Adapter(list, this);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        for (int i = 0; i < titleContents.title.length; i++) {
            list.add(new Model(titleContents.title[i],
                    subTitleContents.subTitle[i],
                    startPage.pageStart[i],
                    endPage.pageEnd[i]));
        }
    }

    private void adaptiveAds() {
        FrameLayout adContainerView = findViewById(R.id.adView_container);
        adView = new AdView(this);
        adContainerView.addView(adView);
        adView.setAdUnitId(getString(R.string.google_banner_ad_unit_id));
        loadBanner();
    }

    public AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    public void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void MenuOptions(MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        
        if (id == R.id.action_privacy) {
            startActivity(new Intent(this, PrivacyActivity.class));
        } else if (id == R.id.action_about_us) {
            showInterstitial(() -> startActivity(new Intent(this, AboutActivity.class)));
        } else if (id == R.id.action_rate) {
            AppRate.showRateDialog(this, getSharedPreferences("apprater", 0).edit());
        } else if (id == R.id.action_update) {
            updateManager.checkForUpdates();
        } else if (id == R.id.action_more_apps) {
            showInterstitial(() -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/dev?id=6669279757479011928"))));
        } else if (id == R.id.action_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            intent.putExtra(Intent.EXTRA_TEXT, "Download this app from Play store \n" + url);
            startActivity(Intent.createChooser(intent, "Choose to send"));
        } else if (id == R.id.action_exit) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Exit")
                    .setMessage("Do you want to close?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        finishAffinity();
                        System.exit(0);
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
        }
    }

    private void showInterstitial(Runnable runnable) {
        if (mInterstitialAd != null) {
            mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    runnable.run();
                    setAds();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    runnable.run();
                }
            });
            mInterstitialAd.show(this);
        } else {
            runnable.run();
        }
    }

    @Override
    public void clickedBook(Model model) {
        int position = list.indexOf(model);
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.putExtra("data", (ArrayList<Model>) list);
        intent.putExtra("pos", position);
        
        int rand = (int) (Math.random() * 100);
        if (rand % 2 != 0) {
            showInterstitial(() -> startActivity(intent));
        } else {
            startActivity(intent);
        }
    }

    private void setAds() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, getString(R.string.google_interstitial_ads_unit_id), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}