package com.example.employeeturniketapp.entity;

import com.example.employeeturniketapp.entity.temp.AbsInt;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class EmpTurnAction extends AbsInt {

    //BU FIELD TRUE -> INCOME, FALSE  -> OUTCOME
    @Column
    private Boolean actionIncomeYokiOutCome;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @ManyToOne(optional = false)
    private Employee employee;

    public EmpTurnAction(Boolean actionIncomeYokiOutCome, Employee employee) {
        this.actionIncomeYokiOutCome = actionIncomeYokiOutCome;
        this.employee = employee;
    }
}
