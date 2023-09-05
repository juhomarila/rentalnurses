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
import com.rental.nursing.logging.NurseLogger;
import com.rental.nursing.service.RatingService;

@RestController
@RequestMapping("/rating")
public class RatingController {

	@Autowired
	RatingService ratingService;

	private final NurseLogger logger;

	@Autowired
	public RatingController(NurseLogger logger) {
		this.logger = logger;
	}

	@PostMapping("/employer")
	public ResponseEntity<?> createEmployerRating(@RequestBody EmployerRatingDto dto) {
		logger.postLogStart("createEmployerRating");
		var vsr = ratingService.createEmployerRating(dto);
		logger.postLogEnd("createEmployerRating");

		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/nurse")
	public ResponseEntity<?> createNurseRating(@RequestBody NurseRatingDto dto) {
		logger.postLogStart("createNurseRating");
		var vsr = ratingService.createNurseRating(dto);
		logger.postLogEnd("createNurseRating");

		return vsr.getVr().validated ? new ResponseEntity<>(vsr.getT(), HttpStatus.OK)
				: new ResponseEntity<>(vsr.getVr().getErrorMsg(),
						vsr.getVr().validated ? HttpStatus.BAD_REQUEST : HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
