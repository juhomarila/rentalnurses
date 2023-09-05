package com.rental.nursing.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.Job;

public interface JobDao extends JpaRepository<Job, Long> {
	List<Job> findByNurseId(Long id);

	List<Job> findByEmployerId(Long id);
}
