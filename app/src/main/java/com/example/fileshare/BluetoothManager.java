package com.example.fileshare;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;

import java.util.Set;

public class BluetoothManager {
    private final Activity activity;
    private final BluetoothAdapter bluetoothAdapter;

    public BluetoothManager(Activity activity) {
        this.activity = activity;
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean isBluetoothSupported() {
        return bluetoothAdapter != null;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    @SuppressLint("MissingPermission")
    public void enableBluetooth(ActivityResultLauncher<Intent> launcher) {
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Bluetooth not supported", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            launcher.launch(enableBtIntent);
        }
    }

    @SuppressLint("MissingPermission")
    public void disableBluetooth() {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            Toast.makeText(activity, "Bluetooth turned off", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean hasBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED &&
                   ActivityCompat.checkSelfPermission(activity, Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    public Set<BluetoothDevice> getBondedDevices() {
        if (bluetoothAdapter != null && hasBluetoothPermissions()) {
            return bluetoothAdapter.getBondedDevices();
        }
        return null;
    }

    @SuppressLint("MissingPermission")
    public void makeDiscoverable(int duration) {
        if (bluetoothAdapter != null) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
            activity.startActivity(discoverableIntent);
        }
    }

    @SuppressLint("MissingPermission")
    public void startDiscovery() {
        if (bluetoothAdapter != null && hasBluetoothPermissions()) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            bluetoothAdapter.startDiscovery();
        }
    }
}
