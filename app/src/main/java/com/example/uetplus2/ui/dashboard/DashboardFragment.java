package com.example.uetplus2.ui.dashboard;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.uetplus2.R;
import com.example.uetplus2.api.UetSupportApi;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.IndicatorView.draw.controller.DrawController;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    SliderView sliderView;
    private SliderAdapterExample adapter;
    LinearLayout snLayout ;
    LinearLayout schedule_btn, examtime_btn, grades_btn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        sliderView = root.findViewById(R.id.imageSlider);
        snLayout = root.findViewById(R.id.student_news);
        adapter = new SliderAdapterExample(getContext());

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        sliderView.setOnIndicatorClickListener(new DrawController.ClickListener() {
            @Override
            public void onIndicatorClicked(int position) {
                sliderView.setCurrentPagePosition(position);
                Log.v("sl ","clcik");
            }
        });

        schedule_btn = root.findViewById(R.id.schedule_btn);
        schedule_btn.setClickable(true);
        examtime_btn = root.findViewById(R.id.examtime_btn);
        examtime_btn.setClickable(true);
        grades_btn = root.findViewById(R.id.grades_btn);
        grades_btn.setClickable(true);

        new GetNews(root.getContext()).execute("/getdashboard");
        new GetStudentNews(root.getContext()).execute("/getstudentnews");
        return root;
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

    public void renewItems(View view) {
        List<SliderItem> sliderItemList = new ArrayList<>();
        //dummy data
        for (int i = 0; i < 5; i++) {
            SliderItem sliderItem = new SliderItem();
            sliderItem.setDescription("Slider Item " + i);
            if (i % 2 == 0) {
                sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
            } else {
                sliderItem.setImageUrl("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260");
            }
            sliderItemList.add(sliderItem);
        }
        adapter.renewItems(sliderItemList);
    }

    public void removeLastItem(View view) {
        if (adapter.getCount() - 1 >= 0)
            adapter.deleteItem(adapter.getCount() - 1);
    }

    public void addNewItem(View view) {
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription("Slider Item Added Manually");
        sliderItem.setImageUrl("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260");
        adapter.addItem(sliderItem);
    }

    public void addUrlImage(String url, String description){
        SliderItem sliderItem = new SliderItem();
        sliderItem.setDescription(description);
        sliderItem.setImageUrl(url);
        adapter.addItem(sliderItem);
    }

    private class GetNews extends AsyncTask<String, Void, List<List<String>>> {

        protected ProgressDialog dialog;
        protected Context context;

        public GetNews(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving News List");
            this.dialog.show();
        }

        @Override
        protected List<List<String>> doInBackground(String... params) {
            try {
                Log.v("News", "Get newsfeed");
                return (List<List<String>>) UetSupportApi.getNews((String) params[0]);
            }
            catch (Exception e) {
                Log.v("News", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<List<String>> result) {
            super.onPostExecute(result);

            for (List<String> r: result) {
                addUrlImage(r.get(1), r.get(0));
            }

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private class GetStudentNews extends AsyncTask<String, Void, List<List<String>>> {

        protected ProgressDialog dialog;
        protected Context context;

        public GetStudentNews(Context context)
        {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Student News List");
            this.dialog.show();
        }

        @Override
        protected List<List<String>> doInBackground(String... params) {
            try {
                Log.v("News", "Get student news");
                return (List<List<String>>) UetSupportApi.getNews((String) params[0]);
            }
            catch (Exception e) {
                Log.v("News", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<List<String>> result) {
            super.onPostExecute(result);

            loadStudentNewsCardView(result);

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    public void loadStudentNewsCardView(List<List<String>> studentNewsList){
        int width = (int) getResources().getDimension(R.dimen.cardview_width);
        int height = (int) getResources().getDimension(R.dimen.cardview_width);
        int marginSize = (int) getResources().getDimension(R.dimen.margin_cardview);
        int heightImage = (int) getResources().getDimension(R.dimen.height_image);
        LinearLayout.LayoutParams param_default = new LinearLayout.LayoutParams(width,height);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(width, height);
        LinearLayout.LayoutParams param_image = new LinearLayout.LayoutParams(width,2*height/3);
        LinearLayout.LayoutParams param_text = new LinearLayout.LayoutParams(width, height);
        param_default.setMargins(marginSize,marginSize,marginSize,marginSize);

        for (List<String> sn: studentNewsList
             ) {
            CardView cardView = new CardView(getContext());
            cardView.setLayoutParams(param_default);
            cardView.setRadius(12f);
            snLayout.addView(cardView);
            LinearLayout lnLayout = new LinearLayout(getContext());
            lnLayout.setLayoutParams(param);
            lnLayout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(param_image);
            imageView.setImageResource(R.drawable.puma_offer);
            new DownloadImageTask(imageView).execute(sn.get(1));
            cardView.addView(imageView);

            TextView textView = new TextView(getContext());

            textView.setLayoutParams(param_text);
            textView.setTextSize(19f);
            textView.setText(sn.get(0));
            textView.setGravity(Gravity.BOTTOM);
            textView.setPadding(marginSize,marginSize,marginSize,marginSize);
            cardView.addView(textView);
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
