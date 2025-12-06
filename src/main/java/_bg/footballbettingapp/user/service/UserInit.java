package _bg.footballbettingapp.user.service;

import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class UserInit implements CommandLineRunner {


    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    public UserInit(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        if(!userRepository.existsByRole(Role.ADMIN)){

            RegisterRequest admin = RegisterRequest.builder()
                    .username("admin")
                    .email("admin@admin.com")
                    .password("123456")
                    .confirmPassword("123456")
                    .build();


            User adminUser = userService.register(admin);
            adminUser.setRole(Role.ADMIN);
            adminUser.setActive(true);
            adminUser.setBalance(BigDecimal.valueOf(10000));
            userService.save(adminUser);
        }



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



