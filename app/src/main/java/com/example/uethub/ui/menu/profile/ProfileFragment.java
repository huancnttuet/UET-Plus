package com.example.uethub.ui.menu.profile;

import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.uethub.MainActivity;
import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.models.Information;
import com.example.uethub.services.extensions.DownloadCircleImageTask;
import com.example.uethub.ui.Base;
import com.example.uethub.ui.activity.LoginActivity;
import com.example.uethub.ui.components.grades.GradesFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Base {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle("Thông tin cá nhân");

        String te = SaveSharedPreference.getPrefData(getContext());

        Gson gson = new Gson();
        Type listType = new TypeToken<Information>() {}.getType();
        Information inforData = gson.fromJson(te,listType);


        new DownloadCircleImageTask((CircleImageView) root.findViewById(R.id.avatar))
                .execute(inforData.avatar);

        LinearLayout infomationLayout = root.findViewById(R.id.infomation);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        for (List<String>ele: inforData.information
             ) {
            TextView tv = new TextView(getContext());
            tv.setText(ele.get(0) + "  :  " +ele.get(1));
            tv.setLayoutParams(params);
            tv.setTextSize(14);
            infomationLayout.addView(tv);
        }

        Button logout_btn = root.findViewById(R.id.logout_btn);
        Button setting_btn = root.findViewById(R.id.settings_btn);

        setting_btn.setOnClickListener(v -> {
            Fragment fragment = new SettingFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack("SETTING_TAG");
            transaction.commit();
        });

        logout_btn.setOnClickListener(v -> dialogExit());

        return  root;
    }

    private void dialogExit() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("Bạn muốn đăng xuất ?");
        dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SaveSharedPreference.cleanCache(getContext());
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
            }
        });
        dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }


}
