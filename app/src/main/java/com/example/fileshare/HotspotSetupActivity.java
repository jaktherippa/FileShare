package com.example.fileshare;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HotspotSetupActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private WifiManager wifiManager;
    private WifiManager.LocalOnlyHotspotReservation hotspotReservation;
    private TextView ssidTextView;
    private TextView passwordTextView;
    private Button startHotspotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.hotspot_setup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        ssidTextView = findViewById(R.id.ssid_value);
        passwordTextView = findViewById(R.id.password_value);
        startHotspotButton = findViewById(R.id.start_hotspot_btn);

        startHotspotButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                Toast.makeText(this, "Hotspot is not supported on this version of Android", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkPermissions()) {
                startHotspot();
            } else {
                requestPermissions();
            }
        });
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.NEARBY_WIFI_DEVICES,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startHotspot();
                }
            } else {
                Toast.makeText(this, "Permissions required for hotspot", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startHotspot() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        wifiManager.startLocalOnlyHotspot(new WifiManager.LocalOnlyHotspotCallback() {
            @Override
            public void onStarted(WifiManager.LocalOnlyHotspotReservation reservation) {
                super.onStarted(reservation);
                hotspotReservation = reservation;
                String ssid = "Unknown";
                String password = "Unknown";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    android.net.wifi.SoftApConfiguration softApConfig = reservation.getSoftApConfiguration();
                    if (softApConfig != null) {
                        ssid = softApConfig.getSsid();
                        password = softApConfig.getPassphrase();
                    }
                } else {
                    WifiConfiguration wifiConfig = reservation.getWifiConfiguration();
                    if (wifiConfig != null) {
                        ssid = wifiConfig.SSID;
                        password = wifiConfig.preSharedKey;
                    }
                }
                // SSID often comes with quotes, let's remove them for display
                if (ssid != null && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    ssid = ssid.substring(1, ssid.length() - 1);
                }
                ssidTextView.setText(ssid);
                passwordTextView.setText(password);
                startHotspotButton.setText(getString(R.string.hotspot_active));
                startHotspotButton.setEnabled(false);
                Toast.makeText(HotspotSetupActivity.this, "Hotspot started", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopped() {
                super.onStopped();
                hotspotReservation = null;
                ssidTextView.setText("");
                passwordTextView.setText("");
                startHotspotButton.setText(R.string.start_hotspot);
                startHotspotButton.setEnabled(true);
                Toast.makeText(HotspotSetupActivity.this, "Hotspot stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(int reason) {
                super.onFailed(reason);
                Toast.makeText(HotspotSetupActivity.this, "Hotspot failed: " + reason, Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hotspotReservation != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hotspotReservation.close();
            }
        }
    }
}