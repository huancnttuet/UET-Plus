package com.example.uet_schedule.View;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
//import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uet_schedule.R;

public class PDFViewerScreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pdf_viewer_screen);
        WebView webView=findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        //---you need this to prevent the webview from
        // launching another browser when a url
        // redirection occurs---
//        webView.setWebViewClient(new AppWebViewClients(findViewById(R.id.loadingPanel)));
        webView.setWebViewClient(new AppWebViewClients());

        String urlPdf = getIntent().getStringExtra("urlPdf");
        Log.v("dasd",urlPdf);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + urlPdf);
//        setContentView(webView);

    }


    public class AppWebViewClients extends WebViewClient {
//        private ProgressBar progressBar;

//        public AppWebViewClients(ProgressBar progressBar) {
//            this.progressBar=progressBar;
//            progressBar.setVisibility(View.VISIBLE);
//        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
//            progressBar.setVisibility(View.GONE);
        }
    }
}
