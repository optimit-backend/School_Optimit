package com.example.model.response;

import com.example.entity.StudentHomework;
import com.example.entity.Subject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentHomeworkResponse {

    private Integer id;

    private int topicNumber;

    private int lessonHour;

    private String homework;

    private String description;

    private String date;

    private Subject subject;

    private Integer teacherId;

    private boolean active;
}
