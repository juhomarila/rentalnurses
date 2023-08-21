package com.rental.nursing.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.EmployerRating;

import jakarta.transaction.Transactional;

public interface EmployerRatingDao extends JpaRepository<EmployerRating, Long> {

	@Transactional
	List<EmployerRating> findByEmployerId(long employerId);

	@Transactional
	List<EmployerRating> findByNurseId(long nurseId);

	@Transactional
	List<EmployerRating> findByEmployerIdAndNurseId(long employerId, long nurseId);
}
