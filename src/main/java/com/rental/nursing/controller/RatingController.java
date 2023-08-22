package com.rental.nursing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.exception.SavingDataException;
import com.rental.nursing.service.RatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

	@Autowired
	RatingService ratingService;

	@PostMapping("/employer")
	public ResponseEntity<?> createEmployerRating(@RequestBody EmployerRatingDto dto) {
		try {
			EmployerRatingDto employerRatingDto = ratingService.createEmployerRating(dto);
			return new ResponseEntity<>(employerRatingDto, HttpStatus.CREATED);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/nurse")
	public ResponseEntity<?> createNurseRating(@RequestBody NurseRatingDto dto) {
		try {
			NurseRatingDto nurseRatingDto = ratingService.createNurseRating(dto);
			return new ResponseEntity<>(nurseRatingDto, HttpStatus.CREATED);
		} catch (SavingDataException e) {
			return new ResponseEntity<>(e.getMessages(), HttpStatus.BAD_REQUEST);
		} catch (IllegalStateException e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
