package com.spring_boot_api_p2.feature.intergration.gmail;

public interface EmailService {
    void sendOTP(String toEmail, String opt);

}
