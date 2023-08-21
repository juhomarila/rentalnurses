package com.rental.nursing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.Job;

public interface JobDao extends JpaRepository<Job, Long> {

}
