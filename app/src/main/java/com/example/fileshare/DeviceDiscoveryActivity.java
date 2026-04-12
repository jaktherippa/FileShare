package com.example.fileshare;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceDiscoveryActivity extends AppCompatActivity {

    private BluetoothManager bluetoothManager;
    private List<BluetoothDevice> deviceList = new ArrayList<>();
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.device_discovery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bluetoothManager = new BluetoothManager(this);
        RecyclerView recyclerView = findViewById(R.id.devices_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(deviceList, device -> {
            Intent intent = new Intent(this, TransferActivity.class);
            intent.putExtra("device_name", device.getName());
            intent.putStringArrayListExtra("selected_files", getIntent().getStringArrayListExtra("selected_files"));
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.refresh_btn).setOnClickListener(v -> refreshDevices());
        refreshDevices();
    }

    private void refreshDevices() {
        if (!bluetoothManager.isBluetoothEnabled()) {
            Toast.makeText(this, "Please enable Bluetooth", Toast.LENGTH_SHORT).show();
            return;
        }
        Set<BluetoothDevice> bondedDevices = bluetoothManager.getBondedDevices();
        if (bondedDevices != null) {
            deviceList.clear();
            deviceList.addAll(bondedDevices);
            adapter.notifyDataSetChanged();
        }
    }

    static class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.ViewHolder> {
        private final List<BluetoothDevice> devices;
        private final OnDeviceClickListener listener;

        interface OnDeviceClickListener {
            void onDeviceClick(BluetoothDevice device);
        }

        DeviceAdapter(List<BluetoothDevice> devices, OnDeviceClickListener listener) {
            this.devices = devices;
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
            return new ViewHolder(view);
        }

        @SuppressLint("MissingPermission")
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            BluetoothDevice device = devices.get(position);
            holder.name.setText(device.getName() != null ? device.getName() : "Unknown Device");
            holder.address.setText(device.getAddress());
            holder.itemView.setOnClickListener(v -> listener.onDeviceClick(device));
        }

        @Override
        public int getItemCount() {
            return devices.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, address;
            ViewHolder(View view) {
                super(view);
                name = view.findViewById(R.id.device_name);
                address = view.findViewById(R.id.device_address);
            }
        }
    }
}