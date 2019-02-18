package com.jbhunt.authvalidation.repository;

import com.jbhunt.authvalidation.entity.CustomerCredentials;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public interface CustomerCredentialsRepository extends ReactiveCrudRepository<CustomerCredentials, String> {

    Mono<CustomerCredentials> findByClientIdAndClientSecret(String clientId, String clientSecret);

    Mono<CustomerCredentials> findByClientId(String clientId);
}
