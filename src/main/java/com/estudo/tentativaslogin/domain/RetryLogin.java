package com.estudo.tentativaslogin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RetryLogin {

    @Id
    private String id;

    private Integer count;
    private String documentNumber;
    private LocalDate birthDate;
    private LocalDateTime time;

}
