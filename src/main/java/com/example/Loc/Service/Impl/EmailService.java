package com.example.Loc.Service.Impl;

import com.example.Loc.Service.IEmailService;
import com.example.Loc.Untils.EmailUtil;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service
public class EmailService implements IEmailService {
    @Override
    public int SendOTP_ResetPassword(String email, String OTP) {
        String fromEmail = "hung59069@gmail.com"; //requires valid gmail id
        String password = "vkbt mgsy zbsd ukap"; // correct password for gmail id
        String toEmail = email; // can be any email id
        String message = "Mã Xác Nhận Của Bạn Là : "+OTP+" Dùng Mã Này Để Reset Password. Mã Sẽ Hết Hạn Sau 10 Phút";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        return EmailUtil.sendEmail(session, toEmail,"Mã Reset", message);
    }

    @Override
    public int SentOTP_Confirm(String email, String OTP) {
        String fromEmail = "hung59069@gmail.com"; //requires valid gmail id
        String password = "vkbt mgsy zbsd ukap"; // correct password for gmail id
        String toEmail = email; // can be any email id
        String message = "Mã Xác Thực Của Bạn Là : "+OTP+" Mã Sẽ Hết Hạn Sau 10 Phút";
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };
        Session session = Session.getInstance(props, auth);

        return EmailUtil.sendEmail(session, toEmail,"Mã Kích Hoạt", message);
    }
}
