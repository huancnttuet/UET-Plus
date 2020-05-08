package com.uetplus.ui.menu.news;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;


import com.uetplus.R;

import com.uetplus.ui.MainActivity;

import com.uetplus.ui.services.I_LoginApi;
import com.uetplus.ui.services.LoginApi;

import java.io.InputStream;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Tin tức");
        final View view =  inflater.inflate(R.layout.fragment_news, container, false);

        I_LoginApi service = LoginApi.getRetrofitInstance().create(I_LoginApi.class);
        Call call = service.getNewsfeed();
        call.enqueue(new Callback<List<List<String>>>() {
            @Override
            public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                List<List<String>> data = response.body();
                draw(view,data);

            }

            @Override
            public void onFailure(Call<List<List<String>>> call, Throwable t) {
                view.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Log.v("Call API", "Fail");
            }
        });


        return view;
    }


    public void draw(View view, List<List<String>> newsfeed){
        LinearLayout coursesView = view.findViewById(R.id.news_list);
        int width = (int) getResources().getDimension(R.dimen.newsfeed_card_view_width);
        int height = (int) getResources().getDimension(R.dimen.newsfeed_card_view_height);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,height);
        LinearLayout.LayoutParams paramWrapper = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        param.setMargins(20,10,20,10);
        for(int i = 0; i < newsfeed.size(); i++){
            List<String> ele = newsfeed.get(i);
            final String title = ele.get(0);
            String imageUrl = ele.get(1);
            final String link = ele.get(2);
            CardView cardView = new CardView(view.getContext());
            cardView.setLayoutParams(param);
            cardView.setRadius(9);
            LinearLayout wrapper = new LinearLayout(view.getContext());
            wrapper.setLayoutParams(paramWrapper);
            wrapper.setGravity(Gravity.CENTER);
            wrapper.setOrientation(LinearLayout.HORIZONTAL);

            CardView cardViewCode = new CardView(view.getContext());
            LinearLayout.LayoutParams paramCard = new LinearLayout.LayoutParams(width,height);
            cardViewCode.setLayoutParams(paramCard);
            cardViewCode.setRadius(9);
//            cardViewCode.setCardBackgroundColor(Color.parseColor("#FF5555"));

            TextView nameText = new TextView(view.getContext());
            nameText.setText(title);
            nameText.setTextSize(TypedValue.COMPLEX_UNIT_DIP,19);
            nameText.setGravity(Gravity.CENTER);

            ImageView img = new ImageView(view.getContext());

            new DownloadImageTask(img).execute(imageUrl);

            wrapper.addView(cardViewCode);
            wrapper.addView(nameText);

            cardViewCode.addView(img);
            cardView.addView(wrapper);
            coursesView.addView(cardView);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Fragment fragment = new NewsDetailFragment();
                    Bundle arguments = new Bundle();
                    arguments.putString( "link" , link);
                    arguments.putString("title", title);
                    fragment.setArguments(arguments);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter,R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                            .replace(R.id.content_frame, fragment)
                            .addToBackStack("news")
                            .commit();

                }
            });
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
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
