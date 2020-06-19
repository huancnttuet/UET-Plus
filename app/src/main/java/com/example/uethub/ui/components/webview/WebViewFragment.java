package com.example.uethub.ui.components.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.permission.PermissionCheck;

import im.delight.android.webview.AdvancedWebView;

public class WebViewFragment extends Fragment implements AdvancedWebView.Listener {

    private AdvancedWebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = (AdvancedWebView) rootView.findViewById(R.id.webview);
        mWebView.setListener(getActivity(),this);
        String url = getArguments().getString("url");
        mWebView.loadUrl(url);

        // ...

        return rootView;
    }

    @SuppressLint("NewApi")
    @Override
    public void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }

    @SuppressLint("NewApi")
    @Override
    public void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mWebView.onDestroy();
        // ...
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);
        // ...
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) { }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        if(PermissionCheck.readAndWriteExternalStorage(getContext())){
            if (AdvancedWebView.handleDownload(getContext(), url, suggestedFilename)) {
                // download successfully handled
                Toast.makeText(getContext(), "Download Success!", Toast.LENGTH_LONG).show();
            } else {
                // download couldn't be handled because user has disabled download manager app on the device
                Toast.makeText(getContext(), "Download Failed!", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onExternalPageRequest(String url) { }

}