    package com.spring_boot_api_p2.feature.core.otp.repository;

    import com.spring_boot_api_p2.domain.Otp;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;

    import java.util.Optional;

    @Repository
    public interface OtpRepository extends JpaRepository<Otp,Long> {

        /** ស្វែងរក OTP record តាម userId (unique constraint → មួយ user មួយ record) */
        Optional<Otp> findByUserId(Integer userId);

    }
