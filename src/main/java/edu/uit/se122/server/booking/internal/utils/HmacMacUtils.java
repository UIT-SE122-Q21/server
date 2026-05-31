package edu.uit.se122.server.booking.internal.utils;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HmacMacUtils {

    public static String hmacSha256Hex(String key, String str) {
        try {
            // 1. Khởi tạo thuật toán HMAC-SHA256
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");

            // 2. Tạo SecretKey từ khóa (key) truyền vào
            SecretKeySpec secretKey = new SecretKeySpec(
                    key.getBytes(StandardCharsets.UTF_8), "HmacSHA256"
            );
            sha256Hmac.init(secretKey);

            // 3. Thực hiện mã hóa dữ liệu (str)
            byte[] rawHmac = sha256Hmac.doFinal(str.getBytes(StandardCharsets.UTF_8));

            // 4. Chuyển đổi mảng byte thành chuỗi Hex (Hexadecimal)
            return bytesToHex(rawHmac);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tính toán HMAC-SHA256", e);
        }
    }

    // Hàm phụ trợ để chuyển byte[] sang chuỗi Hex giống Node.js .digest('hex')
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
