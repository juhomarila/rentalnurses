package com.rental.nursing.service;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;

public interface RatingService {
	EmployerRatingDto createEmployerRating(EmployerRatingDto dto);

	NurseRatingDto createNurseRating(NurseRatingDto nurseRatingDto);

	EmployerRatingDto employerRatingToDto(EmployerRating employerRating);

	NurseRatingDto nurseRatingToDto(NurseRating nurseRating);
}
