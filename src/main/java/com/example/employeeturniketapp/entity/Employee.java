package com.example.employeeturniketapp.entity;

import com.example.employeeturniketapp.entity.temp.AbsInt;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Employee extends AbsInt {

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column
    private String phoneNumber;


}
