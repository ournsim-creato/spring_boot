package com.spring_boot_api_p2.feature.core.otp.controller;


import com.spring_boot_api_p2.feature.core.otp.dto.request.SendOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController       // JSON REST controller
@RequestMapping("/api/otp") // base path for every method below
@RequiredArgsConstructor // Lombok: inject final fields via constructor
public class OtpController {

    // Business logic lives in the service — controller stays thin
    private final OtpService otpService;

    @PostMapping("/send") // POST /api/otp/send
    public ResponseEntity<?> sendOtp(
            @Valid // run Bean Validation on the request body (shape only)
            @RequestBody SendOtpRequest request) {

        // Throws ValidationException on failure → GlobalExceptionHandler → 400
        otpService.sendOtp(request);

        // No data payload — just { status: 200, title: "OK", … }
        return ResponseEntity.ok(null);
    }

}