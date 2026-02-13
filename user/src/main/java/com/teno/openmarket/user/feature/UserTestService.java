package com.teno.openmarket.user.feature;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class UserTestService {
    @PostConstruct
    public void init() {
        System.out.println(">>> [SUCCESS] User Module Bean Scanned!");
    }
}
