package com.estudo.tentativaslogin.service.Impl;

import com.estudo.tentativaslogin.client.AddessClient;
import com.estudo.tentativaslogin.domain.Address;
import com.estudo.tentativaslogin.domain.RetryLogin;
import com.estudo.tentativaslogin.dto.in.RetryLoginInDTO;
import com.estudo.tentativaslogin.dto.out.RetryLoginOutDTO;
import com.estudo.tentativaslogin.repository.RetryLoginRepository;
import com.estudo.tentativaslogin.service.RetryLoginService;
import feign.FeignException;
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
public class RetryLoginServiceImpl implements RetryLoginService {

    @Value("${user.cpf}")
    String cpf;

    @Value("${user.birthDate}")
    String birthDate;

    @Value("${retry.travaloginseconds}")
    String retryTimeBlockLogin;

    @Autowired
    RetryLoginRepository retryLoginRepository;

    @Autowired
    AddessClient client;

    public RetryLoginOutDTO process(RetryLoginInDTO retryLoginInDTO) {

        RetryLogin retryLogin = new RetryLogin();
        retryLogin.setCount(retryLoginInDTO.getCount());
        retryLogin.setDocumentNumber(retryLoginInDTO.getDocumentNumber());
        retryLogin.setBirthDate(retryLoginInDTO.getBirthDate());

        RetryLoginOutDTO retryLoginOutDTO = null;

        if(retryLogin.getDocumentNumber().equals(cpf)) {

            if(retryLogin.getBirthDate().equals(LocalDate.parse(birthDate))) {

                getSizeRetry(retryLogin.getDocumentNumber());

                try {

                 var response = client.getAddress(retryLogin.getDocumentNumber());
                 Address address = response.getBody();

                 retryLoginOutDTO = new RetryLoginOutDTO();
                 retryLoginOutDTO.setDocumentNumber(retryLogin.getDocumentNumber());
                 retryLoginOutDTO.setTime(LocalDateTime.now());
                 retryLoginOutDTO.setBirthDate(retryLogin.getBirthDate());
                 retryLoginOutDTO.setZipcode(address.getZipcode());

                 log.info("Credencias Data Nascimento ok 200-HTTP add{}", retryLoginOutDTO);

                 return retryLoginOutDTO;

                }catch (FeignException fe) {

                   log.error("Error request {} {}",fe.getMessage(),"status code " + fe.status());
                   throw fe;
                }

            }else{

                log.error("Credencias Data Nascimento Invalida  400-HTTP error {}", retryLogin);
                reytryLoginTimes(retryLogin);

            }

        }

        if(!retryLogin.getDocumentNumber().equals(cpf)) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Credencias CPF Invalido 400HTTP error {}");

        }

        return retryLoginOutDTO;
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

            log.info("Credencial Valida! destravado o login : {}",retryLogin);

        }else{

            log.info("Credencial Invalida ! Login Travado : {}",retryLogin);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Numero tentativas excedeu limite ...aguardar: " + retryTimeBlockLogin + " seconds");

        }

       log.info("Obj retry : {}",retryLogin);

      }

    }


}
