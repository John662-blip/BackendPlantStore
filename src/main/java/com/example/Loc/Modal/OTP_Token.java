package com.example.Loc.Modal;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OTP_Token implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long idAccount;
    @Column
    private String otp_code;
    @Column
    private Date created_at;
    @Column
    private Date expires_at;
    @Column
    private boolean is_verified;//true : da su dung | false : chua su dung
    @Column
    private Long type; //1: email 2 :reset password
}
