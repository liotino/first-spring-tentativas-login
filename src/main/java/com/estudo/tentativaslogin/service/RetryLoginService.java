package com.estudo.tentativaslogin.service;

import com.estudo.tentativaslogin.domain.RetryLogin;
import com.estudo.tentativaslogin.repository.RetryLoginRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RetryLoginService {

    @Value("${user.cpf}")
    String cpf;

    @Value("${user.birthDate}")
    String birthDate;

    @Value("${retry.travaloginseconds}")
    String retryTimeBlockLogin;

    @Autowired
    RetryLoginRepository retryLoginRepository;

    public void process(RetryLogin retryLogin) {

        if(retryLogin.getDocumentNumber().equals(cpf)) {

            if(retryLogin.getBirthDate().equals(LocalDate.parse(birthDate))) {

                getSizeRetry(retryLogin.getDocumentNumber());
                log.info("Cpf Data nascimento Igual ok 200 add{}",retryLogin);

            }else{

                log.error("Cpf Igual data nascimento nao 400 error add{}", retryLogin);
                reytryLoginTimes(retryLogin);

            }
        }

        if(!retryLogin.getDocumentNumber().equals(cpf)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados Invalidos");

        }
    }

    private void reytryLoginTimes(RetryLogin retryLogin) {

         var lastCountMax = getMaxLoginAttenptes(retryLogin.getDocumentNumber()).orElse(null);
         log.info("lastCount {}", lastCountMax);

         var retryL = retryLogin;
         retryL.setCount(autoIncrement(lastCountMax));
         log.info("Retry Login {}", retryL);

         retryL.setTime(LocalDateTime.now());
         add(retryL);

         if(retryLogin.getCount() >= 3) {

             throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                     "Dados Invalidos tentativa: " + retryLogin.getCount() + " Login Travado! aguardar " + retryTimeBlockLogin + " segundos");

         }

         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dados Invalidos tentativa: " + retryLogin.getCount());

    }


    private Integer autoIncrement(Integer times) {

        if(times != null) {

            if(times == 0) {

                return 1;

            }
            return ++times;
        }

        return null;
    }


    private void add(RetryLogin retryLogin) {

        retryLoginRepository.save(retryLogin);

    }


    private Optional<Integer> getMaxLoginAttenptes(String documentNumber) {

        List<RetryLogin> retryLogins = retryLoginRepository.getRetryBylogin(documentNumber);
        log.info("List retry login {}",retryLogins.size());


        if(retryLogins.size() == 0) {

          return Optional.ofNullable(0);

        }

        List<RetryLogin> retryLog = retryLoginRepository.getRetryBylogin(documentNumber);

        Optional<Integer> attempt = Optional.ofNullable(retryLog.stream()
                .flatMap(retryLogin -> retryLog.stream().map(RetryLogin::getCount))
                .max(Integer::compareTo).orElse(null));

        return attempt;

    }

    private void getSizeRetry(String documentNumber) {

      List<RetryLogin> retryLogins = retryLoginRepository.getRetryBylogin(documentNumber);

        if(retryLogins.size() >= 2) {

        //pegando o ultimo da lista
        var retryLogin = retryLogins.stream().reduce((a, b) -> b)
                .orElse(null);

        var timeRetry = retryLogin.getTime().plusSeconds(Integer.valueOf(retryTimeBlockLogin));

        if(LocalDateTime.now().isAfter(timeRetry)) {

            retryLoginRepository.removeAllByDocumentNumber(retryLogins);

            log.info("Obj retry Depois True destrava login : {}",retryLogin);

        }else{

            log.info("Obj retry Antes False login travado : {}",retryLogin);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero tentativas excedeu ...aguardar: " + retryTimeBlockLogin + "seconds");

        }

       log.info("Obj retry : {}",retryLogin);

      }

    }


}
