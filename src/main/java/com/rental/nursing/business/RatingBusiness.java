package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.entity.EmployerRating;

public interface RatingBusiness {
	Optional<EmployerRating> createEmployerRating(EmployerRatingDto dto);

	List<EmployerRating> getEmployerRatingsByEmployerId(Long employerId);

	List<EmployerRating> getEmployerRatingsByNurseId(Long nurseId);

	Optional<EmployerRating> getEmployerRatingById(Long id);

	Optional<EmployerRating> getEmployerRatingByEmployerId(Long employerId);

	Optional<EmployerRating> getEmployerRatingByNurseId(Long nurseId);

	Optional<EmployerRating> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId);

}
