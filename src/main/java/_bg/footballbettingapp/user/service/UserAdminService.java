package _bg.footballbettingapp.user.service;

import _bg.footballbettingapp.exception.DomainException;
import _bg.footballbettingapp.user.model.Role;
import _bg.footballbettingapp.user.model.User;
import _bg.footballbettingapp.user.repository.UserRepository;
import _bg.footballbettingapp.web.dto.AdminDashboardStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserAdminService {

    private final UserRepository userRepository;

    @Autowired
    public UserAdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllUsers() {


        return  userRepository.findAll(Sort.by("username"));
    }


      public void switchRole(UUID targetUserId, UUID actorUserId) {


          // targetUserId-> потребителя, чиято роля ще сменя като админ(= id-то от URL: /{id}/role)
          //  actorUserId -> = id-то от session: session.getAttribute("user") → това си ти (логнатият админ)

          if (targetUserId.equals(actorUserId)){
              throw new DomainException("Cannot switch role for yourself");
          }

          User user = getUserById(targetUserId);

          long adminCount = userRepository.countByRole(Role.ADMIN);


          if(adminCount == 1 && user.getRole() == Role.ADMIN) {
              throw new DomainException("Cannot remove last admin role");
          }


          if(user.getRole() == Role.ADMIN) {
              user.setRole(Role.USER);
          } else {
              user.setRole(Role.ADMIN);
          }

          userRepository.save(user);
      }

      public void switchStatus(UUID userId) {
          User user = getUserById(userId);

          user.setActive(!user.isActive());

          userRepository.save(user);
      }

      public User getUserById(UUID userId) {
          return userRepository.findById(userId).orElseThrow(() -> new DomainException("User not found"));
      }


      public long countActiveUsers() {
          return userRepository.countByIsActive(true);
      }

      public long countAdmins() {
          return userRepository.countByRole(Role.ADMIN);
      }


     public long countUsers() {
        return userRepository.count();
     }

}
