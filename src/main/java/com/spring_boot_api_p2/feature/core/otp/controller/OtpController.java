package com.spring_boot_api_p2.feature.core.otp.controller;

import com.spring_boot_api_p2.feature.core.otp.dto.request.ResetPasswordRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.SendOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.VerifyOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    // Step 1: Send OTP to user's email
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(
            @Valid
            @RequestBody SendOtpRequest request) {

        otpService.sendOtp(request);
        return ResponseEntity.ok().build();
    }

    // Step 2: Verify OTP
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(
            @Valid
            @RequestBody VerifyOtpRequest request) {

        otpService.verifyOtp(request);
        return ResponseEntity.ok().build();
    }

    // Step 3: Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid
            @RequestBody ResetPasswordRequest request) {

        otpService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}