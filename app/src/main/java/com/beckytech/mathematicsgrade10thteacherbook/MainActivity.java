package com.beckytech.mathematicsgrade10thteacherbook;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.activity.AboutActivity;
import com.beckytech.mathematicsgrade10thteacherbook.activity.BookDetailActivity;
import com.beckytech.mathematicsgrade10thteacherbook.activity.PrivacyActivity;
import com.beckytech.mathematicsgrade10thteacherbook.adapter.Adapter;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.ActivityMainDrawerBinding;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements Adapter.onBookClicked {

    private InterstitialAd mInterstitialAd;
    private final List<Model> list = new ArrayList<>();
    private ActivityMainDrawerBinding binding;
    private AdsManager adsManager;
    private UpdateManager updateManager;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityMainDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout, (v, windowInsets) -> {
            androidx.core.graphics.Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        adsManager = new AdsManager();
        updateManager = new UpdateManager(this);
        updateManager.checkForUpdates();

        AppRate.app_launched(this);
        MobileAds.initialize(this, initializationStatus -> {});
        setAds();
        allContents();
        setupAds();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateManager.onResume();
    }

    private void allContents() {
        binding.toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
        drawerToggle.syncState();
        drawerToggle.getDrawerArrowDrawable().setColor(Color.BLACK);
        binding.drawerLayout.addDrawerListener(drawerToggle);

        binding.navigationView.setNavigationItemSelectedListener(item -> {
            MenuOptions(item);
            return true;
        });

        View menu = binding.navigationView.getHeaderView(0);
        ImageView back_btn = menu.findViewById(R.id.back_btn_image);
        back_btn.setOnClickListener(view -> binding.drawerLayout.closeDrawer(GravityCompat.START));
        back_btn.setColorFilter(ContextCompat.getColor(this, R.color.black));

        ImageView share_btn = menu.findViewById(R.id.share_btn_image);
        share_btn.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND).setType("text/plain");
            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    .putExtra(Intent.EXTRA_TEXT, "Download " + getString(R.string.app_name) + "\n" + url);
            startActivity(new Intent(Intent.createChooser(intent, "Share with")));
        });
        share_btn.setColorFilter(ContextCompat.getColor(this, R.color.black));

        TextView nav_title = menu.findViewById(R.id.tv_title);
        nav_title.setTextColor(Color.BLACK);

        RecyclerView recyclerView = binding.mainContent.recyclerView;
        Adapter adapter = new Adapter(list, this, this);
        recyclerView.setAdapter(adapter);

        viewModel.getChapters().observe(this, chapters -> {
            if (chapters != null) {
                list.clear();
                list.addAll(chapters);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setupAds() {
        adsManager.loadCollapsibleBanner(this, binding.mainContent.adViewContainer);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    void MenuOptions(MenuItem item) {
        binding.drawerLayout.closeDrawer(GravityCompat.START);
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
                public void onAdShowedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(true);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(false);
                    runnable.run();
                    setAds();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    MyApplication.setFullScreenContentShowing(false);
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
        
        // Show interstitial only 30% of the time to balance revenue and UX
        int rand = (int) (Math.random() * 100);
        if (rand < 30) {
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
                        Log.e("MainActivity", "Interstitial failed to load: " + loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
    }
}