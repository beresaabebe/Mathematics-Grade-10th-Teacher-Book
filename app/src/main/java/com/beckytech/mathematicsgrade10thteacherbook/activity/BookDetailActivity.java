package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.ActivityBookDetailBinding;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.List;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookDetailActivity extends AppCompatActivity {
    private List<Model> models;
    private int currentPos;
    private AdsManager adsManager;
    private ActivityBookDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        binding = ActivityBookDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.getRoot(), (v, windowInsets) -> {
            androidx.core.graphics.Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

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
        binding.backBookDetail.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.backBookDetail.setColorFilter(ContextCompat.getColor(this, R.color.white));

        binding.btnNext.setOnClickListener(v -> navigateNext());
        binding.btnPrevious.setOnClickListener(v -> navigatePrevious());
    }

    private int transitionCount = 0;

    private void navigateNext() {
        if (currentPos < models.size() - 1) {
            currentPos++;
            transitionCount++;
            adsManager.showRewardedInterstitialAd(this, () -> {
                showChapter(currentPos);
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
        binding.btnPrevious.setEnabled(currentPos > 0);
        binding.btnNext.setEnabled(currentPos < models.size() - 1);
    }

    private void updateTitle(int position) {
        Model model = models.get(position);
        binding.titleBookDetail.setText(model.getTitle());
        binding.subTitleBookDetail.setText(model.getSubTitle());
    }

    private void setupAds() {
        adsManager.loadCollapsibleBanner(this, binding.adViewContainer);
    }

    private void shareBtn() {
        binding.shareBtnImage.setColorFilter(ContextCompat.getColor(this, R.color.white));
        binding.shareBtnImage.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/details?id=" + getPackageName();
            Intent intent = new Intent(Intent.ACTION_SEND)
                    .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    .putExtra(Intent.EXTRA_TEXT, "Download " + getString(R.string.app_name) + "\n" + url)
                    .setType("text/plain");
            startActivity(Intent.createChooser(intent, "Share with"));
        });
    }
}