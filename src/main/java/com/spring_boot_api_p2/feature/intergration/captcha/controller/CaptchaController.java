package com.spring_boot_api_p2.feature.intergration.captcha;

import com.spring_boot_api_p2.base.BaseApi;
import com.spring_boot_api_p2.feature.intergration.captcha.dto.response.CaptchaResponse;
import com.spring_boot_api_p2.feature.intergration.captcha.dto.service.CaptchaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/captcha")
@RequiredArgsConstructor
public class CaptchaController {
    private final CaptchaService captchaService;
    @GetMapping //GET /api/captcha
    public ResponseEntity<?> generate(){
        // delegate to service.wrap payload in the standard API envelope
        return ResponseEntity.ok(captchaService.generate());
    }

}
