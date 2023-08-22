package com.rental.nursing.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.NurseRating;

import jakarta.transaction.Transactional;

public interface NurseRatingDao extends JpaRepository<NurseRating, Long> {
	@Transactional
	List<NurseRating> findByEmployerId(long employerId);

	@Transactional
	List<NurseRating> findByNurseId(long nurseId);

	@Transactional
	List<NurseRating> findByEmployerIdAndNurseId(long employerId, long nurseId);
}
