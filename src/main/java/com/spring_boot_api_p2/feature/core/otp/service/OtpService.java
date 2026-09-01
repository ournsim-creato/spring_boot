package com.spring_boot_api_p2.feature.core.otp.service;

import com.spring_boot_api_p2.feature.core.otp.dto.request.ResetPasswordRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.SendOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.VerifyOtpRequest;

public interface OtpService {
    void sendOtp(SendOtpRequest request);
    void verifyOtp(VerifyOtpRequest request);
    void resetPassword(ResetPasswordRequest request);
}