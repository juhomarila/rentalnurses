package com.rental.nursing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.Nurse;

public interface NurseDao extends JpaRepository<Nurse, Long> {

}
