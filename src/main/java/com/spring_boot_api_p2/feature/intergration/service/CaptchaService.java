package com.spring_boot_api_p2.feature.intergration.captcha.dto.service;

import com.spring_boot_api_p2.feature.intergration.captcha.dto.response.CaptchaResponse;

public interface CaptchaService {
    CaptchaResponse generate();
    void validate(String captchaId, String captchaData);
}
