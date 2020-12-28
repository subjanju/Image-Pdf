package com.sanju.jpgtopdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfToImageActivity extends AppCompatActivity {

    Button button;
    // Getting images from Test.pdf file.
    File source = new File(Environment.getExternalStorageDirectory() + "/" + "Text" + ".pdf");

    // Images will be saved in Test folder.
    File destination = new File(Environment.getExternalStorageDirectory() + "/Test");

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_to_image);

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Getting images from pdf in png format.
                try {
                    getImagesFromPDF(source, destination);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    // This method is used to extract all pages in image (PNG) format.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getImagesFromPDF(File pdfFilePath, File DestinationFolder) throws IOException {

//        // Getting images from Test.pdf file.
//        File source = new File(Environment.getExternalStorageDirectory() + "/" + "Test" + ".pdf");
//
//        // Images will be saved in Test folder.
//        File destination = new File(Environment.getExternalStorageDirectory() + "/Test");
//
//        // Getting images from pdf in png format.
//        try {
//            getImagesFromPDF(source, destination);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Check if destination already exists then delete destination folder.
        if(DestinationFolder.exists()){
            DestinationFolder.delete();
        }

        // Create empty directory where images will be saved.
        DestinationFolder.mkdirs();

        // Reading pdf in READ Only mode.
        ParcelFileDescriptor fileDescriptor = ParcelFileDescriptor.open(pdfFilePath, ParcelFileDescriptor.MODE_READ_ONLY);

        // Initializing PDFRenderer object.
        PdfRenderer renderer = new PdfRenderer(fileDescriptor);

        // Getting total pages count.
        final int pageCount = renderer.getPageCount();

        // Iterating pages
        for (int i = 0; i < pageCount; i++) {

            // Getting Page object by opening page.
            PdfRenderer.Page page = renderer.openPage(i);
            int x;
            int y;
            Bitmap bitmap;
            try {
                x = page.getWidth();
                y = page.getHeight();
                bitmap = Bitmap.createBitmap(x, y,Bitmap.Config.ARGB_8888);

            }catch (OutOfMemoryError e){
                x = page.getWidth() / 2;
                y = page.getHeight() / 2;
                bitmap = Bitmap.createBitmap(x, y,Bitmap.Config.ARGB_8888);
            }

//            int size = Math.min(page.getWidth(), page.getHeight());
//            int x = (page.getWidth() - size) / 2;
//            int y = (page.getHeight() - size) / 2;

            // Creating empty bitmap. Bitmap.Config can be changed.
//            bitmap = Bitmap.createBitmap(x, y,Bitmap.Config.ARGB_8888);

            // Creating Canvas from bitmap.
            Canvas canvas = new Canvas(bitmap);

            // Set White background color.
            canvas.drawColor(Color.WHITE);

            // Draw bitmap.
            canvas.drawBitmap(bitmap, 0, 0, null);

            // Rednder bitmap and can change mode too.
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            // closing page
            page.close();

            // saving image into sdcard.
            File file = new File(DestinationFolder.getAbsolutePath(), "image"+i + ".png");

            // check if file already exists, then delete it.
            if (file.exists()) file.delete();

            // Saving image in PNG format with 100% quality.
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                Log.v("Saved Image - ", file.getAbsolutePath());
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}