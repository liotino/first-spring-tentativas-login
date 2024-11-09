package com.estudo.tentativaslogin.controller;

import com.estudo.tentativaslogin.domain.RetryLogin;
import com.estudo.tentativaslogin.dto.in.RetryLoginInDTO;
import com.estudo.tentativaslogin.dto.out.RetryLoginOutDTO;
import com.estudo.tentativaslogin.service.RetryLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

   @Autowired
   RetryLoginService service;

   @PostMapping("/add")
   public ResponseEntity<RetryLoginOutDTO> add(@RequestBody RetryLoginInDTO retryLoginInDTO) {

       return ResponseEntity.ok( service.process(retryLoginInDTO));
   }

}
