package com.example.fileshare;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConnectionSetupActivity extends AppCompatActivity {
    private Button turnOn, turnOff;
    private BluetoothManager bluetoothManager;
    private final ActivityResultLauncher<Intent> bluetoothLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Toast.makeText(this, "Bluetooth turned on", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Bluetooth request denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    private final ActivityResultLauncher<String[]> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestMultiplePermissions(),
            result -> {
                boolean allGranted = true;
                for (String permission : result.keySet()) {
                    Boolean granted = result.get(permission);
                    if (granted == null || !granted) {
                        allGranted = false;
                        break;
                    }
                }
                if (allGranted) {
                    bluetoothManager.enableBluetooth(bluetoothLauncher);
                } else {
                    Toast.makeText(this, "Bluetooth permissions denied", Toast.LENGTH_SHORT).show();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.connection_setup);

        bluetoothManager = new BluetoothManager(this);
        turnOn = findViewById(R.id.turn_bluetooth_on);
        turnOff = findViewById(R.id.turn_bluetooth_off);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bluetoothManager.isBluetoothSupported()) {
                    Toast.makeText(ConnectionSetupActivity.this, "Bluetooth is not supported", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    if (!bluetoothManager.hasBluetoothPermissions()) {
                        permissionLauncher.launch(new String[]{
                                Manifest.permission.BLUETOOTH_CONNECT,
                                Manifest.permission.BLUETOOTH_SCAN
                        });
                        return;
                    }
                }

                if (!bluetoothManager.isBluetoothEnabled()) {
                    bluetoothManager.enableBluetooth(bluetoothLauncher);
                } else {
                    Toast.makeText(ConnectionSetupActivity.this, "Bluetooth is already on", Toast.LENGTH_SHORT).show();
                }
            }
        });

        turnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothManager.isBluetoothEnabled()) {
                    bluetoothManager.disableBluetooth();
                    Toast.makeText(ConnectionSetupActivity.this, "Bluetooth turned off", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ConnectionSetupActivity.this, "Bluetooth is already off", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
