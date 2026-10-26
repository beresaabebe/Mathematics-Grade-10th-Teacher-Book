package com.beckytech.mathematicsgrade10thteacherbook;

import com.beckytech.mathematicsgrade10thteacherbook.contents.ContentEndPage;
import com.beckytech.mathematicsgrade10thteacherbook.contents.ContentStartPage;
import com.beckytech.mathematicsgrade10thteacherbook.contents.SubTitleContents;
import com.beckytech.mathematicsgrade10thteacherbook.contents.TitleContents;
import com.beckytech.mathematicsgrade10thteacherbook.model.Model;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BookRepository {

    private final ContentStartPage startPage = new ContentStartPage();
    private final TitleContents titleContents = new TitleContents();
    private final ContentEndPage endPage = new ContentEndPage();
    private final SubTitleContents subTitleContents = new SubTitleContents();

    @Inject
    public BookRepository() {}

    public List<Model> getBookChapters() {
        List<Model> list = new ArrayList<>();
        for (int i = 0; i < titleContents.title.length; i++) {
            list.add(new Model(titleContents.title[i],
                    subTitleContents.subTitle[i],
                    startPage.pageStart[i],
                    endPage.pageEnd[i]));
        }
        return list;
    }
}