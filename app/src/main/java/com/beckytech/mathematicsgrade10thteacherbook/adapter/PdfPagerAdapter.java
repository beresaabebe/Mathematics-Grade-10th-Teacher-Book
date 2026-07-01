package com.beckytech.mathematicsgrade10thteacherbook.adapter;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.beckytech.mathematicsgrade10thteacherbook.R;
import java.io.IOException;
import java.util.List;

public class PdfPagerAdapter extends RecyclerView.Adapter<PdfPagerAdapter.PdfViewHolder> {

    private final List<Integer> pages;
    private final ParcelFileDescriptor fileDescriptor;
    private PdfRenderer pdfRenderer;

    public PdfPagerAdapter(List<Integer> pages, ParcelFileDescriptor fileDescriptor) {
        this.pages = pages;
        this.fileDescriptor = fileDescriptor;
        try {
            this.pdfRenderer = new PdfRenderer(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public PdfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_page, parent, false);
        return new PdfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PdfViewHolder holder, int position) {
        int pageIndex = pages.get(position);
        renderPage(pageIndex, holder.imageView);
    }

    private void renderPage(int pageIndex, ImageView imageView) {
        if (pdfRenderer == null) return;
        
        // Adjust page index if necessary (PdfRenderer is 0-indexed)
        // assuming input pageIndex is 1-indexed as per previous code
        int index = pageIndex - 1;
        if (index < 0 || index >= pdfRenderer.getPageCount()) return;

        PdfRenderer.Page page = pdfRenderer.openPage(index);
        
        // Determine bitmap size. For high quality, we can use a multiple of the page size.
        int width = imageView.getContext().getResources().getDisplayMetrics().widthPixels;
        int height = (int) (width * (float) page.getHeight() / page.getWidth());
        
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        imageView.setImageBitmap(bitmap);
        page.close();
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        if (pdfRenderer != null) {
            pdfRenderer.close();
        }
    }

    static class PdfViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PdfViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pdf_page_image);
        }
    }
}