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
import com.beckytech.mathematicsgrade10thteacherbook.adapter.ChapterAdapter;
import com.beckytech.mathematicsgrade10thteacherbook.databinding.FragmentChapterBinding;
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
    private FragmentChapterBinding binding;
    private ChapterAdapter adapter;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentChapterBinding.inflate(inflater, container, false);
        binding.chapterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Integer> pages = new ArrayList<>();
        if (model != null) {
            for (int i = model.getStartPage(); i <= model.getEndPage(); i++) {
                pages.add(i);
            }
        }

        try {
            ParcelFileDescriptor pfd = getParcelFileDescriptorFromAssets("maths.pdf");
            adapter = new ChapterAdapter(pages, pfd, getActivity());
            binding.chapterRecyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.close();
        }
        binding = null;
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