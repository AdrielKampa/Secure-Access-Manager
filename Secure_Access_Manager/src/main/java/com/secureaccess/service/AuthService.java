package com.secureaccess.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // TODO: register(username, rawPassword) -> hash password (BCrypt), persist User
    // TODO: authenticate(username, rawPassword) -> verify credentials, report result to
    //       BruteForceDetectorService, delegate token issuance to JwtService

}
