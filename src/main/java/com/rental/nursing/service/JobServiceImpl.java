package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.EmployerBusiness;
import com.rental.nursing.business.JobBusiness;
import com.rental.nursing.business.NurseBusiness;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;
import com.rental.nursing.logging.NurseLogger;

@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobBusiness business;

	@Autowired
	private NurseBusiness nurseBusiness;

	@Autowired
	private EmployerBusiness employerBusiness;

	@Autowired
	private JobValidator validator;

	@Autowired
	private NurseJobUpdateValidator nurseJobUpdateValidator;

	private final NurseLogger logger;

	@Autowired
	public JobServiceImpl(NurseLogger logger) {
		this.logger = logger;
	}

	@Override
	public ValidateServiceResult<JobDto> createJob(JobDto dto) {
		boolean isEmployerPresent = employerBusiness.getEmployerById(dto.getEmployerId()).isPresent() ? true : false;
		ValidationResult vr = validator.validate(dto, isEmployerPresent);
		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.JE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}
		Optional<Employer> optEmp = employerBusiness.getEmployerById(dto.getEmployerId());
		if (optEmp.isPresent()) {
			Optional<Job> optJob = business.createJob(dto);
			if (optJob.isPresent()) {
				JobDto jobDto = jobToDto(optJob.get());
				vr = validator.validate(jobDto, true);
				if (!vr.validated) {
					logger.logValidationFailure(ValidationError.JE102 + vr.getErrorMsg());
					return new ValidateServiceResult<>(null, vr);
				}
				return new ValidateServiceResult<>(jobDto, vr);
			}
		}
		logger.logError(ValidationError.JE207);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<JobDto> updateJob(Long id, JobDto newJobDto) {
		boolean isEmployerPresent = employerBusiness.getEmployerById(newJobDto.getEmployerId()).isPresent() ? true
				: false;
		ValidationResult vr = validator.validate(newJobDto, isEmployerPresent);
		if (!vr.validated) {
			logger.logValidationFailure(ValidationError.JE102 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Optional<Job> optJob = business.getJobById(id);
		if (optJob.isPresent() && optJob.get().getNurse() == null
				&& optJob.get().getStartTime().isAfter(Instant.now())) {
			JobDto updatedJobDto = jobToDto(business.updateJob(optJob.get(), newJobDto).get());
			vr = validator.validate(updatedJobDto, true);
			if (!vr.isValidated()) {
				logger.logValidationFailure(ValidationError.JE102 + vr.getErrorMsg());
				return new ValidateServiceResult<>(null, vr);
			}
			return new ValidateServiceResult<>(updatedJobDto, vr);
		}
		logger.logError(ValidationError.JE202);
		return new ValidateServiceResult<>(null, vr);
	}

	@Override
	public ValidateServiceResult<JobDto> updateOrRemoveJobNurse(Long id, NurseJobUpdateDto nurseJobUpdateDto) {
		Optional<Job> optJob = business.getJobById(id);
		Optional<Nurse> optNurse = nurseBusiness.getNurseById(nurseJobUpdateDto.getNurseId());
		ValidationResult vr = nurseJobUpdateValidator.validate(nurseJobUpdateDto, optJob, optNurse);

		if (!vr.isValidated()) {
			logger.logValidationFailure(ValidationError.JE104 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		Job job = optJob.get();
		boolean nurseAssigned = job.getNurse() != null;

		JobDto jobDto = jobToDto(job);
		boolean removeNurse = nurseJobUpdateDto.getRemoveNurse();

		if (removeNurse && nurseAssigned && job.getNurse().getId().equals(nurseJobUpdateDto.getNurseId())) {
			jobDto.setNurseId(null);
		} else if (!removeNurse && (!nurseAssigned || job.getNurse().getId().equals(nurseJobUpdateDto.getNurseId()))) {
			jobDto.setNurseId(removeNurse ? null : nurseJobUpdateDto.getNurseId());
		} else {
			vr.errorMsg.add(ValidationError.VE009 + (removeNurse ? ".givenIdsDoesNotMatch" : ".idsDoesNotMatch"));
			logger.logForbidden(ValidationError.JE205 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		JobDto updatedJobDto = jobToDto(business.updateJob(job, jobDto).get());
		ValidationResult vrOutput = validator.validate(updatedJobDto, true);

		if (!vrOutput.isValidated()) {
			logger.logValidationFailure(ValidationError.JE102 + vrOutput.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		return new ValidateServiceResult<>(updatedJobDto, vr);
	}

	@Override
	public ValidateServiceResult<JobDto> getJobById(Long id) {
		Optional<Job> optJob = business.getJobById(id);
		ValidationResult vr = new ValidationResult();

		if (optJob.isEmpty()) {
			List<String> errorMsg = new ArrayList<>();
			errorMsg.add(ValidationError.VE001 + ".jobEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.JE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(null, vr);
		}

		JobDto jobDto = jobToDto(optJob.get());
		vr = validator.validate(jobDto, true);

		if (!vr.validated) {
			logger.logValidationAndIdFailure(ValidationError.JE102 + vr.getErrorMsg(),
					ValidationError.JE103 + jobDto.getId().toString());
			return new ValidateServiceResult<>(null, vr);
		}
		return new ValidateServiceResult<>(jobDto, vr);
	}

	@Override
	public ValidateServiceResult<Boolean> deleteJob(Long id) {
		Optional<Job> optJob = business.getJobById(id);
		ValidationResult vr = new ValidationResult();
		if (optJob.isEmpty()) {
			List<String> errorMsg = new ArrayList<>();
			errorMsg.add(ValidationError.VE001 + ".jobEntity");
			vr.setErrorMsg(errorMsg);

			logger.logValidationFailure(ValidationError.JE101 + vr.getErrorMsg());
			return new ValidateServiceResult<>(false, vr);
		}

		vr = validator.validate(jobToDto(optJob.get()), true);

		if (vr.validated) {
			if (optJob.get().getNurse() == null) {
				business.deleteJob(optJob.get());
			} else {
				vr.errorMsg.add(ValidationError.VE009 + ".nurseAssigned");
				logger.logForbidden(ValidationError.JE204 + vr.getErrorMsg());
				return new ValidateServiceResult<>(false, vr);
			}
		}
		return new ValidateServiceResult<>(vr.validated, vr);
	}

	@Override
	public ValidateServiceResult<List<JobDto>> getAllJobs() {
		List<JobDto> jobDtos = business.getAllJobs().stream().map(job -> jobToDto(job)).collect(Collectors.toList());
		List<JobDto> validatedJobDtos = new ArrayList<>();

		for (JobDto dto : jobDtos) {
			ValidationResult vr = validator.validate(dto, true);
			if (vr.validated) {
				validatedJobDtos.add(dto);
			} else {
				logger.logValidationAndIdFailure(ValidationError.JE102 + vr.getErrorMsg(),
						ValidationError.JE103 + dto.getId().toString());
			}
		}
		return new ValidateServiceResult<>(validatedJobDtos, new ValidationResult());
	}

	@Override
	public JobDto jobToDto(Job job) {
		JobDto jobDto = new JobDto();
		jobDto.setId(job.getId());
		jobDto.setName(job.getName());
		jobDto.setCity(job.getCity());
		jobDto.setInfo(job.getInfo());
		jobDto.setStartTime(job.getStartTime());
		jobDto.setEndTime(job.getEndTime());
		jobDto.setSalary(job.getSalary());
		jobDto.setEmployerId(job.getEmployer().getId());
		jobDto.setNurseId(job.getNurse() != null ? job.getNurse().getId() : null);
		jobDto.setLatitude(job.getLatitude());
		jobDto.setLongitude(job.getLongitude());
		jobDto.setOpen(job.getNurse() == null ? true : false);
		return jobDto;
	}
}
