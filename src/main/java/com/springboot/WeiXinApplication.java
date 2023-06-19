package com.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description 主函数
 * @author xiaomai
 * @date 2023/6/6 16:19
 */
@SpringBootApplication
@EnableFeignClients
public class WeiXinApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeiXinApplication.class, args);
    }

}
