package com.example.Loc.Responsitory;

import com.example.Loc.Modal.OTP_Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OTP_TokenRepository extends JpaRepository<OTP_Token, Long> {
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OTP_Token o " +
            "WHERE o.idAccount = :idAccount AND o.otp_code = :otpCode " +
            "AND o.created_at <= CURRENT_TIMESTAMP AND o.expires_at >= CURRENT_TIMESTAMP " +
            "AND o.type = 1 AND o.is_verified = false")
    boolean verifyOTP(@Param("idAccount") Long idAccount, @Param("otpCode") String otpCode);

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OTP_Token o " +
            "WHERE o.idAccount = :idAccount AND o.otp_code = :otpCode " +
            "AND o.created_at <= CURRENT_TIMESTAMP AND o.expires_at >= CURRENT_TIMESTAMP " +
            "AND o.type = 2 AND o.is_verified = false")
    boolean verifyOTP_reset(@Param("idAccount") Long idAccount, @Param("otpCode") String otpCode);

}
