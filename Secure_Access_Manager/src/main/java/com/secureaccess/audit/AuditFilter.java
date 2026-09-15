package com.secureaccess.audit;

import com.secureaccess.model.AccessLog;
import com.secureaccess.repository.AccessLogRepository;
import com.secureaccess.service.BruteForceDetectorService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class AuditFilter extends OncePerRequestFilter {

    private final AccessLogRepository accessLogRepository;
    private final BruteForceDetectorService bruteForceDetectorService;

    public AuditFilter(AccessLogRepository accessLogRepository, BruteForceDetectorService bruteForceDetectorService) {
        this.accessLogRepository = accessLogRepository;
        this.bruteForceDetectorService = bruteForceDetectorService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String ip = request.getRemoteAddr();

        // Bloqueia se o IP atingiu o limite de falhas de login no BruteForceDetectorService
        if (bruteForceDetectorService.isIpBlocked(ip) && request.getRequestURI().contains("/api/auth/login")) {
            response.setStatus(429); // 429 Too Many Requests
            response.getWriter().write("IP bloqueado por tentativas excessivas de login (SIEM/Brute-Force).");
            saveLog(request, "BLOCKED", "Tentativa de login bloqueada pelo motor de anomalias");
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void saveLog(HttpServletRequest request, String status, String details) {
        AccessLog log = AccessLog.builder()
                .timestamp(LocalDateTime.now())
                .username("ANONYMOUS")
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader("User-Agent"))
                .endpoint(request.getRequestURI())
                .status(status)
                .details(details)
                .build();
        accessLogRepository.save(log);
    }
}