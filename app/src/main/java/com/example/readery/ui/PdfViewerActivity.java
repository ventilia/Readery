package com.example.readery.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.readery.R;
import com.github.barteksc.pdfviewer.PDFView;


import java.io.File;

public class PdfViewerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        PDFView pdfView = findViewById(R.id.pdf_view);
        String pdfPath = getIntent().getStringExtra("pdfPath");

        if (pdfPath != null) {
            File pdfFile = new File(pdfPath);
            pdfView.fromFile(pdfFile)
                    .load();
        }
    }
}