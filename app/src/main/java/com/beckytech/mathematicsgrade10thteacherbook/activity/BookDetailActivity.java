package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.widget.Button;
import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.List;

public class BookDetailActivity extends AppCompatActivity {
    private List<Model> models;
    private int currentPos;
    private AdsManager adsManager;
    private TextView title, subTitle;
    private Button btnNext, btnPrevious;

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
        showChapter(currentPos);
        setupAds();
        shareBtn();
    }

    private void initViews() {
        ImageButton back_btn = findViewById(R.id.back_book_detail);
        back_btn.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        back_btn.setColorFilter(ContextCompat.getColor(this, R.color.white));

        title = findViewById(R.id.title_book_detail);
        subTitle = findViewById(R.id.sub_title_book_detail);
        
        btnNext = findViewById(R.id.btn_next);
        btnPrevious = findViewById(R.id.btn_previous);
        
        btnNext.setOnClickListener(v -> navigateNext());
        btnPrevious.setOnClickListener(v -> navigatePrevious());
    }

    private int transitionCount = 0;

    private void navigateNext() {
        if (currentPos < models.size() - 1) {
            currentPos++;
            transitionCount++;
            adsManager.showRewardedInterstitialAd(this, () -> {
                showChapter(currentPos);
                // Refresh collapsible banner every 5 transitions to trigger high-revenue "expansion"
                if (transitionCount % 5 == 0) {
                    setupAds();
                }
            });
        }
    }

    private void navigatePrevious() {
        if (currentPos > 0) {
            currentPos--;
            showChapter(currentPos);
        }
    }

    private void showChapter(int position) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, ChapterFragment.newInstance(models.get(position)))
                .commit();
        updateTitle(position);
        updateButtonVisibility();
    }

    private void updateButtonVisibility() {
        btnPrevious.setEnabled(currentPos > 0);
        btnNext.setEnabled(currentPos < models.size() - 1);
    }

    private void updateTitle(int position) {
        Model model = models.get(position);
        title.setText(model.getTitle());
        subTitle.setText(model.getSubTitle());
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