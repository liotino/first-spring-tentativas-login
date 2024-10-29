package com.estudo.tentativaslogin.controller;

import com.estudo.tentativaslogin.domain.RetryLogin;
import com.estudo.tentativaslogin.dto.RetryLoginDTO;
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
   public ResponseEntity<RetryLoginDTO> add(@RequestBody RetryLogin retryLogin) {

       return ResponseEntity.ok( service.process(retryLogin));
   }

}
