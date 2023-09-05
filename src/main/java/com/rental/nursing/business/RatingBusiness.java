package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.EmployerRatingUpdateDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.dto.NurseRatingUpdateDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;

public interface RatingBusiness {
	Optional<EmployerRating> createEmployerRating(EmployerRatingDto dto);

	Optional<EmployerRating> updateEmployerRating(EmployerRating rating, EmployerRatingUpdateDto newDto);

	List<EmployerRating> getEmployerRatingsByEmployerId(Long employerId);

	Optional<EmployerRating> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId);

	List<EmployerRating> getEmployerRatingsByNurseId(Long nurseId);

	Optional<EmployerRating> getEmployerRatingById(Long id);

	Optional<NurseRating> createNurseRating(NurseRatingDto dto);

	Optional<NurseRating> updateNurseRating(NurseRating nurseRating, NurseRatingUpdateDto newDto);

	List<NurseRating> getNurseRatingsByNurseId(Long nurseId);

	Optional<NurseRating> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId);

	List<NurseRating> getNurseRatingsByEmployerId(Long employerId);
}
