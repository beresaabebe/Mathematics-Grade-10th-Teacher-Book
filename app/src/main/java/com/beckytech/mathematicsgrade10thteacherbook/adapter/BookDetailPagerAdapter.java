package com.beckytech.mathematicsgrade10thteacherbook.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.beckytech.mathematicsgrade10thteacherbook.activity.ChapterFragment;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.List;

public class BookDetailPagerAdapter extends FragmentStateAdapter {

    private final List<Model> models;

    public BookDetailPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Model> models) {
        super(fragmentActivity);
        this.models = models;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return ChapterFragment.newInstance(models.get(position));
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}