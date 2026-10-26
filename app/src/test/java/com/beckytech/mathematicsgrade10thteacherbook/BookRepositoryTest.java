package com.beckytech.mathematicsgrade10thteacherbook;

import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class BookRepositoryTest {

    @Test
    public void getBookChapters_returnsNonEmptyChapterList() {
        BookRepository repository = new BookRepository();
        List<Model> chapters = repository.getBookChapters();

        assertNotNull(chapters);
        assertFalse("Chapter list should not be empty", chapters.isEmpty());
    }

    @Test
    public void getBookChapters_verifiesChapterDataStructure() {
        BookRepository repository = new BookRepository();
        List<Model> chapters = repository.getBookChapters();

        Model firstChapter = chapters.get(0);
        assertNotNull(firstChapter.getTitle());
        assertNotNull(firstChapter.getSubTitle());
        assertTrue("Start page should be positive", firstChapter.getStartPage() > 0);
        assertTrue("End page should be greater or equal to start page",
                firstChapter.getEndPage() >= firstChapter.getStartPage());
    }
}