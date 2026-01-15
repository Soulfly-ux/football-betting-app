package _bg.footballbettingapp.user.service;


import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.web.dto.EditProfileRequest;
import _bg.footballbettingapp.web.dto.LoginRequest;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserService implements UserDetailsService {

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


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void save(User user) {

        userRepository.save(user);
    }



   public User getUserByUsername(String username) {
       return userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User not found"));

   }

    public void deleteById(UUID userId) {
        userRepository.deleteById(userId);
    }

    public User getCurrentUser(HttpSession session) {
        UUID userId = (UUID) session.getAttribute("user");

        return getUserById(userId);
    }


    public EditProfileRequest getCurrentUserProfile(UUID userId) {
        User user = getUserById(userId);

        // този метод може и да се направи с помоща на mapper class

        EditProfileRequest dto = new EditProfileRequest();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCountry(user.getCountry());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());


        return dto;
    }


    public void editProfile(EditProfileRequest dto, UUID userId) {
        User user = getUserById(userId);

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setCountry(dto.getCountry());
        user.setProfilePictureUrl(dto.getProfilePictureUrl());
        save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() -> new DomainException("User not found"));


        return new AuthenticationDetails(user.getId(), username, user.getPassword(), user.getRole(), user.isActive());
    }
}












