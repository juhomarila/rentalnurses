package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.EmployerBusiness;
import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.Job;
import com.rental.nursing.exception.EmployerNotFoundException;
import com.rental.nursing.exception.SavingDataException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployerServiceImpl implements EmployerService {
	@Autowired
	private EmployerBusiness business;

	@Autowired
	private EmployerValidator validator;

	private static final Logger logger = LoggerFactory.getLogger(EmployerServiceImpl.class);

	@Override
	public EmployerDto createEmployer(EmployerDto dto) {
		ValidationResult vrInput = validator.validate(dto);
		if (!vrInput.validated) {
			logger.error(ValidationError.EE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}

		Optional<Employer> optEmployer = business.createEmployer(dto);
		if (optEmployer.isPresent()) {
			EmployerDto employerDto = employerToDto(optEmployer.get());
			ValidationResult vrOutput = validator.validate(employerDto);
			if (!vrOutput.validated) {
				logger.error(ValidationError.EE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrInput.getErrorMsg());
			}
			return employerDto;
		} else {
			throw new IllegalStateException(ValidationError.EE201);
		}
	}

	@Override
	public EmployerDto updateEmployer(Long id, EmployerDto newEmployer) {
		ValidationResult vrInput = validator.validate(newEmployer);
		if (!vrInput.validated) {
			logger.error(ValidationError.EE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}
		Optional<Employer> optEmployer = business.getEmployerById(id);
		if (optEmployer.isPresent()) {
			EmployerDto updatedEmployerDto = employerToDto(
					business.updateEmployer(optEmployer.get(), newEmployer).get());
			ValidationResult vrOutput = validator.validate(updatedEmployerDto);
			if (!vrOutput.validated) {
				logger.error(ValidationError.EE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrInput.getErrorMsg());
			}
			return updatedEmployerDto;
		} else {
			throw new EntityNotFoundException(ValidationError.EE202);
		}

	}

	@Override
	public List<EmployerDto> getEmployers() {
		List<EmployerDto> employerDtos = business.getEmployers().stream().map(emp -> employerToDto(emp))
				.collect(Collectors.toList());
		List<EmployerDto> validatedEmployerDtos = new ArrayList<>();

		for (EmployerDto dto : employerDtos) {
			ValidationResult vr = validator.validate(dto);
			if (vr.validated) {
				validatedEmployerDtos.add(dto);
			} else {
				logger.error(ValidationError.EE102 + vr.getErrorMsg());
				logger.error(ValidationError.EE103 + dto.getId().toString());
			}
		}
		return validatedEmployerDtos;
	}

	@Override
	public EmployerDto getEmployerById(Long id) {
		Optional<Employer> optEmployer = business.getEmployerById(id);
		if (optEmployer.isPresent()) {
			EmployerDto employerDto = employerToDto(optEmployer.get());
			ValidationResult vr = validator.validate(employerDto);
			if (vr.validated) {
				return employerDto;
			} else {
				logger.error(ValidationError.EE102 + vr.getErrorMsg());
				logger.error(ValidationError.EE103 + employerDto.getId().toString());
			}
		} else {
			throw new EmployerNotFoundException(ValidationError.EE101 + id);
		}
		return null;
	}

	@Override
	public void deleteEmployer(Long id) {
		Optional<Employer> optEmployer = business.getEmployerById(id);
		if (optEmployer.isPresent()) {
			business.deleteEmployer(optEmployer.get());
		} else {
			throw new EntityNotFoundException(ValidationError.EE203);
		}
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
		employerDto.setLastEmployment(employer.getLastEmployment());
		employerDto.setRating(calculateAverageRating(employer.getRatings()));
		setPastAndFutureJobs(employer.getJobs(), employerDto);
		if (employer.getJobs() != null) {
			employer.getJobs().forEach(job -> {
				if (!employerDto.getEmployedNurses().contains(job.getNurse())) {
					employerDto.getEmployedNurses().add(job.getNurse());
				}
			});
			employerDto.setHasOpenJobs(employer.getJobs().stream().anyMatch(job -> job.getNurse() == null));
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
			List<Job> pastJobs = jobs.stream().filter(job -> job.getDate().isBefore(Instant.now()))
					.collect(Collectors.toList());
			List<Job> futureJobs = jobs.stream().filter(job -> job.getDate().isAfter(Instant.now()))
					.collect(Collectors.toList());
			employerDto.setPastJobs(pastJobs);
			employerDto.setFutureJobs(futureJobs);
		}
	}
}
