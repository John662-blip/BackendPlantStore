package com.example.Loc.Modal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private Boolean status;//0 : thanh cong : cac so khac that bai
    private String message;
    private Object body;
}
