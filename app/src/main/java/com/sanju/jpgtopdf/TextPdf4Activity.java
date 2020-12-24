package com.sanju.jpgtopdf;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TextPdf4Activity extends AppCompatActivity {

    EditText edit2,edit1,name,phone;
    Spinner spinner1,spinner2;
    Button createBtn;

    Bitmap bitmap, scaleBitmap;
    int pageWeight = 1200;
    Date dateObj;
    DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_pdf4);

        edit2 = findViewById(R.id.edit2);
        edit1 = findViewById(R.id.edit1);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        createBtn = findViewById(R.id.btn);

        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.new3);
        scaleBitmap = Bitmap.createScaledBitmap(bitmap,100,100,false);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        createPDF();

    }

    private void createPDF() {
        createBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                dateObj = new Date();

                if(name.getText().toString().length() == 0 ||
                phone.getText().toString().length() == 0 ||
                edit1.getText().toString().length() == 0 ||
                edit2.getText().toString().length() == 0 ){
                    Toast.makeText(TextPdf4Activity.this,"Fields are empty",Toast.LENGTH_LONG).show();
                }

                PdfDocument myPDFDocument = new PdfDocument();
                Paint myPaint = new Paint();
                Paint titlePaint = new Paint();

                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250,400,1).create();
                PdfDocument.Page myPage = myPDFDocument.startPage(myPageInfo);
                Canvas canvas = myPage.getCanvas();

                canvas.drawBitmap(scaleBitmap,0,0,myPaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                titlePaint.setTextSize(70);
                canvas.drawText("Diamond Pizza", pageWeight/2, 270, titlePaint);

                myPaint.setColor(Color.rgb(0,113,188));
                myPaint.setTextSize(30f);
                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("Call: 022-414212313",1160,40, myPaint);
                canvas.drawText("022-121313414", 1160,80, myPaint);

                titlePaint.setTextAlign(Paint.Align.CENTER);
                titlePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.ITALIC));
                titlePaint.setTextSize(70);
                canvas.drawText("Invoice", pageWeight/2, 500, titlePaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setTextSize(35f);
                myPaint.setColor(Color.BLACK);
                canvas.drawText("Customer Name: "+name.getText(),20,590, myPaint);
                canvas.drawText("Contact No.: "+phone.getText(),20,640, myPaint);

                myPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText("Invoice No.:" +"223223", pageWeight-20, 590, myPaint);

                dateFormat = new SimpleDateFormat("dd/mm/yy");
                canvas.drawText("Date: "+dateFormat.format(dateObj), pageWeight-20,640, myPaint);

                dateFormat = new SimpleDateFormat("HH:mm:ss");
                canvas.drawText("Time: "+dateFormat.format(dateObj), pageWeight-20,640, myPaint);

                myPaint.setStyle(Paint.Style.STROKE);
                myPaint.setStrokeWidth(2);
                canvas.drawRect(20,780,pageWeight-20,860,myPaint);

                myPaint.setTextAlign(Paint.Align.LEFT);
                myPaint.setStyle(Paint.Style.FILL);
                canvas.drawText("Sl. No. ",40,830, myPaint);
                canvas.drawText("Item Description ",200,830, myPaint);
                canvas.drawText("Price ",700,830, myPaint);
                canvas.drawText("Qty ",900,830, myPaint);
                canvas.drawText("Total ",1030,830, myPaint);

                canvas.drawLine(180,790,180,840, myPaint);
                canvas.drawLine(680,790,680,840, myPaint);
                canvas.drawLine(880,790,680,840, myPaint);
                canvas.drawLine(1030,790,1030,840, myPaint);

                myPDFDocument.finishPage(myPage);

                File file = new File(Environment.getExternalStorageDirectory(),"/FirstPDF.pdf");

                try {
                    myPDFDocument.writeTo(new FileOutputStream(file));
                }catch (IOException e){
                    e.printStackTrace();
                }
                myPDFDocument.close();

            }
        });
    }

}