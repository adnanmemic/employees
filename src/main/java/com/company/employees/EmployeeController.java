package com.company.employees;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class EmployeeController {

    private final String securityName;

    public EmployeeController(@Value("${security.name}") String securityName) {
        this.securityName = securityName;
    }

    @GetMapping("/employee")
    public String employee() {
        return "employee!!!";
    }

    @GetMapping("/greet")
    public String greet() {
        return "whaaaat";
    }

    @GetMapping("/security")
    public String security() {
        return this.securityName;
    }
}
