package com.spring_boot_api_p2.feature.core.otp.service.impl;


import com.spring_boot_api_p2.feature.core.otp.compponent.OtpGenerator;
import com.spring_boot_api_p2.feature.core.otp.dto.request.ResetPasswordRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.SendOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.dto.request.VerifyOtpRequest;
import com.spring_boot_api_p2.feature.core.otp.service.OtpService;
import com.spring_boot_api_p2.feature.core.role.user.repository.UserRepository;
import com.spring_boot_api_p2.feature.intergration.gmail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final UserRepository userRepository;
    private final OtpGenerator otpGenerator;       // random 6-digit code
    //private final OtpManager otpManager;
    private final EmailService emailService;
    @Override
    public void sendOtp(SendOtpRequest request) {
        // 1) Trim whitespace, lowercase email so lookups are consistent
        //otpNormalizer.normalize(request);

        // 2) Business rules: email format, account exists, etc.
        //otpValidator.validate(request);

        // 3) Resolve the account — username == email in this project
        // User user = findUser(request.getEmail());

        // 4) Cryptographically random 6-digit code (plaintext only in memory)
        String code = otpGenerator.generate();

        // 5) Persist encrypted first — if the DB write fails we must not send a code
        //    the server cannot later verify.
        //otpManager.create(user.getId(), code);

        // 6) Deliver plaintext code to the user's inbox via SMTP
        emailService.sendOTP(request.getEmail(), code);

    }

    @Override
    public void verifyOtp(VerifyOtpRequest request) {

    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {

    }
}