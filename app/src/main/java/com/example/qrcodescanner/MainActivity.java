package com.example.qrcodescanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.example.qrcodescanner.databinding.ActivityMainBinding;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    boolean allow = false;
    private CodeScanner codeScanner;
    List<String> itemsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        int PERMISSION_CODE = 1;
        String[] permission = {Manifest.permission.CAMERA,};

        if (!hasPermission(this, permission))
            ActivityCompat.requestPermissions(this, permission, PERMISSION_CODE);
        else {
            codeScanner = new CodeScanner(this, binding.scanner);
            codeScanner.setScanMode(ScanMode.SINGLE);

            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result.getText().contains("1234567890allow")) {
                                allow = true;
                                StringBuilder text = new StringBuilder();
                                List<String> temp = Arrays.asList(result.getText().split("\n"));
                                text.append("Code: ").append(temp.get(0)).append("\n");
                                text.append("Name: ").append(temp.get(1)).append("\n");
                                text.append("MRP: ").append(temp.get(2));
                                binding.itemText.setText(text);
                            } else {
                                Toast.makeText(MainActivity.this, "Please Scan Proper QR Code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            binding.checkCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, Cart.class);
                    intent.putStringArrayListExtra("items", (ArrayList<String>) itemsList);
                    startActivity(intent);
                }
            });

            binding.scanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    codeScanner.startPreview();
                }
            });

            binding.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (allow) {
                        itemsList.add(binding.itemText.getText().toString());
                        codeScanner.startPreview();
                        Toast.makeText(MainActivity.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();
                        binding.itemText.setText("");
                    } else {
                        Toast.makeText(MainActivity.this, "Please Scan Proper QR Code", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        codeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        codeScanner.releaseResources();
        super.onPause();
    }

    private boolean hasPermission(Context context, String[] permission) {
        if (context != null && permission != null)
            for (String p : permission)
                if (ActivityCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                    return false;

        return true;
    }
}