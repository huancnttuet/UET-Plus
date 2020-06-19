package com.example.uethub.ui.components.PDFView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;


public class DownloadPDF extends AsyncTask<String, Void, Void> {

    Context ctx;
    protected ProgressDialog dialog;

    public DownloadPDF(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.dialog = new ProgressDialog(ctx, 3);
        this.dialog.setMessage("Đang tải");
        this.dialog.show();
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
        FileDownLoader.downloadFile(fileUrl, pdfFile,ctx,dialog);

        return null;
    }

}