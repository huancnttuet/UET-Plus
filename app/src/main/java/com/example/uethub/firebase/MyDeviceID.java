package com.example.uethub.firebase;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.uethub.R;
import com.example.uethub.cache.SaveSharedPreference;
import com.example.uethub.extensions.Device;
import com.example.uethub.models.DeviceModel;
import com.example.uethub.services.device.DeleteMyDevice;
import com.example.uethub.services.device.InsertMyDevice;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Objects;

public class MyDeviceID {

    public static void sendToken(View view){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.i("FCM Token", token);
                Log.d("FCM Token", "My phonename: " + Device.getDeviceName() );
                DeviceModel myDevice = new DeviceModel();
                myDevice.device_id = token;
                myDevice.device_name = Device.getDeviceName();
                new InsertMyDevice(output -> {
                    if(output != null){
                        SaveSharedPreference.setCache(view.getContext(),"setting_notify", "1");
                        Toast.makeText(view.getContext(), "sent MyDeviceCode",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(view.getContext(), "My device invalid",
                                Toast.LENGTH_LONG).show();
                        Switch switch1 =view.findViewById(R.id.switch_notify);
                        switch1.setChecked(false);
                    }
                }).execute(myDevice);
            }
        });
    }
    public static void deleteMyDeviceID(View view){
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) view.getContext(), new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                Log.i("FCM Token", token);
                Log.d("FCM Token", "My phonename: " + Device.getDeviceName() );
                DeviceModel myDevice = new DeviceModel();
                myDevice.device_id = token;
                myDevice.device_name = Device.getDeviceName();
                new DeleteMyDevice(output -> {
                    if(output != null){
                        SaveSharedPreference.setCache(view.getContext(),"setting_notify", "0");
                        Toast.makeText(view.getContext(), "deleted MyDeviceCode",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(view.getContext(), "My device code not delete",
                                Toast.LENGTH_LONG).show();
                        Switch switch1 =view.findViewById(R.id.switch_notify);
                        switch1.setChecked(true);
                    }
                }).execute(myDevice);
            }
        });
    }



}
