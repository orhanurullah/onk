package com.onk.security.resetPasswordToken;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class ResetPasswordTokenService {

    private final ResetPasswordTokenRepo resetPasswordTokenRepo;

    public ResetPasswordTokenService(ResetPasswordTokenRepo resetPasswordTokenRepo) {
        this.resetPasswordTokenRepo = resetPasswordTokenRepo;
    }

    public void save(ResetPasswordToken resetPasswordToken) {
        resetPasswordTokenRepo.save(resetPasswordToken);
    }

    public ResetPasswordToken validatePasswordResetToken(String token) {
        return resetPasswordTokenRepo.getToken(token);
    }
}
