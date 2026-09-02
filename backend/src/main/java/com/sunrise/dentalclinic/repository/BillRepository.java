package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    List<Bill> findByIssuedDateBetween(LocalDate from, LocalDate to);
}
