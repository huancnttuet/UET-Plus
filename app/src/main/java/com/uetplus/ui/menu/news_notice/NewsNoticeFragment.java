package com.uetplus.ui.menu.news_notice;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
public class NewsNoticeFragment extends Fragment implements Html.ImageGetter {
    private TextView txtDescription;
    private Drawable empty;

    public NewsNoticeFragment() {
        // Required empty public constructor
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_news_detail, container, false);

        Bundle arguments = getArguments();
        String title;
        String link ;
        if(arguments == null){
            title = "Thông báo";
            link = "https://uet.vnu.edu.vn/category/tin-tuc/tin-sinh-vien/";
        }else {
            title = arguments.getString("title");
            link = arguments.getString("link");
        }
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(title);

        txtDescription = view.findViewById(R.id.content);
        I_LoginApi service = LoginApi.getRetrofitInstance().create(I_LoginApi.class);
        Call call = service.getFromUrl(link);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String>call, Response<String> response) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                String data = response.body();
                setTextViewHTML(txtDescription,data);
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

    protected void makeLinkClickable(SpannableStringBuilder strBuilder, final URLSpan span)
    {
        int start = strBuilder.getSpanStart(span);
        int end = strBuilder.getSpanEnd(span);
        int flags = strBuilder.getSpanFlags(span);
        ClickableSpan clickable = new ClickableSpan() {
            public void onClick(View view) {
                // Do something with span.getURL() to handle the link click...
                Log.v("Click",span.getURL());
                String link = span.getURL();
                Fragment fragment = new NewsNoticeFragment();
                Bundle arguments = new Bundle();
                arguments.putString( "link" , link);
                arguments.putString("title", link);
                fragment.setArguments(arguments);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter,R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                        .replace(R.id.content_frame, fragment)
                        .addToBackStack("news")
                        .commit();
            }
        };
        strBuilder.setSpan(clickable, start, end, flags);
        strBuilder.removeSpan(span);
    }

    protected void setTextViewHTML(TextView text, String html)
    {
        CharSequence sequence = Html.fromHtml(html);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            sequence = (CharSequence) Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY, NewsNoticeFragment.this, null);
        } else {
            sequence = (CharSequence) Html.fromHtml(html, NewsNoticeFragment.this, null);
        }
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        URLSpan[] urls = strBuilder.getSpans(0, sequence.length(), URLSpan.class);
        for(URLSpan span : urls) {
            makeLinkClickable(strBuilder, span);
        }
        text.setText(strBuilder);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
