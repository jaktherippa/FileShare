package com.example.fileshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
//    Declare UI components
    Button send_files,receive_files, hotspot,bluetooth,home,history, settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//Match Declared components with xml component id
        send_files = findViewById(R.id.send_files_button);
        receive_files = findViewById(R.id.receive_files_button);
        hotspot = findViewById(R.id.hotspot_button);
        bluetooth = findViewById(R.id.bluetooth_button);
        home = findViewById(R.id.home_button);
        history = findViewById(R.id.history_button);
        settings = findViewById(R.id.settings_button);

 send_files.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
         boolean connection_type;
         Toast.makeText(MainActivity.this, "Bluetooth Created", Toast.LENGTH_SHORT).show();
         Intent connectionSetup = new Intent(MainActivity.this,ConnectionSetupActivity.class);
         startActivity(connectionSetup);
         finish();
     }
 });

    }
}