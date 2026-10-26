package com.faers.emergency.dao;

import com.faers.emergency.entity.EmergencyRequestEntity;
import com.faers.emergency.model.EmergencyStatus;
import com.faers.emergency.model.Severity;
import com.faers.emergency.repository.EmergencyRequestRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmergencyRequestDaoImpl implements EmergencyRequestDao {

    private final EmergencyRequestRepository repository;

    public EmergencyRequestDaoImpl(EmergencyRequestRepository repository) {
        this.repository = repository;
    }

    @Override
    public EmergencyRequestEntity save(EmergencyRequestEntity entity) {
        return repository.save(entity);
    }

    @Override
    public Optional<EmergencyRequestEntity> findById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<EmergencyRequestEntity> findAll() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public List<EmergencyRequestEntity> findByStatus(EmergencyStatus status) {
        return repository.findByStatusOrderByCreatedAtDesc(status);
    }

    @Override
    public List<EmergencyRequestEntity> findBySeverity(Severity severity) {
        return repository.findBySeverityOrderByCreatedAtDesc(severity);
    }

    @Override
    public List<EmergencyRequestEntity> findByCallerId(String callerId) {
        return repository.findByCallerIdOrderByCreatedAtDesc(callerId);
    }

    @Override
    public boolean existsById(String id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(String id) {
        repository.deleteById(id);
    }
}
