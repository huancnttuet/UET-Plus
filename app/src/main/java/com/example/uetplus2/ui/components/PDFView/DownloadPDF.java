package com.example.uetplus2.ui.components.PDFView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.uetplus2.models.GradesModel;

import java.io.File;
import java.io.IOException;


public class DownloadPDF extends AsyncTask<String, Void, Void> {

    Context ctx;

    public DownloadPDF(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected Void doInBackground(String... strings) {

        // Creating the name of files and urls
        String fileUrl = strings[0];
        String fileName = strings[1];

        //Getting reference for external storage
        String extStorageDirectory = ctx.getExternalCacheDir().toString();
        Log.d("file Dir: ", extStorageDirectory);


        // File location
        File folder = new File(extStorageDirectory , "PDFDOWNLOAD");
        folder.mkdirs();

        File pdfFile = new File(folder + "/" +  fileName);

        try {

            if (!pdfFile.exists()) {
                pdfFile.createNewFile();
            } else {
                Log.d("File: ", "tồn tại file rồi");
                pdfFile.delete();
                pdfFile.createNewFile();
            }
        } catch (IOException e) {
            Log.d("notify ", "không tạo được file");
            e.printStackTrace();
        }


        //File downloader class
        FileDownLoader.downloadFile(fileUrl, pdfFile);

        return null;
    }

}