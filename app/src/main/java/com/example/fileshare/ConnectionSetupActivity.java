package com.example.fileshare;

import static java.lang.System.out;
import android.bluetooth.BluetoothAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConnectionSetupActivity extends AppCompatActivity {
//Bluetooth Launcher
    Button turnOn = findViewById(R.id.turn_bluetooth_on);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.connection_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        turnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BluetoothManager(ConnectionSetupActivity.this);
            }
        });
    }
}