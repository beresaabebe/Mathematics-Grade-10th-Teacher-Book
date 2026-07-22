package com.beckytech.mathematicsgrade10thteacherbook.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import java.util.List;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;

import com.beckytech.mathematicsgrade10thteacherbook.AdsManager;
import android.app.Activity;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_AD = 1;

    private final List<Model> list;
    private final onBookClicked bookClicked;
    private final AdsManager adsManager;
    private final Activity activity;

    public Adapter(List<Model> list, onBookClicked bookClicked, Activity activity) {
        this.list = list;
        this.bookClicked = bookClicked;
        this.activity = activity;
        this.adsManager = new AdsManager();
    }

    public interface onBookClicked {
        void clickedBook(Model model);
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % 4 == 0) {
            return TYPE_AD;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false));
        } else {
            return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_container, parent, false), adsManager);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            int index = position - (position / 4);
            Model model = list.get(index);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;
            itemHolder.title.setText(model.getTitle());
            itemHolder.subTitle.setText(model.getSubTitle());
            itemHolder.itemView.setOnClickListener(v -> bookClicked.clickedBook(model));
        } else {
            AdViewHolder adHolder = (AdViewHolder) holder;
            adHolder.loadAd(activity);
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + (list.size() / 3);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView title, subTitle;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setSelected(true);
            subTitle = itemView.findViewById(R.id.subTitle);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    static class AdViewHolder extends RecyclerView.ViewHolder {
        FrameLayout container;
        AdsManager adsManager;
        public AdViewHolder(@NonNull View itemView, AdsManager adsManager) {
            super(itemView);
            this.container = (FrameLayout) itemView;
            this.adsManager = adsManager;
        }

        void loadAd(Activity activity) {
            if (container.getChildCount() > 0) return;
            adsManager.loadAdWithFallback(activity, container);
        }
    }
}