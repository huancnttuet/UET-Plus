package com.uetplus.ui.menu_profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uetplus.R;
import com.uetplus.ui.SaveSharedPreference;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Hồ sơ");


        String te = SaveSharedPreference.getPrefData(this);
        Log.v("dasdas","te");
        Log.v("dasdas",te);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<List<List<String>>>>() {}.getType();
        List<List<List<String>>> list = gson.fromJson(te,listType);
        Log.v("daad", String.valueOf(list.size()));
        List<List<String>> info = list.get(0);
        List<String> urlImage = list.get(2).get(0);

        new DownloadImageTask((CircleImageView) findViewById(R.id.avatar))
                .execute(urlImage.get(0));

        LinearLayout infomation = findViewById(R.id.infomation);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        for(int i = 0; i < info.size();i++){
            TextView tv = new TextView(this);
            tv.setText(info.get(i).get(0) + "  :  " +info.get(i).get(1));
            tv.setLayoutParams(params);
            tv.setTextSize(14);
            infomation.addView(tv);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        CircleImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

