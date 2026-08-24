package com.spring_boot_api_p2.feature.auth;

import com.spring_boot_api_p2.domain.entity.User;
import com.spring_boot_api_p2.feature.core.role.user.repository.UserRepository;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"
            );

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,100}$"
            );

    private static final int USERNAME_MAX = 100;
    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 100;


    /**
     * Validate login username and password.
     */
    public User validateLoginCredentials(
            String username,
            String rawPassword
    ) {

        // 1. Validate username
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        // 2. Validate password
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("Password is required");
        }

        // Do NOT trim password.
        // Password spaces can technically be part of a password.
        String normalizedUsername = username.trim();

        // 3. Find user
        User user = userRepository
                .findByUsernameIgnoreCase(normalizedUsername)
                .orElseThrow(() ->
                        new ValidationException(
                                "Invalid username or password"
                        )
                );

        // 4. Check account enabled
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new ValidationException(
                    "Account is disabled"
            );
        }

        // 5. Check account locked
        if (!Boolean.TRUE.equals(user.getAccountNonLocked())) {
            throw new ValidationException(
                    "Account is locked"
            );
        }

        // 6. Check account expiration
        if (!Boolean.TRUE.equals(user.getAccountNonExpired())) {
            throw new ValidationException(
                    "Account has expired"
            );
        }

        // 7. Check credentials expiration
        if (!Boolean.TRUE.equals(user.getCredentialsNonExpired())) {
            throw new ValidationException(
                    "User credentials have expired"
            );
        }
// 8. Check password hash
        String encodedPassword = user.getPassword();

        if (encodedPassword == null || encodedPassword.isBlank()) {
            throw new ValidationException(
                    "Invalid username or password"
            );
        }

// Temporary debug information
        System.out.println("Username = " + normalizedUsername);

        System.out.println(
                "Password hash exists = "
                        + !encodedPassword.isBlank()
        );

        boolean passwordMatches =
                passwordEncoder.matches(
                        rawPassword,
                        encodedPassword
                );

        System.out.println(
                "Password matches = " + passwordMatches
        );

        if (!passwordMatches) {
            throw new ValidationException(
                    "Invalid username or password"
            );
        }

        // 9. Login validation successful
        return user;
    }


    /**
     * Validate username/email format.
     */
    public void validateUsername(String username) {

        if (username == null || username.isBlank()) {
            throw new ValidationException(
                    "Username is required"
            );
        }

        String normalizedUsername = username.trim();

        if (normalizedUsername.length() > USERNAME_MAX) {
            throw new ValidationException(
                    "Username must not exceed "
                            + USERNAME_MAX
                            + " characters"
            );
        }

        if (!EMAIL_PATTERN.matcher(normalizedUsername).matches()) {
            throw new ValidationException(
                    "Username must be a valid email address"
            );
        }
    }


    /**
     * Check whether username already exists.
     */
    public void validateUsernameNotExists(String username) {

        validateUsername(username);

        String normalizedUsername = username.trim();

        if (userRepository.existsByUsernameIgnoreCase(
                normalizedUsername
        )) {
            throw new ValidationException(
                    "Username already exists"
            );
        }
    }


    /**
     * Validate password strength.
     */
    public void validatePassword(String password) {

        if (password == null || password.isBlank()) {
            throw new ValidationException(
                    "Password is required"
            );
        }

        if (password.length() < PASSWORD_MIN) {
            throw new ValidationException(
                    "Password must be at least "
                            + PASSWORD_MIN
                            + " characters"
            );
        }

        if (password.length() > PASSWORD_MAX) {
            throw new ValidationException(
                    "Password must not exceed "
                            + PASSWORD_MAX
                            + " characters"
            );
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new ValidationException(
                    "Password must contain at least one uppercase letter, "
                            + "one lowercase letter, "
                            + "one number, "
                            + "and one special character"
            );
        }
    }
}