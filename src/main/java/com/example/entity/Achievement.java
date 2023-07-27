package com.example.entity;

import com.example.model.request.AchievementDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Achievement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String aboutAchievement;

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photoCertificate;

    @ManyToOne
    @JsonIgnore
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public static Achievement toAchievement(AchievementDto achievement) {
        return Achievement
                .builder()
                .name(achievement.getName())
                .aboutAchievement(achievement.getAboutAchievement())
                .build();
    }
}
