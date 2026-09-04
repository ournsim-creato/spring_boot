package com.spring_boot_api_p2.feature.intergration.gmail.impl;

import com.spring_boot_api_p2.feature.intergration.gmail.EmailService;

import com.spring_boot_api_p2.feature.intergration.gmail.EmailService;
import com.spring_boot_api_p2.property.OtpProperties;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final OtpProperties otpProperties;

    @Value("${spring.mail.username:no-reply@example.com}")
    private String fromAddress; // sender address from application.yml

    @Override
    public void sendOTP(String toEmail, String otp) {
        try {
            // 1) Build a MIME message (supports HTML + UTF-8)
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // 2) Populate headers and HTML body
            helper.setFrom(fromAddress);
            helper.setTo(toEmail);
            helper.setSubject("Your password reset code");
            helper.setText(buildHtml(otp), true); // true = HTML, not plain text

            // 3) Send via configured SMTP server
            mailSender.send(message);

        } catch (Exception e) {
            // Surface failure — OtpServiceImpl must not log "OTP sent" if email never left

            throw new IllegalStateException("Failed to send OTP email", e);
        }
    }

    private String buildHtml(String otp) {
        return """
                <!DOCTYPE html>
                <html lang="en">
                <head><meta charset="UTF-8"/></head>
                <body style="margin:0;padding:0;background:#f4f6f9;font-family:Arial,Helvetica,sans-serif;">
                  <table width="100%%" cellpadding="0" cellspacing="0" style="background:#f4f6f9;padding:40px 0;">
                    <tr>
                      <td align="center">
                        <table width="480" cellpadding="0" cellspacing="0"
                               style="background:#ffffff;border-radius:12px;overflow:hidden;
                                      box-shadow:0 4px 20px rgba(0,0,0,0.08);">
                          <tr>
                            <td align="center" style="background:#2e7d32;padding:32px 40px;">
                              <h1 style="margin:0;color:#ffffff;font-size:20px;font-weight:700;">
                                Password reset code
                              </h1>
                            </td>
                          </tr>
                          <tr>
                            <td style="padding:32px 40px;">
                              <p style="margin:0 0 20px;color:#555;font-size:14px;line-height:1.6;">
                                Use the code below to reset your password.
                              </p>
                              <div style="background:#f1f8e9;border:2px dashed #66bb6a;border-radius:10px;
                                          padding:24px;text-align:center;margin-bottom:20px;">
                                <span style="font-size:38px;font-weight:800;letter-spacing:10px;color:#2e7d32;">
                                  %s
                                </span>
                              </div>
                              <p style="margin:0;color:#7a6000;font-size:13px;">
                                This code expires in %d minutes.
                              </p>
                              <p style="margin:16px 0 0;color:#888;font-size:12px;line-height:1.6;">
                                Never share this code with anyone. If you did not request it, ignore this email.
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(otp, otpProperties.getTtlMinutes());
    }
}
