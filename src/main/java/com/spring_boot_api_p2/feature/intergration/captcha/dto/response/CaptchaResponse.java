    package com.spring_boot_api_p2.feature.intergration.captcha.dto.response;

    import com.spring_boot_api_p2.feature.intergration.captcha.dto.request.CaptchaRequest;
    import jakarta.validation.Valid;
    import lombok.Builder;
    import lombok.Data;
    @Data
    @Builder
    public class CaptchaResponse {
        private String captchaId;
        private boolean enabled;
        private String imageBase64;
        private String captchaData;
//        // ប្រើ CaptchaRequest របស់អ្នក និងដាក់ @Valid ដើម្បី validate nested fields (@NotBlank លើ captchaId/captchaData)
//        @Valid
//        private CaptchaRequest captcha;
    }
