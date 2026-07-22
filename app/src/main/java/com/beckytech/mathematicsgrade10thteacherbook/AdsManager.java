package com.beckytech.mathematicsgrade10thteacherbook;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import android.util.Log;
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
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.nativead.NativeAdView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.google.android.gms.ads.nativead.MediaView;

public class AdsManager {

    private RewardedAd rewardedAd;
    private RewardedInterstitialAd rewardedInterstitialAd;
    private static final String TAG = "AdsManager";

    public void loadAdWithFallback(Activity activity, FrameLayout adContainer) {
        Log.d(TAG, "Starting ad fallback sequence: Native -> Rectangle -> Banner");
        loadNativeAd(activity, adContainer);
    }

    public void loadNativeAd(Activity activity, FrameLayout adContainer) {
        String unitId = activity.getString(R.string.google_native_ads_unit_id);
        Log.d(TAG, "Attempting to load Native Ad: " + unitId);
        AdLoader adLoader = new AdLoader.Builder(activity, unitId)
                .forNativeAd(nativeAd -> {
                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater()
                            .inflate(R.layout.item_native_ad, null);
                    populateNativeAdView(nativeAd, adView);
                    adContainer.removeAllViews();
                    adContainer.addView(adView);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        Log.e(TAG, "Native ad failed to load: " + adError.getMessage() + ". Falling back to Medium Rectangle.");
                        loadMediumRectangle(activity, adContainer);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView(adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        if (adView.getHeadlineView() != null) {
            ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        }
        
        if (adView.getMediaView() != null && nativeAd.getMediaContent() != null) {
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());
        }

        if (adView.getBodyView() != null) {
            if (nativeAd.getBody() == null) {
                adView.getBodyView().setVisibility(View.INVISIBLE);
            } else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
            }
        }

        if (adView.getCallToActionView() != null) {
            if (nativeAd.getCallToAction() == null) {
                adView.getCallToActionView().setVisibility(View.INVISIBLE);
            } else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
            }
        }

        if (adView.getIconView() != null) {
            if (nativeAd.getIcon() == null) {
                adView.getIconView().setVisibility(View.GONE);
            } else {
                ((ImageView) adView.getIconView()).setImageDrawable(
                        nativeAd.getIcon().getDrawable());
                adView.getIconView().setVisibility(View.VISIBLE);
            }
        }

        if (adView.getPriceView() != null) {
            if (nativeAd.getPrice() == null) {
                adView.getPriceView().setVisibility(View.INVISIBLE);
            } else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
            }
        }

        if (adView.getStoreView() != null) {
            if (nativeAd.getStore() == null) {
                adView.getStoreView().setVisibility(View.INVISIBLE);
            } else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
            }
        }

        if (adView.getStarRatingView() != null) {
            if (nativeAd.getStarRating() == null) {
                adView.getStarRatingView().setVisibility(View.INVISIBLE);
            } else {
                ((RatingBar) adView.getStarRatingView())
                        .setRating(nativeAd.getStarRating().floatValue());
                adView.getStarRatingView().setVisibility(View.VISIBLE);
            }
        }

        if (adView.getAdvertiserView() != null) {
            if (nativeAd.getAdvertiser() == null) {
                adView.getAdvertiserView().setVisibility(View.INVISIBLE);
            } else {
                ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);
    }

    public void loadNativeAdWithFallback(Activity activity, FrameLayout adContainer) {
        loadAdWithFallback(activity, adContainer);
    }

    public void loadMediumRectangle(Activity activity, FrameLayout adContainer) {
        String unitId = activity.getString(R.string.google_banner_ad_unit_id);
        Log.d(TAG, "Attempting to load Medium Rectangle: " + unitId);
        AdView adView = new AdView(activity);
        adView.setAdUnitId(unitId);
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.e(TAG, "Medium Rectangle failed to load: " + loadAdError.getMessage() + ". Falling back to Banner.");
                loadBannerAd(activity, adContainer);
            }
        });
        adContainer.removeAllViews();
        adContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public void loadBannerAd(Activity activity, FrameLayout adContainer) {
        String unitId = activity.getString(R.string.google_banner_ad_unit_id);
        Log.d(TAG, "Attempting to load Standard Banner: " + unitId);
        AdView adView = new AdView(activity);
        adView.setAdUnitId(unitId);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                Log.e(TAG, "Banner failed to load: " + adError.getMessage());
            }
        });
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
                        Log.e(TAG, "Rewarded ad failed to load: " + loadAdError.getMessage());
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
                        Log.e(TAG, "Rewarded Interstitial ad failed to load: " + loadAdError.getMessage());
                        rewardedInterstitialAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedInterstitialAd ad) {
                        rewardedInterstitialAd = ad;
                    }
                });
    }

    public void showRewardedAd(Activity activity, Runnable onAdDismissed) {
        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(true);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(false);
                    if (onAdDismissed != null) onAdDismissed.run();
                    loadRewardedAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    MyApplication.setFullScreenContentShowing(false);
                    if (onAdDismissed != null) onAdDismissed.run();
                }
            });
            rewardedAd.show(activity, rewardItem -> {
                // Reward logic here
            });
        } else {
            if (onAdDismissed != null) onAdDismissed.run();
        }
    }

    public void showRewardedInterstitialAd(Activity activity, Runnable onAdDismissed) {
        if (rewardedInterstitialAd != null) {
            rewardedInterstitialAd.setFullScreenContentCallback(new com.google.android.gms.ads.FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(true);
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    MyApplication.setFullScreenContentShowing(false);
                    if (onAdDismissed != null) onAdDismissed.run();
                    loadRewardedInterstitialAd(activity);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                    MyApplication.setFullScreenContentShowing(false);
                    if (onAdDismissed != null) onAdDismissed.run();
                }
            });
            rewardedInterstitialAd.show(activity, rewardItem -> {
                // Reward logic here
            });
        } else {
            if (onAdDismissed != null) onAdDismissed.run();
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