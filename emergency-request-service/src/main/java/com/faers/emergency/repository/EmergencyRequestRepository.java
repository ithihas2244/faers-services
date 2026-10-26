package com.faers.emergency.repository;

import com.faers.emergency.entity.EmergencyRequestEntity;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyRequestRepository extends JpaRepository<EmergencyRequestEntity, String> {

    List<EmergencyRequestEntity> findByStatusOrderByCreatedAtDesc(EmergencyStatus status);

    List<EmergencyRequestEntity> findBySeverityOrderByCreatedAtDesc(Severity severity);

    List<EmergencyRequestEntity> findByCallerIdOrderByCreatedAtDesc(String callerId);

    List<EmergencyRequestEntity> findAllByOrderByCreatedAtDesc();
}
