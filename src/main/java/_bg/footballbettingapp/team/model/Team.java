package _bg.footballbettingapp.team.model;


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
    private String country;


    @Column(name = "logo_url", length = 255)
    private String logoUrl;
}
