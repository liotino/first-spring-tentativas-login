package com.estudo.tentativaslogin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ApllicationEstudoTentativasLogin {

    public static void main(String [] args) {

        SpringApplication.run(ApllicationEstudoTentativasLogin.class);

    }

}
