package com.example.csgs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.csgs.mapper")
public class CsgsApplication {

    public static void main(String[] args) {
        String a = "aaa";
        System.out.println(a.toString());

//        SpringApplication.run(CsgsApplication.class, args);
    }

}
