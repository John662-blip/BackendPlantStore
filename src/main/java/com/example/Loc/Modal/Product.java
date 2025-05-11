package com.example.Loc.Modal;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column // Lưu đường dẫn ảnh
    private String imageUrl;

    @Column(nullable = false)
    private String productName;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JsonBackReference
    private Category category;

    private Long stockCount;//hang trong kho

    private Long soldCount=0L;//so luong hang da ban

    private String description;

    private float Humadity;//%

    private int temperature;//do C

    private Long price;

    @Column(nullable = false,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @Column(nullable = false, columnDefinition = "SMALLINT DEFAULT 1")
    private Short status; //0: Ẩn, 1: Hiển thị, 2: Hết hàng

    @PrePersist
    protected void onCreate() {
        if (createDate == null) {
            createDate = new Date();
        }
        if (status == 0){
            status = 1;
        }
    }
}
