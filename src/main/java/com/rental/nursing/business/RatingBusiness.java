package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;

public interface RatingBusiness {
	Optional<EmployerRating> createEmployerRating(EmployerRatingDto dto);

	Optional<NurseRating> createNurseRating(NurseRatingDto dto);

	List<EmployerRating> getEmployerRatingsByEmployerId(Long employerId);

	Optional<NurseRating> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId);

	List<EmployerRating> getEmployerRatingsByNurseId(Long nurseId);

	Optional<EmployerRating> getEmployerRatingById(Long id);

	Optional<EmployerRating> getEmployerRatingByEmployerId(Long employerId);

	Optional<EmployerRating> getEmployerRatingByNurseId(Long nurseId);

	Optional<EmployerRating> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId);

}
