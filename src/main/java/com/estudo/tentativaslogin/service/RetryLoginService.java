package com.estudo.tentativaslogin.service;

import com.estudo.tentativaslogin.dto.in.RetryLoginInDTO;
import com.estudo.tentativaslogin.dto.out.RetryLoginOutDTO;

public interface RetryLoginService {

    RetryLoginOutDTO process(RetryLoginInDTO retryLoginInDTO);

}
