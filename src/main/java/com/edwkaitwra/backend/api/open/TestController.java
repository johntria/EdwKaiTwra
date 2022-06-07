package com.edwkaitwra.backend.api.open;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        String test = "testttttttt";
        return ResponseEntity.ok().body(test);
    }
}
