package com.pondokit.navigationdrawer.ui.menu.menu_saran_dan_kritikan;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pondokit.navigationdrawer.R;
import com.pondokit.navigationdrawer.ui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaranDanKritikanFragment extends Fragment {


    public SaranDanKritikanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Saran dan Kritikan");
        return inflater.inflate(R.layout.fragment_saran_dan_kritikan, container, false);
    }

}
