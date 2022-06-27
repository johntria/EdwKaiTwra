package com.edwkaitwra.backend.api.open;

import com.edwkaitwra.backend.domain.User;
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
//    @GetMapping("/test")
//    @ResponseBody
//    public ResponseEntity<?> test() {
//        String test = "{id:1}";
//        return ResponseEntity.ok().body(test);
//    }

    @GetMapping("/test")
    public ResponseEntity<List<User>> getUser() {
        return ResponseEntity.ok().body(userService.getUsers());
    }
}
