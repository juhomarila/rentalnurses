package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.EmployerBusiness;
import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.Job;
import com.rental.nursing.logging.NurseLogger;

@Service
public class EmployerServiceImpl implements EmployerService {
	@Autowired
	private EmployerBusiness business;

	@Autowired
	private EmployerValidator validator;

	private final NurseLogger logger;

	@Autowired
	public EmployerServiceImpl(NurseLogger logger) {
		this.logger = logger;
	}

	@Override
	public ValidateServiceResult<EmployerDto> createEmployer(EmployerDto dto) {
		ValidationResult vr = validator.validate(dto, true);
		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.EE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Optional<Employer> optEmployer = business.createEmployer(dto);
		if (optEmployer.isPresent()) {
			EmployerDto employerDto = employerToDto(optEmployer.get());
			return new ValidateServiceResult<>(employerDto, vr);
		}
		logger.logError(ValidationError.EE201);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<EmployerDto> updateEmployer(Long id, EmployerDto newEmployerDto) {
		boolean isEmployerPresent = business.getEmployerById(id).isPresent() ? true : false;
		ValidationResult vr = validator.validateForUpdate(newEmployerDto, isEmployerPresent, id);
		if (!vr.isValidated()) {
			logger.logValidationFailure(ValidationError.EE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Optional<Employer> optEmployer = business.getEmployerById(id);
		if (optEmployer.isPresent()) {
			Optional<Employer> optUpdatedEmployer = business.updateEmployer(optEmployer.get(), newEmployerDto);
			if (optUpdatedEmployer.isPresent()) {
				EmployerDto updatedEmployerDto = employerToDto(optUpdatedEmployer.get());
				return new ValidateServiceResult<>(updatedEmployerDto, vr);
			}
		}
		logger.logError(ValidationError.EE202);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<List<EmployerDto>> getEmployers() {
		List<EmployerDto> employerDtos = business.getEmployers().stream().map(emp -> employerToDto(emp))
				.collect(Collectors.toList());
		List<EmployerDto> validatedEmployerDtos = new ArrayList<>();

		for (EmployerDto dto : employerDtos) {
			ValidationResult vr = validator.validate(dto, false);
			if (vr.validated) {
				validatedEmployerDtos.add(dto);
			} else {
				logger.logValidationAndIdFailure(ValidationError.EE102 + vr.getErrorMsg(),
						ValidationError.EE103 + dto.getId().toString());
			}
		}
		return new ValidateServiceResult<>(validatedEmployerDtos, new ValidationResult());
	}

	@Override
	public ValidateServiceResult<EmployerDto> getEmployerById(Long id) {
		Optional<Employer> optEmployer = business.getEmployerById(id);
		ValidationResult vr = new ValidationResult();

		if (optEmployer.isEmpty()) {
			List<String> errorMsg = new ArrayList<>();
			errorMsg.add(ValidationError.VE001 + ".employerEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.EE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		EmployerDto employerDto = employerToDto(optEmployer.get());
		vr = validator.validate(employerDto, false);

		if (!vr.validated) {
			logger.logValidationAndIdFailure(ValidationError.EE102 + vr.getErrorMsg(),
					ValidationError.EE103 + employerDto.getId().toString());
			return new ValidateServiceResult<>(null, vr);
		}
		return new ValidateServiceResult<>(employerDto, vr);
	}

	@Override
	public ValidateServiceResult<Boolean> deleteEmployer(Long id) {
		Optional<Employer> optEmployer = business.getEmployerById(id);
		ValidationResult vr = new ValidationResult();

		if (optEmployer.isEmpty()) {
			List<String> errorMsg = new ArrayList<>();
			errorMsg.add(ValidationError.VE001 + ".employerEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.EE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(false, vr);
		}

		vr = validator.validate(employerToDto(optEmployer.get()), false);

		if (vr.validated) {
			business.deleteEmployer(optEmployer.get());
		}

		return new ValidateServiceResult<>(vr.validated, vr);
	}

	@Override
	public EmployerDto employerToDto(Employer employer) {
		EmployerDto employerDto = new EmployerDto();

		employerDto.setId(employer.getId());
		employerDto.setName(employer.getName());
		employerDto.setAddress(employer.getAddress());
		employerDto.setCity(employer.getCity());
		employerDto.setBic(employer.getBic());
		employerDto.setZipCode(employer.getZipCode());
		employerDto.setSector(employer.getSector());
		employerDto.setInfo(employer.getInfo());
		employerDto.setJoined(employer.getJoined());
		employerDto.setEdited(employer.getEdited());
		employerDto.setRating(calculateAverageRating(employer.getRatings()));

		setPastAndFutureJobs(employer.getJobs(), employerDto);

		if (employer.getJobs() != null) {
			List<Long> employedNurseIds = employer.getJobs().stream().filter(job -> job.getNurse() != null)
					.map(job -> job.getNurse().getId()).distinct().collect(Collectors.toList());

			employerDto.setEmployedNurses(employedNurseIds);
			employerDto.setHasOpenJobs(employer.getJobs().stream()
					.anyMatch(job -> job.getNurse() == null && job.getStartTime().isBefore(Instant.now())));
		}

		employerDto.setVerified(employer.isVerified());

		return employerDto;
	}

	private Double calculateAverageRating(List<EmployerRating> employerRatings) {
		if (employerRatings == null) {
			return 0.0; // No ratings yet, return 0
		}

		int totalRating = 0;

		for (EmployerRating employerRating : employerRatings) {
			totalRating += employerRating.getRating();
		}

		return (double) totalRating / employerRatings.size();
	}

	private void setPastAndFutureJobs(List<Job> jobs, EmployerDto employerDto) {
		if (jobs != null) {
			List<Long> pastJobs = jobs.stream().filter(job -> job.getEndTime().isBefore(Instant.now()))
					.map(j -> j.getId()).collect(Collectors.toList());

			List<Long> futureJobs = jobs.stream().filter(job -> job.getStartTime().isAfter(Instant.now()))
					.map(j -> j.getId()).collect(Collectors.toList());

			employerDto.setPastJobs(pastJobs);
			employerDto.setFutureJobs(futureJobs);
		}
	}
}
