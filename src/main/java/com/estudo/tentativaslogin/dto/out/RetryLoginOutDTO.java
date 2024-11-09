package com.estudo.tentativaslogin.dto.out;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RetryLoginOutDTO {

    private String documentNumber;
    private String zipcode;
    private LocalDate birthDate;
    private LocalDateTime time;

}
