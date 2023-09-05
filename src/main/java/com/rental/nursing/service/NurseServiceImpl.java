package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.JobBusiness;
import com.rental.nursing.business.NurseBusiness;
import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;
import com.rental.nursing.entity.NurseRating;
import com.rental.nursing.logging.NurseLogger;

@Service
public class NurseServiceImpl implements NurseService {
	@Autowired
	private NurseBusiness business;

	@Autowired
	private JobBusiness jobBusiness;

	@Autowired
	private NurseValidator validator;

	private final NurseLogger logger;

	@Autowired
	public NurseServiceImpl(NurseLogger logger) {
		this.logger = logger;
	}

	@Override
	public ValidateServiceResult<NurseDto> createNurse(NurseDto dto) {
		var vr = validator.validate(dto, true);
		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.NE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		var optNurse = business.createNurse(dto);
		if (optNurse.isPresent()) {
			var nurseDto = nurseToDto(optNurse.get());
			return new ValidateServiceResult<>(nurseDto, vr);
		}
		logger.logError(ValidationError.NE201);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<NurseDto> updateNurse(Long id, NurseDto newNurseDto) {
		boolean isNursePresent = business.getNurseById(id).isPresent() ? true : false;
		var vr = validator.validate(newNurseDto, isNursePresent);
		if (!vr.isValidated()) {
			logger.logValidationFailure(ValidationError.EE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		var optNurse = business.getNurseById(id);
		if (optNurse.isPresent()) {
			var optUpdatedNurse = business.updateNurse(optNurse.get(), newNurseDto);
			if (optUpdatedNurse.isPresent()) {
				var updatedNurseDto = nurseToDto(optUpdatedNurse.get());
				return new ValidateServiceResult<>(updatedNurseDto, vr);
			}
		}
		logger.logError(ValidationError.EE202);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<NurseDto> getNurseById(Long id) {
		var optNurse = business.getNurseById(id);
		var vr = new ValidationResult();

		if (optNurse.isEmpty()) {
			var errorMsg = new ArrayList<String>();
			errorMsg.add(ValidationError.VE001 + ".nurseEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.NE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		var nurseDto = nurseToDto(optNurse.get());
		vr = validator.validate(nurseDto, true);

		if (!vr.validated) {
			logger.logValidationAndIdFailure(ValidationError.NE102 + vr.getErrorMsg(),
					ValidationError.NE103 + nurseDto.getId().toString());
			return new ValidateServiceResult<>(null, vr);
		}
		return new ValidateServiceResult<>(nurseDto, vr);
	}

	@Override
	public ValidateServiceResult<List<NurseDto>> getAllNurses() {
		var nurseDtos = business.getAllNurses().stream().map(nurse -> nurseToDto(nurse)).collect(Collectors.toList());
		var validatedNurseDtos = new ArrayList<NurseDto>();

		for (NurseDto dto : nurseDtos) {
			var vr = validator.validate(dto, true);
			if (vr.validated) {
				validatedNurseDtos.add(dto);
			} else {
				logger.logValidationAndIdFailure(ValidationError.NE102 + vr.getErrorMsg(),
						ValidationError.NE103 + dto.getId().toString());
			}
		}
		return new ValidateServiceResult<>(validatedNurseDtos, new ValidationResult());
	}

	@Override
	public ValidateServiceResult<Boolean> deleteNurse(Long id) {
		var optNurse = business.getNurseById(id);
		var vr = new ValidationResult();
		var errorMsg = new ArrayList<String>();

		if (optNurse.isEmpty()) {
			errorMsg.add(ValidationError.VE001 + ".nurseEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.NE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(false, vr);
		}

		vr = validator.validate(nurseToDto(optNurse.get()), true);

		if (vr.validated) {
			boolean hasUnpaidJobs = jobBusiness.findJobsByNurseId(id).stream()
					.anyMatch(job -> job.getPayment().isPaid() == false);
			if (hasUnpaidJobs) {
				errorMsg.add(ValidationError.VE012 + ".nurse");
				vr.setErrorMsg(errorMsg);
				vr.setValidated(false);
				logger.logValidationFailure(ValidationError.NE104 + vr.getErrorMsg());
				return new ValidateServiceResult<>(false, vr);
			}
			jobBusiness.findJobsByNurseId(id).forEach(job -> {
				var jobDto = jobBusiness.jobToDto(job);
				jobDto.setNurseId(null);
				jobBusiness.updateJob(job, jobDto);
			});
			business.deleteNurse(optNurse.get());
		}
		return new ValidateServiceResult<>(true, vr);
	}

	@Override
	public NurseDto nurseToDto(Nurse nurse) {
		var nurseDto = new NurseDto();
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
			var employerIds = nurse.getJobs().stream().map(job -> job.getEmployer().getId()).distinct()
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
			var pastJobs = jobs.stream().filter(job -> job.getEndTime().isBefore(Instant.now())).map(j -> j.getId())
					.collect(Collectors.toList());
			var futureJobs = jobs.stream().filter(job -> job.getStartTime().isAfter(Instant.now())).map(j -> j.getId())
					.collect(Collectors.toList());
			nurseDto.setPastJobs(pastJobs);
			nurseDto.setFutureJobs(futureJobs);
		}
	}
}
