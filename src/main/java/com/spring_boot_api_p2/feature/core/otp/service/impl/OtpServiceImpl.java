package com.spring_boot_api_p2.feature.core.otp.service.impl;

import com.spring_boot_api_p2.domain.Otp;
import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.exception.ResourceNotFoundException;
import com.spring_boot_api_p2.feature.core.otp.compponent.OtpGenerator;
import com.spring_boot_api_p2.feature.core.otp.dto.request.ResetPasswordRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.SendOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.VerifyOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.repository.OtpRepository;
import com.spring_boot_api_p2.feature.core.otp.service.OtpManager;
import com.spring_boot_api_p2.feature.core.otp.service.OtpService;
import com.spring_boot_api_p2.feature.core.user.repository.UserRepository;
import com.spring_boot_api_p2.feature.intergration.gmail.EmailService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final OtpGenerator otpGenerator;
    private final OtpManager otpManager;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendOtp(SendOtpRequest request) {
        // 1. ស្វែងរកគណនីតាមអ៊ីមែល
        User user = userRepository
                .findByUsernameIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // 2. បង្កើត OTP 6 ខ្ទង់
        String code = otpGenerator.generate();

        // 3. រក្សាទុក OTP
        otpManager.create(user.getId().intValue(), code);

        // 4. ផ្ញើ OTP ទៅកាន់អ៊ីមែល
        emailService.sendOTP(request.getEmail(), code);
    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {
        // 1. ស្វែងរកគណនី
        User user = userRepository
                .findByUsernameIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // 2. ទាញយក OTP row របស់អ្នកប្រើប្រាស់
        Otp otp = otpRepository.findByUserId(user.getId().intValue())
                .orElseThrow(() -> new ValidationException("No OTP found for this user"));

        // 3. ផ្ទៀងផ្ទាត់ code ដោយមិនទាន់ mark consumed (ប្រើ check)
        boolean isValid = otpManager.check(otp, request.getOtp());
        if (!isValid) {
            throw new ValidationException("Invalid or expired OTP code");
        }
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        // 1. ស្វែងរកគណនី
        User user = userRepository
                .findByUsernameIgnoreCase(request.getEmail().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        // 2. ទាញយក OTP row
        Otp otp = otpRepository.findByUserId(user.getId().intValue())
                .orElseThrow(() -> new ValidationException("No OTP found for this user"));

        // 3. ផ្ទៀងផ្ទាត់ code និង mark consumed ភ្លាមៗតែម្ដង (method verify នឹង throw exception បើខុស)
        otpManager.verify(otp, request.getOtp());

        // 4. Encrypt និងរក្សាទុកពាក្យសម្ងាត់ថ្មី
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}