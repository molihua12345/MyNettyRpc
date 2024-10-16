package com.lxd.common.http;

import lombok.Data;

@Data
public class Response {
    private String requestId;
    private Object result;
}
