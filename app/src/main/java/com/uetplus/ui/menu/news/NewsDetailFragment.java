package com.uetplus.ui.menu.news;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.uetplus.R;

import com.uetplus.ui.MainActivity;
import com.uetplus.ui.services.I_LoginApi;
import com.uetplus.ui.services.LoginApi;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailFragment extends Fragment implements Html.ImageGetter {
    private TextView txtDescription;
    private Drawable empty;

    public NewsDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle arguments = getArguments();
        String title = arguments.getString("title");
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);
        final View view =  inflater.inflate(R.layout.fragment_news_detail, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        TextView toolbarText = view.findViewById(R.id.title);
        toolbarText.setText(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        String link = arguments.getString("link");

        txtDescription = view.findViewById(R.id.content);
        I_LoginApi service = LoginApi.getRetrofitInstance().create(I_LoginApi.class);
        Call call = service.getFromUrl(link);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String>call, Response<String> response) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                String data = response.body();
                Spannable html;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    html = (Spannable) Html.fromHtml(data, Html.FROM_HTML_MODE_LEGACY, NewsDetailFragment.this, null);
                } else {
                    html = (Spannable) Html.fromHtml(data, NewsDetailFragment.this, null);
                }
                txtDescription.setText(html);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Log.v("Call API", "Fail");

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public Drawable getDrawable(String s) {
        LevelListDrawable d = new LevelListDrawable();
        empty = getResources().getDrawable(R.drawable.uet_icon2);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
        new LoadImage().execute(s, d);
        return d;
    }

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {
        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d("TAG", "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                //mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());
                mDrawable.setLevel(1);
                CharSequence t = txtDescription.getText();
                txtDescription.setText(t);
            }
        }
    }


}
