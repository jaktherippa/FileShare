package com.example.fileshare;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.widget.Button;

public class BluetoothManager{
    public BluetoothManager(ConnectionSetupActivity activity){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            System.out.println("Bluetooth Not Supported");
        }
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(activity,bluetooth,0, " ");
        }
    }
}
