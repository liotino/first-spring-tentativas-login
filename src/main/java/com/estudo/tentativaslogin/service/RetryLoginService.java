package com.estudo.tentativaslogin.service;

import com.estudo.tentativaslogin.domain.RetryLogin;
import com.estudo.tentativaslogin.dto.RetryLoginDTO;

public interface RetryLoginService {

    RetryLoginDTO process(RetryLogin retryLogin);

}
