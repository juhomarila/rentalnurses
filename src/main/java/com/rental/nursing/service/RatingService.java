package com.rental.nursing.service;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;

public interface RatingService {
	ValidateServiceResult<EmployerRatingDto> createEmployerRating(EmployerRatingDto dto);

	ValidateServiceResult<NurseRatingDto> createNurseRating(NurseRatingDto nurseRatingDto);

	ValidateServiceResult<EmployerRatingDto> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId);

	ValidateServiceResult<NurseRatingDto> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId);

	EmployerRatingDto employerRatingToDto(EmployerRating employerRating);

	NurseRatingDto nurseRatingToDto(NurseRating nurseRating);
}
