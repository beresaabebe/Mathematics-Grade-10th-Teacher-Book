package com.beckytech.mathematicsgrade10thteacherbook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.List;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    private final BookRepository repository;
    private final MutableLiveData<List<Model>> chaptersLiveData = new MutableLiveData<>();

    @Inject
    public MainViewModel(BookRepository repository) {
        this.repository = repository;
        loadChapters();
    }

    public LiveData<List<Model>> getChapters() {
        return chaptersLiveData;
    }

    private void loadChapters() {
        chaptersLiveData.setValue(repository.getBookChapters());
    }
}