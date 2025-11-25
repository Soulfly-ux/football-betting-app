package _bg.footballbettingapp.user.model;

import _bg.footballbettingapp.common.model.Country;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)// ако ще го изисквам задължително за регистрацията
    private String email;

    @Column(nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column
    @Enumerated(EnumType.STRING)
    private Country country;


    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_on")
    private LocalDateTime createdOn;
}
