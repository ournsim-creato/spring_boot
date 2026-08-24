package com.spring_boot_api_p2.feature.intergration.captcha.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CaptchaRequest {

    @NotBlank(message = "Captcha ID is required")
    private String captchaId;

    @NotBlank(message = "Captcha data is required")
    private String captchaData;
}