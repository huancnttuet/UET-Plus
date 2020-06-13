package com.example.uetplus2.ui.components.PDFView;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class FileDownLoader  {

    private  static final int MEGABYTE = 1024*1024;

    public static void downloadFile(String fileUrl, File directory){
        try {
            // URL Connection
            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            Log.d("url: ", fileUrl);


            //input stream
            InputStream inputStream = urlConnection.getInputStream();

            //output stream
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();

            //Byte array
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;

            //writing array of bytes

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }

            fileOutputStream.close();


        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}