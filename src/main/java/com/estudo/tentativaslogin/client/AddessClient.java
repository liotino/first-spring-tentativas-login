package com.estudo.tentativaslogin.client;

import com.estudo.tentativaslogin.domain.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url="${address.api}",name = "FeignClientAddress")
public interface AddessClient {

 @GetMapping("/zipcode/{documentNumber}")
 ResponseEntity<Address> getAddress(@PathVariable String documentNumber);


}
