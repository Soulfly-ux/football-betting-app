package _bg.footballbettingapp.user;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.user.service.UserAdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class UserAdminServiceUTests {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserAdminService userAdminService;



    @Test
    void givenUserWithStatusActive_whenSwitchStatus_thenUserBecomeInactive() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .isActive(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userAdminService.switchStatus(userId);


        // Then
        assertFalse(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenUserWithStatusInactive_whenSwitchStatus_thenUserBecomeActive() {

        // Given
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .isActive(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        userAdminService.switchStatus(userId);


        // Then
        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void givenMissingUser_whenSwitchStatus_thenExceptionIsThrown() {

        UUID userId = UUID.randomUUID();
        User user = User.builder().build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> userAdminService.switchStatus(userId));
        verify(userRepository, never()).save(user);




    }


    @Test
    void givenUserRole_whenSwitchRole_thenCorrectRoleIsAdmin () {
        // Given
        UUID targetId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();

        User user = User.builder()
                .id(targetId)
                .role(Role.USER)
                .build();

        when(userRepository.findById(targetId)).thenReturn(Optional.of(user));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(1L);


        // When
        userAdminService.switchRole(targetId, actorId);

        // Then
        assertEquals(Role.ADMIN, user.getRole());
        verify(userRepository).save(user);

    }

    @Test
    void givenUserRole_whenSwitchRole_thenCorrectRoleIsUser () {
        // Given
        UUID targetId = UUID.randomUUID();
        UUID actorId = UUID.randomUUID();

        User userAdmin = User.builder()
                .id(targetId)
                .role(Role.ADMIN)
                .build();



        when(userRepository.findById(targetId)).thenReturn(Optional.of(userAdmin));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(3L);


        // When
        userAdminService.switchRole(targetId, actorId);

        // Then
        assertEquals(Role.USER, userAdmin.getRole());
        verify(userRepository).save(any());


    }


    @Test
    void givenSameActorAndTargetUser_whenSwitchRole_thenExceptionIsThrown() {

        // Given

        UUID id = UUID.randomUUID();

        // When & Then

        assertThrows(DomainException.class, () -> userAdminService.switchRole(id, id));
        verify(userRepository, never()).save(any());

    }

    @Test
    void givenLastAdmin_whenSwitchRole_thenExceptionIsThrown() {

        // Given
        UUID targetId = UUID.randomUUID();
        UUID actorId= UUID.randomUUID();

        User user = User.builder()
                .id(targetId)
                .role(Role.ADMIN)
                .build();

        when(userRepository.findById(targetId)).thenReturn(Optional.of(user));
        when(userRepository.countByRole(Role.ADMIN)).thenReturn(1L);

        // When & Then
        assertEquals(Role.ADMIN,user.getRole());
        assertThrows(DomainException.class, () -> userAdminService.switchRole(targetId, actorId));
        verify(userRepository, never()).save(any());


    }


    @Test
    void givenMissingTargetUser_whenSwitchRole_thenExceptionIsThrown() {

        // Given
        UUID targetId = UUID.randomUUID();
        UUID actorId= UUID.randomUUID();

        when(userRepository.findById(targetId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(DomainException.class, () -> userAdminService.switchRole(targetId, actorId));
        verify(userRepository, never()).save(any());






    }
}
