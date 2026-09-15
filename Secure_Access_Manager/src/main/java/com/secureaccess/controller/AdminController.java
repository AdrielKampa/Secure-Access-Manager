package com.secureaccess.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    // TODO: GET /users -> list registered users (UserRepository)
    // TODO: GET /logs  -> view access logs (AccessLogRepository), filterable by user/date/event type

}
