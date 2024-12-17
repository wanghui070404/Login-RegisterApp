package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.utils.PasswordHasher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView registerTextView;
    SQLiteConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        // Khởi tạo SQLiteConnector
        dbConnector = new SQLiteConnector(this);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerTextView = findViewById(R.id.register_link);

        // Xử lý sự kiện khi nhấn nút Đăng nhập
        loginButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(MainActivity.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {

                // Hash mật khẩu nhập vào
//                String hashedPassword = PasswordHasher.hashPassword(password);
                // Gọi hàm thực hiện API đăng nhập
                loginUser(username, password);



//                // Kiểm tra user trong database
//                if (dbConnector.checkUser(username, hashedPassword)) {
//                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
//
//                    // Chuyển sang DisplayActivity và truyền thông tin username
//                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
//                    intent.putExtra("username", username);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(MainActivity.this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
//                }
            }
        });


        registerTextView.setOnClickListener(view -> {
            // Chuyển sang màn hình đăng ký
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        });
    }
    private boolean authenticate(String username, String password) {
        // Kiểm tra đăng nhập (giả sử là kiểm tra đơn giản với dữ liệu giả)
        return username.equals("admin") && password.equals("1234");
    }
    private void loginUser(String username, String password) {
        // Hash mật khẩu trước khi gửi
        String hashedPassword = PasswordHasher.hashPassword(password);

        // Gọi API thông qua Retrofit
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, hashedPassword);

        apiService.loginUser(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String token = response.body().getToken();
                    Toast.makeText(MainActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang DisplayActivity và truyền token hoặc email
                    Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
                    intent.putExtra("token", token);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Sai thông tin đăng nhập!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Lỗi kết nối tới server!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }
}