package com.example.kitchen.model.Response;

import com.example.entity.Branch;
import com.example.kitchen.entity.Warehouse;
import com.example.model.response.UserResponseDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
public class PurchasedDrinksResponse {

    private Integer id;

    private String name;

    private int count;

    private double unitPrice;

    private double totalPrice;

    private int literQuantity;

    private String description;

    private boolean active;

    private LocalDateTime localDateTime;

    private UserResponseDto employee;

    private Branch branch;

    private Warehouse warehouse;
}
