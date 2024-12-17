package com.example.myapplication.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashPassword(String password) {
        try {
            // Sử dụng SHA-256 để hash
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes());
            StringBuilder hashString = new StringBuilder();

            // Chuyển đổi byte sang chuỗi hex
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashString.append('0');
                hashString.append(hex);
            }

            return hashString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Lỗi: Thuật toán SHA-256 không khả dụng", e);
        }
    }
}
