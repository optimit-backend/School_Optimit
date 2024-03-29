package com.example.mappers;

import com.example.entity.Branch;
import com.example.entity.Student;
import com.example.entity.StudentClass;
import com.example.model.request.StudentRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class StudentMapper {


    public static Student toEntity(StudentRequest studentRequest, StudentClass studentClass, Branch branch) {
        Student student = new Student();
        setStudent(studentRequest, studentClass, branch, student);
        return student;
    }


    public static Student update(StudentRequest studentRequest, StudentClass studentClass, Branch branch, Student student) {
        setStudent(studentRequest, studentClass, branch, student);
        return student;
    }

    private static void setStudent(StudentRequest studentRequest, StudentClass studentClass, Branch branch, Student student) {
        student.setActive(true);
        student.setBranch(branch);
        student.setStudentClass(studentClass);
        student.setAddedTime(LocalDateTime.now());
        student.setLastName(studentRequest.getLastName());
        student.setDocNumber(studentRequest.getDocNumber());
        student.setBirthDate(studentRequest.getBirthDate());
        student.setFirstName(studentRequest.getFirstName());
        student.setFatherName(studentRequest.getFatherName());

        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setPassword(studentRequest.getPassword());
        student.setPaymentAmount(studentRequest.getPaymentAmount());
    }
}
