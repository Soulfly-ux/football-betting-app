package _bg.footballbettingapp.team.model;


import _bg.footballbettingapp.common.model.Country;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "teams")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "short_name", length = 100)
    private String shortName;


    @Column(length = 50)
    @Enumerated(EnumType.STRING)
    private Country country;


    @Column(name = "logo_url", length = 255)
    private String logoUrl;
}
