package com.secureaccess.repository;

import com.secureaccess.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface AccessLogRepository extends JpaRepository<AccessLog, Long> {
    long countByIpAddressAndStatusAndTimestampAfter(String ipAddress, String status, LocalDateTime after);
}