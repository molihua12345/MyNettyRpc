package com.lxd;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class RpcServerApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(RpcServerApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}
