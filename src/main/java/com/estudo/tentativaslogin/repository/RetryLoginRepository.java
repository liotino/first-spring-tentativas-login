package com.estudo.tentativaslogin.repository;


import com.estudo.tentativaslogin.domain.RetryLogin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface RetryLoginRepository extends MongoRepository<RetryLogin,String> {

    @Query("{'documentNumber' : ?0}")
    List<RetryLogin> getRetryBylogin(String documentNumber);

    default void removeAllByDocumentNumber(List<RetryLogin> retryLogins) {

        if(retryLogins != null && retryLogins.size() > 0) {

            retryLogins.forEach( r -> delete(r));

        }

    }

}

