package _bg.footballbettingapp.user;

import _bg.footballbettingapp.security.AuthenticationDetails;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

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
}
