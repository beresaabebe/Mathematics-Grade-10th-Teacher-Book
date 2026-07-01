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

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_AD = 1;

    private final List<Model> list;
    private final onBookClicked bookClicked;

    public Adapter(List<Model> list, onBookClicked bookClicked) {
        this.list = list;
        this.bookClicked = bookClicked;
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
            return new AdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_container, parent, false));
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
            adHolder.loadAd();
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
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
            container = (FrameLayout) itemView;
        }

        void loadAd() {
            if (container.getChildCount() > 0) return;
            AdView adView = new AdView(container.getContext());
            adView.setAdUnitId(container.getContext().getString(R.string.google_banner_ad_unit_id));
            adView.setAdSize(AdSize.BANNER);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Fallback or retry logic if needed, but for now we just show banner
                }
            });
            container.removeAllViews();
            container.addView(adView);
            adView.loadAd(new AdRequest.Builder().build());
        }
    }
}