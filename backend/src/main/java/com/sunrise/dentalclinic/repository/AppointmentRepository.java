package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentNo(String appointmentNo);

    List<Appointment> findByDate(LocalDate date);
}
