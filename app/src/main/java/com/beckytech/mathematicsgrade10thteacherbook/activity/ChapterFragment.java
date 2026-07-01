package com.beckytech.mathematicsgrade10thteacherbook.activity;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import com.beckytech.mathematicsgrade10thteacherbook.adapter.ChapterAdapter;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChapterFragment extends Fragment {

    private static final String ARG_MODEL = "model";
    private Model model;

    public static ChapterFragment newInstance(Model model) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MODEL, model);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            model = (Model) getArguments().getSerializable(ARG_MODEL);
        }
    }

    private ChapterAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.chapterRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Integer> pages = new ArrayList<>();
        for (int i = model.getStartPage(); i <= model.getEndPage(); i++) {
            pages.add(i);
        }

        try {
            ParcelFileDescriptor pfd = getParcelFileDescriptorFromAssets("maths.pdf");
            adapter = new ChapterAdapter(pages, pfd, getActivity());
            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.close();
        }
    }

    private ParcelFileDescriptor getParcelFileDescriptorFromAssets(String assetName) throws IOException {
        File file = new File(requireContext().getCacheDir(), assetName);
        if (!file.exists()) {
            AssetManager assetManager = requireContext().getAssets();
            try (InputStream is = assetManager.open(assetName);
                 FileOutputStream fos = new FileOutputStream(file)) {
                byte[] buffer = new byte[1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, read);
                }
            }
        }
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
    }
}