package com.onk.security.resetPasswordToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepo extends JpaRepository<ResetPasswordToken, String> {

    @Query("select reset from ResetPasswordToken reset where reset.token = ?1 ")
    ResetPasswordToken getToken(String token);
}
