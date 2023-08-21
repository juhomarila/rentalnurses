package com.rental.nursing.service;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.entity.EmployerRating;

public interface RatingService {
	EmployerRatingDto createEmployerRating(EmployerRatingDto dto);

	EmployerRatingDto employerRatingToDto(EmployerRating employerRating);
}
