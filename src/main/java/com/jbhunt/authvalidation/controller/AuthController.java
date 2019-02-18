package com.jbhunt.authvalidation.controller;

import com.jbhunt.authvalidation.dto.CustomerAuthDTO;
import com.jbhunt.authvalidation.entity.CustomerCredentials;
import com.jbhunt.authvalidation.service.AuthService;
import com.jbhunt.authvalidation.util.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    private final JwtTokenUtil jwtTokenUtil;

    AuthController(AuthService authService, JwtTokenUtil jwtTokenUtil) {
        this.authService = authService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/token")
    public Mono<ResponseEntity<Map<String, String>>> validateClient(@RequestBody CustomerCredentials customerCredential) {
        return authService.findCustomer(customerCredential)
                .map(customerCredentia -> {
                    final String token = jwtTokenUtil.doGenerateToken(customerCredentia.getClientId());
                    Map<String, String> keyMap = new HashMap<>();
                    keyMap.put("token", token);
                    return ResponseEntity.ok(keyMap);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
    }

    @PostMapping("/validate")
    public Mono<ResponseEntity<Map<String, String>>> validateToken(@RequestBody CustomerAuthDTO customerAuthDTO) {
        return jwtTokenUtil.isValidToken(customerAuthDTO)
                .map(clientId -> {
                    Map<String, String> resultMap = new HashMap<>();
                    resultMap.put("clientId", clientId);
                    return ResponseEntity.ok(resultMap);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @GetMapping("/cusomter/{customerId}")
    public Mono<ResponseEntity<CustomerCredentials>> getCustomerAuthDetails(@PathVariable("customerId") String customerId) {
        return authService.findCustomerByClientId(customerId)
                .map(customerCredential -> ResponseEntity.ok(customerCredential))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<CustomerCredentials>> saveClientDetails(@RequestBody CustomerCredentials customerCredentials) {
        return authService.saveCustomer(customerCredentials)
                .map(customerCredential -> ResponseEntity.ok(customerCredential))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/fetch")
    public Flux<CustomerCredentials> fetchClientDetails() {
        return authService.fetch();
    }
}
