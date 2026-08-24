package com.spring_boot_api_p2.feature.intergration.captcha.controller;

import com.spring_boot_api_p2.feature.intergration.captcha.dto.request.CaptchaRequest;
import com.spring_boot_api_p2.feature.intergration.service.CaptchaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {

    private final CaptchaService captchaService;

    @GetMapping
    public ResponseEntity<?> generate() {
        return ResponseEntity.ok(captchaService.generate());
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(
            @Valid @RequestBody CaptchaRequest request) {

        captchaService.validate(
                request.getCaptchaId(),
                request.getCaptchaData()
        );

        return ResponseEntity.ok().build();
    }
}