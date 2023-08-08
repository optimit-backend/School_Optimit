package com.example.kitchen.entity;

import com.example.entity.Branch;
import com.example.kitchen.model.request.DrinksInWareHouseRequest;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class DrinksInWareHouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int count;

    private double totalPrice;

    private int literQuantity;

    private boolean active;

    @ManyToOne
    private Branch branch;

    @ManyToOne
    private Warehouse warehouse;

    public static DrinksInWareHouse toEntity(DrinksInWareHouseRequest request) {
        return DrinksInWareHouse
                .builder()
                .active(true)
                .name(request.getName())
                .count(request.getCount())
                .totalPrice(request.getTotalPrice())
                .literQuantity(request.getLiterQuantity())
                .build();
    }
}
