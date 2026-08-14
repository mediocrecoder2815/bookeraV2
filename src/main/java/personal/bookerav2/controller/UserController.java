package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import personal.bookerav2.dto.user.UserDtoResponse;
import personal.bookerav2.repository.UserRepository;
import personal.bookerav2.service.UserService;

import java.security.Principal;

@RestController("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDtoResponse> getMe(Principal principal){

    }

}
