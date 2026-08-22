package com.spring_boot_api_p2.feature.core.permission.validator;

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

    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 100;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$"
            );

    /**
     * Validate login credentials.
     */
    public User validateLoginCredentials(
            String username,
            String rawPassword
    ) {

        // Check username
        if (username == null || username.isBlank()) {
            throw new ValidationException("Username is required");
        }

        // Check password
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new ValidationException("Password is required");
        }

        // Normalize username
        String normalizedUsername =
                username.trim().toLowerCase();

        // Find user
        User user = userRepository
                .findByUsername(normalizedUsername)
                .orElse(null);

        // User not found
        if (user == null) {
            throw new ValidationException(
                    "Invalid username or password"
            );
        }

        // Account disabled
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new ValidationException(
                    "Account is disabled"
            );
        }

        // Account locked
        if (!Boolean.TRUE.equals(user.getAccountNonLocked())) {
            throw new ValidationException(
                    "Account is locked"
            );
        }

        // Account expired
        if (!Boolean.TRUE.equals(user.getAccountNonExpired())) {
            throw new ValidationException(
                    "Account has expired"
            );
        }

        // Credentials expired
        if (!Boolean.TRUE.equals(user.getCredentialsNonExpired())) {
            throw new ValidationException(
                    "User credentials have expired"
            );
        }

        // Check password
        if (!passwordEncoder.matches(
                rawPassword,
                user.getPassword()
        )) {
            throw new ValidationException(
                    "Invalid username or password"
            );
        }

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

        String normalizedUsername =
                username.trim().toLowerCase();

        if (normalizedUsername.length() > 100) {
            throw new ValidationException(
                    "Username must not exceed 100 characters"
            );
        }

        if (!EMAIL_PATTERN.matcher(normalizedUsername).matches()) {
            throw new ValidationException(
                    "Username must be a valid email address"
            );
        }
    }

    /**
     * Check if username already exists.
     */
    public void validateUsernameNotExists(String username) {

        validateUsername(username);

        String normalizedUsername =
                username.trim().toLowerCase();

        if (userRepository.existsByUsername(
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

        if (!password.equals(password.trim())) {
            throw new ValidationException(
                    "Password must not have leading or trailing spaces"
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
                            + "one lowercase letter, one number, "
                            + "and one special character"
            );
        }
    }
}