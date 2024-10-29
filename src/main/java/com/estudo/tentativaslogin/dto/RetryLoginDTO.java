package com.estudo.tentativaslogin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RetryLoginDTO {

    private String id;
    private String documentNumber;
    private String zipcode;
    private LocalDate birthDate;
    private LocalDateTime time;

}
