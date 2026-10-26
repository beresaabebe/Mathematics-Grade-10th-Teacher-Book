package com.beckytech.mathematicsgrade10thteacherbook;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private BookRepository repository;

    private MainViewModel viewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<Model> mockChapters = new ArrayList<>();
        mockChapters.add(new Model("Chapter 1", "Sub Title 1", 1, 10));
        when(repository.getBookChapters()).thenReturn(mockChapters);

        viewModel = new MainViewModel(repository);
    }

    @Test
    public void getChapters_loadsChaptersFromRepository() {
        List<Model> result = viewModel.getChapters().getValue();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Chapter 1", result.get(0).getTitle());
        verify(repository, times(1)).getBookChapters();
    }
}