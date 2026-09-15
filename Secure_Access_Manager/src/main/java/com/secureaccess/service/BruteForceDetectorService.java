package com.secureaccess.service;

import com.secureaccess.repository.AccessLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class BruteForceDetectorService {

    private final AccessLogRepository accessLogRepository;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int TIME_WINDOW_MINUTES = 2;

    public BruteForceDetectorService(AccessLogRepository accessLogRepository) {
        this.accessLogRepository = accessLogRepository;
    }

    public boolean isIpBlocked(String ipAddress) {
        LocalDateTime timeWindow = LocalDateTime.now().minusMinutes(TIME_WINDOW_MINUTES);
        long failedAttempts = accessLogRepository.countByIpAddressAndStatusAndTimestampAfter(
                ipAddress, "FAILURE", timeWindow
        );
        return failedAttempts >= MAX_FAILED_ATTEMPTS;
    }
}