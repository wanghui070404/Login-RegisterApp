package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DisplayActivity extends AppCompatActivity {

    TextView greetingTextView;
    SQLiteConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);

        // Khởi tạo SQLiteConnector
        dbConnector = new SQLiteConnector(this);
        greetingTextView = findViewById(R.id.greeting_text);

        // Lấy tên người dùng từ Intent và hiển thị lời chào
        String username = getIntent().getStringExtra("username");

        greetingTextView.setText("Chào mừng " + username + "!");
    }
}