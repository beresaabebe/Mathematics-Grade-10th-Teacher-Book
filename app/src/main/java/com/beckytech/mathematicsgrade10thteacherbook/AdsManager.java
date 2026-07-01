package com.beckytech.mathematicsgrade10thteacherbook;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.AdLoader;

public class AdsManager {

    private RewardedAd rewardedAd;
    private RewardedInterstitialAd rewardedInterstitialAd;

    public void loadNativeAdWithFallback(Activity activity, FrameLayout adContainer) {
        AdLoader adLoader = new AdLoader.Builder(activity, activity.getString(R.string.google_native_ads_unit_id))
                .forNativeAd(nativeAd -> {
                    // Logic for displaying native ad would go here.
                    // If not implemented, we fallback to Medium Rectangle.
                    loadMediumRectangle(activity, adContainer);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        loadMediumRectangle(activity, adContainer);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    public void loadMediumRectangle(Activity activity, FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                loadBannerAd(activity, adContainer);
            }
        });
        adContainer.removeAllViews();
        adContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public void loadBannerAd(Activity activity, FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        adContainer.removeAllViews();
        adContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public void loadRewardedAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(activity, activity.getString(R.string.google_rewarded_ads_unit_id),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        rewardedAd = ad;
                    }
                });
    }

    public void loadRewardedInterstitialAd(Activity activity) {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedInterstitialAd.load(activity, activity.getString(R.string.google_rewarded_interstitial_ads_unit_id),
                adRequest, new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        rewardedInterstitialAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                    }
                });
    }

    public void showRewardedAd(Activity activity) {
        if (rewardedAd != null) {
            rewardedAd.show(activity, rewardItem -> {
                loadRewardedAd(activity);
            });
        }
    }

    public void showRewardedInterstitialAd(Activity activity) {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.show(activity, rewardItem -> {
                loadRewardedInterstitialAd(activity);
            });
        }
    }

    public void loadCollapsibleBanner(Activity activity, FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adContainer.removeAllViews();
        adContainer.addView(adView);

        AdSize adSize = getAdSize(activity);
        adView.setAdSize(adSize);

        Bundle extras = new Bundle();
        extras.putString("collapsible", "bottom");

        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                .build();

        adView.loadAd(adRequest);
    }

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }
}