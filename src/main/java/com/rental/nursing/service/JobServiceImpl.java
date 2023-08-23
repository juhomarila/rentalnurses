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
import com.rental.nursing.business.JobBusiness;
import com.rental.nursing.business.NurseBusiness;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.entity.Employer;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;
import com.rental.nursing.exception.NotFoundException;
import com.rental.nursing.exception.SavingDataException;
import com.rental.nursing.exception.UpdateDataException;
import com.rental.nursing.exception.ValidationException;

import jakarta.persistence.EntityNotFoundException;

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

	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Override
	public JobDto createJob(JobDto dto) {
		ValidationResult vrInput = validator.validate(dto);
		if (!vrInput.validated) {
			logger.error(ValidationError.JE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}
		Optional<Employer> optEmp = employerBusiness.getEmployerById(dto.getEmployerId());
		if (optEmp.isPresent()) {
			Optional<Job> optJob = business.createJob(dto);
			if (optJob.isPresent()) {
				JobDto jobDto = jobToDto(optJob.get());
				ValidationResult vrOutput = validator.validate(jobDto);
				if (!vrOutput.validated) {
					logger.error(ValidationError.JE102 + vrOutput.getErrorMsg());
					throw new SavingDataException(vrOutput.getErrorMsg());
				}
				return jobDto;
			} else {
				throw new IllegalStateException(ValidationError.JE201);
			}
		} else {
			throw new EntityNotFoundException(ValidationError.JE207);
		}
	}

	@Override
	public JobDto updateJob(Long id, JobDto newJobDto) {
		ValidationResult vrInput = validator.validate(newJobDto);
		Optional<Employer> optEmployer = employerBusiness.getEmployerById(newJobDto.getEmployerId());
		if (!optEmployer.isPresent()) {
			throw new NotFoundException(ValidationError.VE001 + ".employerEntity");
		}
		if (!vrInput.validated) {
			logger.error(ValidationError.JE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}
		Optional<Job> optJob = business.getJobById(id);
		if (optJob.isPresent() && optJob.get().getNurse() == null
				&& optJob.get().getStartTime().isAfter(Instant.now())) {
			JobDto updatedJobDto = jobToDto(business.updateJob(optJob.get(), newJobDto).get());
			ValidationResult vrOutput = validator.validate(updatedJobDto);
			if (!vrOutput.isValidated()) {
				logger.error(ValidationError.JE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrOutput.getErrorMsg());
			}
			return updatedJobDto;

		} else {
			throw new NotFoundException(ValidationError.VE001 + ".jobEntity");
		}
	}

	@Override
	public JobDto updateOrRemoveJobNurse(Long id, NurseJobUpdateDto nurseJobUpdateDto) {
		Optional<Job> optJob = business.getJobById(id);
		Optional<Nurse> optNurse = nurseBusiness.getNurseById(nurseJobUpdateDto.getNurseId());
		ValidationResult vr = nurseJobUpdateValidator.validate(nurseJobUpdateDto, optJob, optNurse);

		if (!vr.isValidated()) {
			logger.error(ValidationError.JE104 + vr.getErrorMsg());
			throw new SavingDataException(vr.getErrorMsg());
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
			logger.error(ValidationError.JE205 + vr.getErrorMsg());
			throw new UpdateDataException(vr.getErrorMsg());
		}

		JobDto updatedJobDto = jobToDto(business.updateJob(job, jobDto).get());
		ValidationResult vrOutput = validator.validate(updatedJobDto);

		if (!vrOutput.isValidated()) {
			logger.error(ValidationError.JE102 + vrOutput.getErrorMsg());
			throw new SavingDataException(vrOutput.getErrorMsg());
		}

		return updatedJobDto;
	}

	@Override
	public JobDto getJobById(Long id) {
		Optional<Job> optJob = business.getJobById(id);
		if (optJob.isPresent()) {
			JobDto jobDto = jobToDto(optJob.get());
			ValidationResult vr = validator.validate(jobDto);
			if (vr.validated) {
				return jobDto;
			} else {
				logger.error(ValidationError.JE102 + vr.getErrorMsg());
				logger.error(ValidationError.JE103 + jobDto.getId().toString());
				throw new ValidationException(ValidationError.JE102 + vr.getErrorMsg());
			}
		} else {
			throw new NotFoundException(ValidationError.JE101 + id);
		}
	}

	@Override
	public void deleteJob(Long id) {
		Optional<Job> optJob = business.getJobById(id);
		if (optJob.isPresent()) {
			ValidationResult vr = validator.validate(jobToDto(optJob.get()));
			if (vr.validated) {
				if (optJob.get().getNurse() == null) {
					business.deleteJob(optJob.get());
				} else {
					vr.errorMsg.add(ValidationError.VE009 + ".nurseAssigned");
					logger.error(ValidationError.JE204 + vr.getErrorMsg());
					throw new UpdateDataException(vr.getErrorMsg());
				}
			}
		} else {
			throw new EntityNotFoundException(ValidationError.JE203);
		}
	}

	@Override
	public List<JobDto> getAllJobs() {
		List<JobDto> jobDtos = business.getAllJobs().stream().map(job -> jobToDto(job)).collect(Collectors.toList());
		List<JobDto> validatedJobDtos = new ArrayList<>();

		for (JobDto dto : jobDtos) {
			ValidationResult vr = validator.validate(dto);
			if (vr.validated) {
				validatedJobDtos.add(dto);
			} else {
				logger.error(ValidationError.EE102 + vr.getErrorMsg());
				logger.error(ValidationError.EE103 + dto.getId().toString());
			}
		}
		return validatedJobDtos;
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
