package com.example.fileshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class FileSelectionActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetMultipleContents(),
            uris -> {
                if (uris != null && !uris.isEmpty()) {
                    ArrayList<String> uriStrings = new ArrayList<>();
                    for (Uri uri : uris) {
                        uriStrings.add(uri.toString());
                    }
                    Intent intent = new Intent(this, DeviceDiscoveryActivity.class);
                    intent.putStringArrayListExtra("selected_files", uriStrings);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No files selected", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // We don't really need a layout if we just launch the picker immediately,
        // but for a better UX we could have a "Select Files" button.
        setContentView(R.layout.file_selection);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        findViewById(R.id.select_files_btn).setOnClickListener(v -> {
            filePickerLauncher.launch("*/*");
        });
    }
}