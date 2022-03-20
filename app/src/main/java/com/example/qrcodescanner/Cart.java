package com.example.qrcodescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;

import com.example.qrcodescanner.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Cart extends AppCompatActivity {
    ActivityCartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ArrayList<String> items = getIntent().getStringArrayListExtra("items");

        if(items.size()>0){
            StringBuilder text= new StringBuilder();
            for(String s:items){
                List<String>temp= Arrays.asList(s.split("\n"));
                text.append(temp.get(0)).append("\n");
                text.append(temp.get(1)).append("\n");
                text.append(temp.get(2));
                text.append("\n\n");
            }
            binding.items.setText(text);
        }


    }
}