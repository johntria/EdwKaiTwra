package com.edwkaitwra.backend.api.open;

import com.edwkaitwra.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/open")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final UserService userService;

    @GetMapping("/test")
    public ResponseEntity<List<?>> getUser() {
        return ResponseEntity.ok().body(List.of("test"));
    }
}
