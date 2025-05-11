package com.example.Loc.Service;

public interface IEmailService {
    int SentOTP_Confirm(String email, String OTP);
    int SendOTP_ResetPassword(String email,String OTP);
}
