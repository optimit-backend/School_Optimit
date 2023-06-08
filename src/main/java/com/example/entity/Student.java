package com.example.entity;

import com.example.model.request.StudentDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String fatherName;

    private LocalDate birthDate;

    @Column(nullable = false)
    private String docNumber;

    @Column(nullable = false)
    @OneToMany(cascade = CascadeType.ALL)
    private List<Attachment> docPhoto; // guvohnoma yoki pasport rasmi

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment reference;  // ish joyidan siprafka

    @OneToOne(cascade = CascadeType.ALL)
    private Attachment photo;  // 3*4 rasm

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private StudentClass studentClass;

    private boolean active;

    private LocalDateTime addedTime;
//    @OneToOne(cascade = CascadeType.ALL)
//    private Attachment medDocPhoto;  // med sprafka rasmi

    public static Student from(StudentDto studentDto){
        return Student.builder()
                .firstName(studentDto.getFirstName())
                .lastName(studentDto.getLastName())
                .fatherName(studentDto.getFatherName())
                .birthDate(studentDto.getBirthDate())
                .docNumber(studentDto.getDocNumber())
                .docNumber(studentDto.getDocNumber())
                .addedTime(LocalDateTime.now())
                .active(true)
                .build();
    }
}
