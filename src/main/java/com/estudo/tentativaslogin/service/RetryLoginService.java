package com.estudo.tentativaslogin.service;

import com.estudo.tentativaslogin.domain.RetryLogin;

public interface RetryLoginService {

    void process(RetryLogin retryLogin);

}
