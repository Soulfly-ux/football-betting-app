package _bg.footballbettingapp.user.service;


import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.web.dto.LoginRequest;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public User login(LoginRequest loginRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(loginRequest.getUsername());


        if (optionalUser.isEmpty()) {
            throw new DomainException("User or password is incorrect");
        }


        User user = optionalUser.get();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new DomainException("User or password is incorrect");
        }


        return user;
    }
     @Transactional
    public User register(RegisterRequest registerRequest) {

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());

        if (optionalUser.isPresent()) {
            throw new DomainException("Username already exists");
        }


        Optional<User> userByEmail = userRepository.findByEmail(registerRequest.getEmail());

        if (userByEmail.isPresent()) {
            throw new DomainException("Email already exists");
        }

        User user = userRepository.save(initializeUser(registerRequest));



       log.info("User: {} is registered", user.getUsername());
       return user;
    }







    public User initializeUser(RegisterRequest registerRequest) {


        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .isActive(true)
                .balance(BigDecimal.valueOf(10))
                .createdOn(LocalDateTime.now())
                .build();
    }

    public User getUserById(UUID userId) {
        Optional<User> byId = userRepository.findById(userId);
        return byId.orElseThrow(() -> new DomainException("User not found"));
    }

    public void save(User user) {
        userRepository.save(user);
    }
    }

