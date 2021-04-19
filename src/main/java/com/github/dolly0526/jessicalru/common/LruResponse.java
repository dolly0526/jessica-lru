package com.github.dolly0526.jessicalru.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yusenyang
 * @create 2021/4/19 11:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LruResponse {

    private Integer code;

    private String msg;

    private Object value;


    public LruResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
