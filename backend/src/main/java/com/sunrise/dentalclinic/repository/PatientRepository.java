package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByContactNumber(String contactNumber);
}
