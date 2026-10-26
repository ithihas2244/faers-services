package com.faers.emergency.dao;

import com.faers.emergency.entity.EmergencyRequestEntity;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;

import java.util.List;
import java.util.Optional;

public interface EmergencyRequestDao {

    EmergencyRequestEntity save(EmergencyRequestEntity entity);

    Optional<EmergencyRequestEntity> findById(String id);

    List<EmergencyRequestEntity> findAll();

    List<EmergencyRequestEntity> findByStatus(EmergencyStatus status);

    List<EmergencyRequestEntity> findBySeverity(Severity severity);

    List<EmergencyRequestEntity> findByCallerId(String callerId);

    boolean existsById(String id);

    void deleteById(String id);
}
