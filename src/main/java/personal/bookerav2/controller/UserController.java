package personal.bookerav2.controller;


import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import personal.bookerav2.repository.UserRepository;

@RestController("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;

//
//    public ResponseBody<> updateUser(){
//
//    }

}
