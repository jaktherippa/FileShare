package com.example.fileshare;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class TransferActivity extends AppCompatActivity {

    private LinearProgressIndicator progressIndicator;
    private TextView progressText;
    private int progress = 0;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ProjectDB db;
    private String fileName = "Unknown File";
    private String deviceName = "Unknown Device";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.transfer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressIndicator = findViewById(R.id.transfer_progress);
        progressText = findViewById(R.id.progress_text);
        TextView fileNameText = findViewById(R.id.file_name);
        db = new ProjectDB(this);

        deviceName = getIntent().getStringExtra("device_name");
        ArrayList<String> selectedFiles = getIntent().getStringArrayListExtra("selected_files");

        if (deviceName != null) {
            ((TextView) findViewById(R.id.device_info)).setText("Sending to " + deviceName);
        }

        if (selectedFiles != null && !selectedFiles.isEmpty()) {
            fileName = selectedFiles.get(0);
            fileNameText.setText(fileName);
        }

        findViewById(R.id.cancel_btn).setOnClickListener(v -> finish());

        simulateTransfer();
    }

    private void simulateTransfer() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (progress < 100) {
                    progress += 5;
                    progressIndicator.setProgress(progress);
                    progressText.setText(progress + "%");
                    handler.postDelayed(this, 200);
                } else {
                    String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                    db.addHistory(fileName, "Sent to " + deviceName, date);
                    Toast.makeText(TransferActivity.this, "Transfer Complete", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }, 500);
    }
}