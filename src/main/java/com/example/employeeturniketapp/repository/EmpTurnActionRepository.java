package com.example.employeeturniketapp.repository;

import com.example.employeeturniketapp.entity.EmpTurnAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface EmpTurnActionRepository extends JpaRepository<EmpTurnAction, Integer> {

    //EMPLOYEE ID BO`YICHA BARCHA EMPTURNACTIONLARNI LISTINI CHIQARIB BERADI.
    List<EmpTurnAction> findAllByEmployeeId(Integer employee_id);

    //EMPLOYEE ID VA KIRGAN YOKI CHIQQANLIGINI BERILSA BOOLEANDA BERILSA O`SHA EMPTURNACTIONLARNI LISTINI CHIQARIB BERADI
    List<EmpTurnAction> findAllByEmployeeIdAndActionIncomeYokiOutCome(Integer employee_id, Boolean actionIncomeYokiOutCome);

    /*
    ISHGA ERTA KELGAN, ISHDAN ERTA KETGAN, ISHGA KECH KELGAN, ISHDAN KECH KETGAN BARCHA XODIMLARNI YIG`IB OLISH UCHUN
     */
    List<EmpTurnAction> findAllByCreatedAtAfterAndCreatedAtBeforeAndActionIncomeYokiOutCome(Timestamp createdAt, Timestamp createdAt2, Boolean actionIncomeYokiOutCome);

}
