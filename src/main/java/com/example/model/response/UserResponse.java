package com.example.model.response;

import com.example.entity.Branch;
import com.example.entity.Role;
import com.example.entity.Subject;
import lombok.Data;

import java.util.List;


@Data
public class UserResponse {

    private Integer id;

    private int inn;

    private int inps;

    private int workDays;

    private String name;

    private String surname;

    private String fatherName;

    private String biography;

    private String registeredDate;

    private String phoneNumber;

<<<<<<< HEAD
    private Integer profilePhotoId;
=======
    private String profilePhotoUrl;
>>>>>>> 67ccb880a99b336fb6ab7fc42bff89f882b33348

    private String birthDate;

    private String gender;

    private List<Subject> subjects;

    private Role role;

    private Branch branch;

    private Integer businessId;
}
