package com.rental.nursing.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.RatingBusiness;
import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;
import com.rental.nursing.logging.NurseLogger;

@Service
public class RatingServiceImpl implements RatingService {
	@Autowired
	private RatingBusiness ratingBusiness;

	@Autowired
	private EmployerRatingValidator employerRatingValidator;

	@Autowired
	private NurseRatingValidator nurseRatingValidator;

	private final NurseLogger logger;

	@Autowired
	public RatingServiceImpl(NurseLogger logger) {
		this.logger = logger;
	}

	@Override
	public ValidateServiceResult<EmployerRatingDto> createEmployerRating(EmployerRatingDto dto) {
		boolean isRatingPresent = ratingBusiness
				.getEmployerRatingByEmployerAndNurseId(dto.getEmployerId(), dto.getNurseId()).isPresent() ? true
						: false;
		ValidationResult vr = employerRatingValidator.validate(dto, isRatingPresent);
		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.RE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Optional<EmployerRating> optEmployerRating = ratingBusiness.createEmployerRating(dto);
		if (optEmployerRating.isPresent()) {
			EmployerRatingDto employerRatingDto = employerRatingToDto(optEmployerRating.get());
			vr = employerRatingValidator.validate(employerRatingDto, false);
			if (!vr.validated) {
				logger.logValidationFailure(ValidationError.RE102 + vr.getErrorMsg());
				return new ValidateServiceResult<>(null, vr);
			}
			return new ValidateServiceResult<>(employerRatingDto, vr);
		}
		logger.logError(ValidationError.RE201 + "employerId: " + dto.getEmployerId() + " nurseId " + dto.getNurseId());
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<NurseRatingDto> createNurseRating(NurseRatingDto dto) {
		boolean isRatingPresent = ratingBusiness
				.getNurseRatingByEmployerAndNurseId(dto.getEmployerId(), dto.getNurseId()).isPresent() ? true : false;
		ValidationResult vr = nurseRatingValidator.validate(dto, isRatingPresent);

		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.RE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Optional<NurseRating> optNurseRating = ratingBusiness.createNurseRating(dto);
		if (optNurseRating.isPresent()) {
			NurseRatingDto nurseRatingDto = nurseRatingToDto(optNurseRating.get());
			vr = nurseRatingValidator.validate(dto, false);
			if (!vr.validated) {
				logger.logValidationFailure(ValidationError.RE102 + vr.getErrorMsg());
				return new ValidateServiceResult<>(null, vr);
			}
			return new ValidateServiceResult<>(nurseRatingDto, vr);
		}
		logger.logError(ValidationError.RE201 + "employerId: " + dto.getEmployerId() + " nurseId " + dto.getNurseId());
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<EmployerRatingDto> getEmployerRatingByEmployerAndNurseId(Long employerId,
			Long nurseId) {
		Optional<EmployerRating> optEmployerRating = ratingBusiness.getEmployerRatingByEmployerAndNurseId(employerId,
				nurseId);
		if (optEmployerRating.isPresent()) {
			EmployerRatingDto employerRatingDto = employerRatingToDto(optEmployerRating.get());
			ValidationResult vr = employerRatingValidator.validate(employerRatingDto, false);
			if (vr.validated) {
				return new ValidateServiceResult<>(employerRatingDto, vr);
			} else {
				logger.logValidationAndIdFailure(ValidationError.RE102 + vr.getErrorMsg(),
						ValidationError.RE103 + employerRatingDto.getId().toString());
				return new ValidateServiceResult<>(null, vr);
			}
		}
		logger.logError(ValidationError.RE201 + "employerId: " + employerId + " nurseId " + nurseId);
		return new ValidateServiceResult<>(null, null);
	}

	@Override
	public ValidateServiceResult<NurseRatingDto> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId) {
		Optional<NurseRating> optNurseRating = ratingBusiness.getNurseRatingByEmployerAndNurseId(employerId, nurseId);
		if (optNurseRating.isPresent()) {
			NurseRatingDto nurseRatingDto = nurseRatingToDto(optNurseRating.get());
			ValidationResult vr = nurseRatingValidator.validate(nurseRatingDto, false);
			if (vr.validated) {
				return new ValidateServiceResult<>(nurseRatingDto, vr);
			} else {
				logger.logValidationAndIdFailure(ValidationError.RE102 + vr.getErrorMsg(),
						ValidationError.RE103 + nurseRatingDto.getId().toString());
				return new ValidateServiceResult<>(null, vr);
			}
		} else {
			logger.logError(ValidationError.RE201 + "employerId: " + employerId + " nurseId " + nurseId);
			return new ValidateServiceResult<>(null, null);
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

	@Override
	public NurseRatingDto nurseRatingToDto(NurseRating nurseRating) {
		NurseRatingDto nurseRatingDto = new NurseRatingDto();
		nurseRatingDto.setId(nurseRating.getId());
		nurseRatingDto.setRating(nurseRating.getRating());
		nurseRatingDto.setEmployerId(nurseRating.getEmployer().getId());
		nurseRatingDto.setNurseId(nurseRating.getNurse().getId());
		return nurseRatingDto;
	}
}
