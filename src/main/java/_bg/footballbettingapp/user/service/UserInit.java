package _bg.footballbettingapp.user.service;

import _bg.footballbettingapp.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class UserInit implements CommandLineRunner {


    private final UserService userService;
    @Autowired
    public UserInit(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {

        if(!userService.getAllUsers().isEmpty()){
           return;
        }

        RegisterRequest registerRequest = RegisterRequest.builder()
                .username("Ricardo")
                .email("ricardo@ricardo.com")
                .password("123456")
                .confirmPassword("123456")
                .build();

        userService.register(registerRequest);
    }
}



