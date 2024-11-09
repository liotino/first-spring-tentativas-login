package com.estudo.tentativaslogin.dto.in;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RetryLoginInDTO {

    private Integer count;
    private String documentNumber;
    private LocalDate birthDate;


}
