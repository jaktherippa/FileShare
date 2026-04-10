package com.example.fileshare;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
public class BluetoothManager{
    private ActivityResultLauncher<Intent> enableBluetoothLauncher = new ActivityResultContracts.StartActivityForResult(), result -> {
        if(result.getResultCode() == Activity.RESULT_OK){
            //Bluetooth was turned on successfully
        }else{
            //Bluetooth Not Supported
        }
    }
    public BluetoothManager(ConnectionSetupActivity activity){
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null){
            System.out.println("Bluetooth Not Supported");
        }
        if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
            Intent bluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        }
    }
}
