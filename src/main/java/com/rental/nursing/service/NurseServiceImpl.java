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

import com.rental.nursing.business.NurseBusiness;
import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;
import com.rental.nursing.entity.NurseRating;
import com.rental.nursing.exception.SavingDataException;

@Service
public class NurseServiceImpl implements NurseService {
	@Autowired
	private NurseBusiness business;

	@Autowired
	private NurseValidator validator;

	private static final Logger logger = LoggerFactory.getLogger(NurseServiceImpl.class);

	@Override
	public NurseDto createNurse(NurseDto dto) {
		ValidationResult vrInput = validator.validate(dto);
		if (!vrInput.validated) {
			logger.error(ValidationError.NE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}

		Optional<Nurse> optNurse = business.createNurse(dto);
		if (optNurse.isPresent()) {
			NurseDto nurseDto = nurseToDto(optNurse.get());
			ValidationResult vrOutput = validator.validate(nurseDto);
			if (!vrOutput.validated) {
				logger.error(ValidationError.NE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrOutput.getErrorMsg());
			}
			return nurseDto;
		} else {
			throw new IllegalStateException(ValidationError.NE201);
		}
	}

	@Override
	public List<NurseDto> getAllNurses() {
		List<NurseDto> nurseDtos = business.getAllNurses().stream().map(nurse -> nurseToDto(nurse))
				.collect(Collectors.toList());
		List<NurseDto> validatedNurseDtos = new ArrayList<>();

		for (NurseDto dto : nurseDtos) {
			ValidationResult vr = validator.validate(dto);
			if (vr.validated) {
				validatedNurseDtos.add(dto);
			} else {
				logger.error(ValidationError.NE102 + vr.getErrorMsg());
				logger.error(ValidationError.NE103 + dto.getId().toString());
			}
		}
		return validatedNurseDtos;
	}

	@Override
	public NurseDto nurseToDto(Nurse nurse) {
		NurseDto nurseDto = new NurseDto();
		nurseDto.setId(nurse.getId());
		nurseDto.setFirstName(nurse.getFirstName());
		nurseDto.setLastName(nurse.getLastName());
		nurseDto.setCity(nurse.getCity());
		nurseDto.setZipCode(nurse.getZipCode());
		nurseDto.setSector(nurse.getSector());
		nurseDto.setInfo(nurse.getInfo());
		nurseDto.setJoined(nurse.getJoined());
		nurseDto.setEdited(nurse.getEdited());
		nurseDto.setLastEmployment(nurse.getLastEmployment());
		nurseDto.setRating(calculateAverageRating(nurse.getRatings()));
		setPastAndFutureJobs(nurse.getJobs(), nurseDto);
		if (nurse.getJobs() != null) {
			List<Long> employerIds = nurse.getJobs().stream().map(job -> job.getEmployer().getId()).distinct()
					.collect(Collectors.toList());
			nurseDto.setEmployers(employerIds);
			nurseDto.setDoingJob(nurse.getJobs().stream().anyMatch(
					job -> job.getStartTime().isBefore(Instant.now()) && job.getEndTime().isAfter(Instant.now())));
		}
		nurseDto.setVerified(nurse.isVerified());
		return nurseDto;
	}

	private Double calculateAverageRating(List<NurseRating> nurseRatings) {
		if (nurseRatings == null) {
			return 0.0; // No ratings yet, return 0
		}
		int totalRating = 0;
		for (NurseRating nurseRating : nurseRatings) {
			totalRating += nurseRating.getRating();
		}

		return (double) totalRating / nurseRatings.size();
	}

	private void setPastAndFutureJobs(List<Job> jobs, NurseDto nurseDto) {
		if (jobs != null) {
			List<Long> pastJobs = jobs.stream().filter(job -> job.getEndTime().isBefore(Instant.now()))
					.map(j -> j.getId()).collect(Collectors.toList());
			List<Long> futureJobs = jobs.stream().filter(job -> job.getStartTime().isAfter(Instant.now()))
					.map(j -> j.getId()).collect(Collectors.toList());
			nurseDto.setPastJobs(pastJobs);
			nurseDto.setFutureJobs(futureJobs);
		}
	}
}
