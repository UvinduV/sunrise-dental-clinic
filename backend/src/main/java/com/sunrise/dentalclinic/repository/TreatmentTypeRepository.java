package com.sunrise.dentalclinic.repository;

import com.sunrise.dentalclinic.entity.TreatmentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreatmentTypeRepository extends JpaRepository<TreatmentType, Long> {
}
