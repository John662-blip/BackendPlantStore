    package com.example.Loc.Modal;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column // Lưu đường dẫn ảnh
    private String imageUrl;
    @Column
    private String email;
    @Column
    private String password;
    @Column
    private String name;
    @Column
    private String phone;
    @Column
    private Long type;// type : 0 : Admin . type: 1 :Khach
    @Column
    private Long accountStatus; // = 0 : Tai khoan khong hoat dong , 1 : dang hoat dong
    @Column
    private String uname;
    @Column
    private boolean gender;
}
