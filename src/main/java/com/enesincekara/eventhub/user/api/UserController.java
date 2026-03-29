package com.enesincekara.eventhub.user.api;

import com.enesincekara.eventhub.user.api.dto.RegisterUserRequest;
import com.enesincekara.eventhub.user.application.RegisterUserService;
import com.enesincekara.eventhub.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final RegisterUserService registerUserService;

    @PostMapping
    public User registerUser(@RequestBody RegisterUserRequest req) {
        return registerUserService.registerUser(
                req.email(),
                req.password()
        );

    }
}
