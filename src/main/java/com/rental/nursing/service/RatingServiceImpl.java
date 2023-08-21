package com.rental.nursing.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.EmployerBusiness;
import com.rental.nursing.business.NurseBusiness;
import com.rental.nursing.business.RatingBusiness;
import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;
import com.rental.nursing.exception.SavingDataException;

@Service
public class RatingServiceImpl implements RatingService {
	@Autowired
	private RatingBusiness ratingBusiness;

	@Autowired
	private NurseBusiness nurseBusiness;

	@Autowired
	private EmployerBusiness employerBusiness;

	@Autowired
	private EmployerRatingValidator employerRatingValidator;

	@Autowired
	private NurseRatingValidator nurseRatingValidator;

	private static final Logger logger = LoggerFactory.getLogger(RatingServiceImpl.class);

	@Override
	public EmployerRatingDto createEmployerRating(EmployerRatingDto dto) {
		ValidationResult vrInput = employerRatingValidator.validate(dto);
		if (ratingBusiness.getEmployerRatingByEmployerAndNurseId(dto.getEmployerId(), dto.getNurseId()).isPresent()) {
			vrInput.errorMsg.add(ValidationError.VE004 + ".exists");
			vrInput.validated = false;
		}
		if (!nurseBusiness.getNurseById(dto.getNurseId()).get().isVerified()) {
			vrInput.errorMsg.add(ValidationError.VE005 + ".notVerifiedNurse");
			vrInput.validated = false;
		}
		if (!employerBusiness.getEmployerById(dto.getEmployerId()).get().isVerified()) {
			vrInput.errorMsg.add(ValidationError.VE005 + ".notVerifiedEmployer");
			vrInput.validated = false;
		}
		if (!vrInput.validated) {
			logger.error(ValidationError.RE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}

		Optional<EmployerRating> optEmployerRating = ratingBusiness.createEmployerRating(dto);
		if (optEmployerRating.isPresent()) {
			EmployerRatingDto employerRatingDto = employerRatingToDto(optEmployerRating.get());
			ValidationResult vrOutput = employerRatingValidator.validate(employerRatingDto);
			if (!vrOutput.validated) {
				logger.error(ValidationError.RE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrInput.getErrorMsg());
			}
			return employerRatingDto;
		} else {
			throw new IllegalStateException(ValidationError.RE201);
		}
	}

	@Override
	public EmployerRatingDto employerRatingToDto(EmployerRating employerRating) {
		EmployerRatingDto employerRatingDto = new EmployerRatingDto();
		employerRatingDto.setId(employerRating.getId());
		employerRatingDto.setRating(employerRating.getRating());
		employerRatingDto.setEmployerId(employerRating.getEmployer().getId());
		employerRatingDto.setNurseId(employerRating.getNurse().getId());
		employerRatingDto.setComment(employerRating.getComment());
		return employerRatingDto;
	}

	private NurseRatingDto nurseRatingToDto(NurseRating nurseRating) {
		NurseRatingDto nurseRatingDto = new NurseRatingDto();
		nurseRatingDto.setId(nurseRating.getId());
		nurseRatingDto.setRating(nurseRating.getRating());
		nurseRatingDto.setEmployerId(nurseRating.getEmployer().getId());
		nurseRatingDto.setNurseId(nurseRating.getNurse().getId());
		return nurseRatingDto;
	}

}
