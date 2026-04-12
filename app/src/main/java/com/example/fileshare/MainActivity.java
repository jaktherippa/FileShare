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

        findViewById(R.id.send_files_button).setOnClickListener(v -> {
            startActivity(new Intent(this, FileSelectionActivity.class));
        });

        findViewById(R.id.receive_files_button).setOnClickListener(v -> {
            startActivity(new Intent(this, ConnectionSetupActivity.class));
        });

        findViewById(R.id.history_button).setOnClickListener(v -> {
            startActivity(new Intent(this, TransferHistoryActivity.class));
        });

        findViewById(R.id.settings_button).setOnClickListener(v -> {
            startActivity(new Intent(this, SettingsActivity.class));
        });
    }
}