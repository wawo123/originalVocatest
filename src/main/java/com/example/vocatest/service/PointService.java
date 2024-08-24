package com.example.vocatest.service;

import com.example.vocatest.entity.PointRequestEntity;
import com.example.vocatest.repository.PointRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PointService {

    private final PointRequestRepository pointRequestRepository;

    public PointService(PointRequestRepository pointRequestRepository){
        this.pointRequestRepository = pointRequestRepository;
    }
    public PointRequestEntity savePointRequest(PointRequestEntity pointRequestEntity){
        return pointRequestRepository.save(pointRequestEntity);
    }

    public List<PointRequestEntity> showPointRequest(String email){
        return pointRequestRepository.findByEmail(email);
    }

    public Optional<PointRequestEntity> findPointRequestEntityById(Long id){
        return pointRequestRepository.findById(id);
    }
}

