package com.example.uetplus2.ui.components.PDFView;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.uetplus2.MainActivity;
import com.example.uetplus2.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class PDF extends Fragment {

    public String url;
    private Context mContext;
    View root;
    PDFView scorePDF;
    Button btn;

    private static final int REQUEST = 112;

    public PDF(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_p_d, container, false);
        mContext = root.getContext();

        scorePDF = (PDFView) root.findViewById(R.id.pdfView);
        btn = (Button) root.findViewById(R.id.btn_viewPDF);
        DownloadPDFfromURL();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPDF();
            }
        });


        return root;
    }

    private void DownloadPDFfromURL() {
        new DownloadPDF(root.getContext())
                .execute("http://112.137.129.30/viewgrade/"+url, "downPDF.pdf");

    }

    public void ViewPDF() {
        File pdfFile = new File(root.getContext().getExternalCacheDir().toString() + "/PDFDOWNLOAD/" + "downPDF.pdf");
        Log.d("notify: ", pdfFile.getPath());

        Uri path = Uri.fromFile(pdfFile);

        scorePDF.fromFile(pdfFile).load();

    }

}