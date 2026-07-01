package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;
import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.adapter.BookDetailPagerAdapter;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BookDetailActivity extends AppCompatActivity {
    private List<Model> models;
    private int currentPos;
    private ViewPager2 viewPager2;
    private AdsManager adsManager;
    private TextView title, subTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        adsManager = new AdsManager();
        adsManager.loadRewardedAd(this);
        adsManager.loadRewardedInterstitialAd(this);

        models = (List<Model>) getIntent().getSerializableExtra("data");
        currentPos = getIntent().getIntExtra("pos", 0);

        initViews();
        setupViewPager();
        setupAds();
        shareBtn();
    }

    private void initViews() {
        ImageButton back_btn = findViewById(R.id.back_book_detail);
        back_btn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        back_btn.setColorFilter(ContextCompat.getColor(this, R.color.white));

        title = findViewById(R.id.title_book_detail);
        subTitle = findViewById(R.id.sub_title_book_detail);
        viewPager2 = findViewById(R.id.viewPager2);
    }

    private void setupViewPager() {
        BookDetailPagerAdapter adapter = new BookDetailPagerAdapter(this, models);
        viewPager2.setAdapter(adapter);
        viewPager2.setCurrentItem(currentPos, false);
        updateTitle(currentPos);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateTitle(position);
                showRandomRewardedAd();
            }
        });
    }

    private void updateTitle(int position) {
        Model model = models.get(position);
        title.setText(model.getTitle());
        subTitle.setText(model.getSubTitle());
    }

    private void showRandomRewardedAd() {
        Random random = new Random();
        int rand = random.nextInt(100);
        if (rand % 2 == 0) {
            adsManager.showRewardedAd(this);
        } else {
            adsManager.showRewardedInterstitialAd(this);
        }
    }

    private void setupAds() {
        FrameLayout adContainer = findViewById(R.id.adView_container);
        adsManager.loadCollapsibleBanner(this, adContainer);
    }

    private void shareBtn() {
        ImageView share_btn = findViewById(R.id.share_btn_image);
        share_btn.setColorFilter(ContextCompat.getColor(this, R.color.white));
        share_btn.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    .putExtra(Intent.EXTRA_TEXT, "Download " + getString(R.string.app_name) + "\n" + url)
                    .setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share with"));
        });
    }
}