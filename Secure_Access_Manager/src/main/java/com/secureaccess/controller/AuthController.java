package com.secureaccess.controller;

import com.secureaccess.audit.AuditFilter;
import com.secureaccess.model.User;
import com.secureaccess.repository.UserRepository;
import com.secureaccess.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuditFilter auditFilter;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuditFilter auditFilter) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.auditFilter = auditFilter;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> body) {
        User user = User.builder()
                .username(body.get("username"))
                .password(passwordEncoder.encode(body.get("password")))
                .role("ROLE_USER")
                .build();
        userRepository.save(user);
        return ResponseEntity.ok("Usuario cadastrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body, HttpServletRequest request) {
        var userOpt = userRepository.findByUsername(body.get("username"));
        if (userOpt.isPresent() && passwordEncoder.matches(body.get("password"), userOpt.get().getPassword())) {
            auditFilter.saveLog(request, "SUCCESS", "Login bem-sucedido");
            return ResponseEntity.ok(Map.of("token", jwtService.generateToken(body.get("username"))));
        }
        auditFilter.saveLog(request, "FAILURE", "Falha de credenciais");
        return ResponseEntity.status(401).body("Credenciais invalidas");
    }
}