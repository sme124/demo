package com.example.ssdemoapp.repository;

import com.example.ssdemoapp.model.OtpDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpDetailRepository extends JpaRepository<OtpDetail, Long> {

    Optional<OtpDetail> findByEmail(String email);
}

