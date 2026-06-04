package _bg.footballbettingapp.user;

import _bg.footballbettingapp.common.model.Country;
import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.user.service.UserService;
import _bg.footballbettingapp.web.dto.EditProfileRequest;
import _bg.footballbettingapp.web.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserServiceUTests {
    @Mock
    private  UserRepository userRepository;

    @Mock
    private  PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;



    @Test
    void givenExistingUser_whenLoadUserByUsername_thenReturnAuthenticationDetails() {

        // Given
        String username = "Pumba";
        UUID id = UUID.randomUUID();

        User user = User.builder()
                .id(id)
                .username(username)
                .password("123123")
                .role(Role.ADMIN)
                .isActive(true)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        UserDetails authenticationDetails = userService.loadUserByUsername(username);

        // Then
        assertInstanceOf(AuthenticationDetails.class, authenticationDetails);
        AuthenticationDetails result = (AuthenticationDetails) authenticationDetails;
        assertEquals(user.getId(), result.getUserId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertEquals(user.getRole(), result.getRole());
        assertEquals(user.isActive(), result.isActive());

        assertEquals("ROLE_ADMIN", result.getAuthorities().iterator().next().getAuthority());


    }


    @Test
    void givenMissingUser_whenLoadUserByUsername_thenExceptionIsThrown() {

        // Given
        String username = "Pumba";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));


    }

    @Test
    void givenExistingUsername_whenRegister_thenExceptionIsThrown() {

        // Given
        String username = "Timon";

         RegisterRequest dto = RegisterRequest.builder()
                 .username(username)
                 .email("Timon&Pumba@gmail.com")
                .build();

         when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User())); // С (Optional.of(new User())) симулирам, че в базата има потребител с това име

         // When & Then
        assertThrows(DomainException.class, () -> userService.register(dto));
        verify(userRepository, never()).save(any());



    }


    @Test
    void givenValidRegisterRequest_whenRegister_thenUserIsCreated() {

        // Given
        String username = "Pumba";
        String email = "Timon&Pumba@gmail.com";
        String rawPassword = "123123123";
        String encodedPassword = "************";

        RegisterRequest dto = RegisterRequest.builder()
                .username(username)
                .email(email)
                .password(rawPassword)
                .build();
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(encodedPassword)
                .role(Role.USER)
                .isActive(true)
                .balance(BigDecimal.valueOf(10))
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(user);
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);// моквам паролата защото в register използвам initializeUser(там използвам passwordEncoder )


        // When

        User registeredUser = userService.register(dto);


        // Then
        assertEquals(user.getId(), registeredUser.getId());
        assertEquals(dto.getUsername(),registeredUser.getUsername());
        assertEquals(dto.getEmail(), registeredUser.getEmail());
        assertEquals(encodedPassword, registeredUser.getPassword());
        assertEquals(user.getRole(), registeredUser.getRole());
        assertEquals(user.isActive(), registeredUser.isActive());
        assertEquals(user.getBalance(), registeredUser.getBalance());
        verify(userRepository).save(any(User.class));


    }

    @Test
    void givenExistingEmail_whenRegister_thenExceptionIsThrown(){

        // Given
        String username = "Stamat";
        String email = "StamatStamat@abv.bg";

        RegisterRequest dto = RegisterRequest.builder()
                .username(username)
                .email(email)
                .build();
       User existingUser = new User();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));

        // When & Then

        assertThrows(DomainException.class, () -> userService.register(dto));
        verify(userRepository, never()).save(any());


    }

    @Test
    void givenExistingUser_whenGetUserById_thenReturnUser() {

        // Given
        UUID id = UUID.randomUUID();
        User user = User.builder()
                .id(id)
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        User userById = userService.getUserById(id);

        // Then
        assertEquals(user, userById);
        verify(userRepository).findById(id);

    }

    @Test
    void givenMissingUser_whenGetUserById_thenExceptionIsThrown() {

        // Given
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());


        // When & Then

        assertThrows(DomainException.class, () -> userService.getUserById(id));


    }

    @Test
    void givenExistingUsername_whenGetUserByUsername_thenReturnUser() {

        // Given
        String username = "Stamat";

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // When
        User userByUsername = userService.getUserByUsername(username);

        // Then
        assertEquals(user.getUsername(), userByUsername.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void givenMissingUsername_whenGetUserByUsername_thenExceptionIsThrown() {


        // Given
        String username = "Stamat";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> userService.getUserByUsername(username));

    }

    @Test
    void givenExistingUser_whenEditProfile_thenUserIsUpdatedAndSaved() {

        // Given
        UUID id = UUID.randomUUID();
        EditProfileRequest dto = EditProfileRequest.builder()
                .firstName("Cristiano")
                .lastName("Cristianov")
                .country(Country.ITALY)
                .profilePictureUrl("image.at")
                .build();

        User user = User.builder()
                .id(id)
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        // When
        userService.editProfile(dto,id);

        // Then
        assertEquals("Cristiano", user.getFirstName());
        assertEquals("Cristianov", user.getLastName());
        assertEquals(Country.ITALY, user.getCountry());
        assertEquals("image.at", user.getProfilePictureUrl());
        verify(userRepository).save(user);



    }
}
