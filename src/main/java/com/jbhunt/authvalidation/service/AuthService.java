package com.jbhunt.authvalidation.service;

import com.jbhunt.authvalidation.entity.CustomerCredentials;
import com.jbhunt.authvalidation.repository.CustomerCredentialsRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class AuthService {
    private  final CustomerCredentialsRepository customerCredentialsRepository;

    AuthService(CustomerCredentialsRepository customerCredentialsRepository)   {
        this.customerCredentialsRepository = customerCredentialsRepository;
    }

    public Mono<CustomerCredentials> findCustomer(CustomerCredentials customerCredentials) {
        return customerCredentialsRepository.findByClientIdAndClientSecret(customerCredentials.getClientId(), customerCredentials.getClientSecret());
    }

    public Mono<CustomerCredentials> findCustomerByClientId(String clientId) {
        return customerCredentialsRepository.findByClientId(clientId);
    }

    public Mono<CustomerCredentials> saveCustomer(CustomerCredentials customerCredentials) {
        return customerCredentialsRepository.save(customerCredentials);
    }

    public Flux<CustomerCredentials> fetch() {
        return customerCredentialsRepository.findAll();
    }
}
