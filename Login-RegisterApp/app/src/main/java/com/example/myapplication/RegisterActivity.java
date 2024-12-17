package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.utils.PasswordHasher;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText, usernameEditText, passwordEditText;
    Button registerButton;
//    SQLiteConnector dbConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Khởi tạo SQLiteConnector
//        dbConnector = new SQLiteConnector(this);


        emailEditText = findViewById(R.id.email);
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        registerButton = findViewById(R.id.register_button);

        // Xử lý sự kiện khi nhấn nút Đăng ký
        registerButton.setOnClickListener(view -> {
            String username = usernameEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {

                // Hash mật khẩu
                String hashedPassword = PasswordHasher.hashPassword(password);

                registerUser(username, email, hashedPassword);

                // Tạo đối tượng User
//                User user = new User();
//                user.setName(username);
//                user.setEmail(email);
//                user.setPassword(hashedPassword);
                
//                // Thêm user vào cơ sở dữ liệu
//                dbConnector.addUser(user);
//                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                // Đóng activity (quay lại màn hình đăng nhập)
                finish();
            }
        });

    }
    private boolean validateRegistration(String email, String username, String password) {
        // Kiểm tra đăng ký (giả sử chỉ kiểm tra nếu thông tin không rỗng)
        return !email.isEmpty() && !username.isEmpty() && !password.isEmpty();
    }

    private void registerUser(String username, String email, String password) {
        ApiService apiService = ApiClient.getRetrofitInstance().create(ApiService.class);

        // Tạo đối tượng User
        Users user = new Users(username, email, password);

        apiService.registerUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng activity và quay về màn hình đăng nhập
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại! Hãy thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối đến server!", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

}