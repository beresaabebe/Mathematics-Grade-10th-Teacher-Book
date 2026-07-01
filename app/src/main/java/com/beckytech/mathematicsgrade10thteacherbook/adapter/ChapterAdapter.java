package com.beckytech.mathematicsgrade10thteacherbook.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import java.io.IOException;
import java.util.List;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.AdLoader;

public class ChapterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PAGE = 0;
    private static final int TYPE_AD_NATIVE = 1;
    private static final int TYPE_AD_RECTANGLE = 2;
    private static final int TYPE_AD_BANNER = 3;

    private final List<Integer> pages;
    private final ParcelFileDescriptor fileDescriptor;
    private PdfRenderer pdfRenderer;
    private final Activity activity;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public ChapterAdapter(List<Integer> pages, ParcelFileDescriptor fileDescriptor, Activity activity) {
        this.pages = pages;
        this.fileDescriptor = fileDescriptor;
        this.activity = activity;
        try {
            this.pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 5 == 0) {
            int adIndex = (position + 1) / 5;
            if (adIndex % 3 == 1) return TYPE_AD_NATIVE;
            if (adIndex % 3 == 2) return TYPE_AD_RECTANGLE;
            return TYPE_AD_BANNER;
        }
        return TYPE_PAGE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
            return new PageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_container, parent, false);
            return new AdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_PAGE) {
            int pageIndex = getPageIndex(position);
            renderPage(pageIndex, ((PageViewHolder) holder).photoView);
        } else {
            loadAd((AdViewHolder) holder, getItemViewType(position));
        }
    }

    private int getPageIndex(int position) {
        return pages.get(position - (position / 5));
    }

    private void renderPage(int pageIndex, PhotoView photoView) {
        if (pdfRenderer == null) return;
        int index = pageIndex - 1;
        if (index < 0 || index >= pdfRenderer.getPageCount()) return;

        executorService.execute(() -> {
            try {
                PdfRenderer.Page page = pdfRenderer.openPage(index);
                int width = activity.getResources().getDisplayMetrics().widthPixels;
                int height = (int) (width * (float) page.getHeight() / page.getWidth());
                Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                page.close();
                mainHandler.post(() -> photoView.setImageBitmap(bitmap));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadAd(AdViewHolder holder, int type) {
        if (holder.container.getChildCount() > 0) return;

        if (type == TYPE_AD_NATIVE) {
            loadNativeAdWithFallback(holder.container);
        } else if (type == TYPE_AD_RECTANGLE) {
            loadMediumRectangle(holder.container);
        } else {
            loadBannerAd(holder.container);
        }
    }

    private void loadNativeAdWithFallback(FrameLayout adContainer) {
        AdLoader adLoader = new AdLoader.Builder(activity, activity.getString(R.string.google_native_ads_unit_id))
                .forNativeAd(nativeAd -> {
                    // Fallback to Medium Rectangle as we haven't implemented Native Ad Layout
                    loadMediumRectangle(adContainer);
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                        loadMediumRectangle(adContainer);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder().build())
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private void loadMediumRectangle(FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                loadBannerAd(adContainer);
            }
        });
        adContainer.removeAllViews();
        adContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    private void loadBannerAd(FrameLayout adContainer) {
        AdView adView = new AdView(activity);
        adView.setAdUnitId(activity.getString(R.string.google_banner_ad_unit_id));
        adView.setAdSize(AdSize.BANNER);
        adContainer.removeAllViews();
        adContainer.addView(adView);
        adView.loadAd(new AdRequest.Builder().build());
    }

    public void close() {
        executorService.shutdown();
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
        try {
            if (fileDescriptor != null) {
                fileDescriptor.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pages.size() + (pages.size() / 4);
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        PhotoView photoView;
        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.pdf_page_image);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout container;
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            container = (FrameLayout) itemView;
        }
    }
}