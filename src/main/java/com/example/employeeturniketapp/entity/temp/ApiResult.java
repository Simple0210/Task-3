package com.example.employeeturniketapp.entity.temp;

import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Data
//@JsonInclude(content = JsonInclude.Include.NON_NULL) //null qiymat bormasligi uchun
public class ApiResult {

    private String message;

    private Object object;

    private boolean success;

    public ApiResult(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public ApiResult(Object object, boolean success) {
        this.object = object;
        this.success = success;
    }
}
