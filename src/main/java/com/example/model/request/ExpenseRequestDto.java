package com.example.model.request;

import com.example.enums.ExpenseType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ExpenseRequestDto {

    private Integer id;

    @Column(nullable = false)
    private double summa;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private Integer takerId;


    @Column(nullable = false)
    private Integer paymentTypeId;

    @Column(nullable = false)
    private Integer branchId;

    private ExpenseType expenseType;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate givenDate;
}
