package com.rental.nursing.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.business.JobBusiness;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;
import com.rental.nursing.exception.SavingDataException;

@Service
public class JobServiceImpl implements JobService {
	@Autowired
	private JobBusiness business;

	@Autowired
	private JobValidator validator;

	private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Override
	public JobDto createJob(JobDto dto) {
		ValidationResult vrInput = validator.validate(dto);
		if (!vrInput.validated) {
			logger.error(ValidationError.JE102 + vrInput.getErrorMsg());
			throw new SavingDataException(vrInput.getErrorMsg());
		}

		Optional<Job> optJob = business.createJob(dto);
		if (optJob.isPresent()) {
			JobDto jobDto = jobToDto(optJob.get());
			ValidationResult vrOutput = validator.validate(jobDto);
			if (!vrOutput.validated) {
				logger.error(ValidationError.JE102 + vrOutput.getErrorMsg());
				throw new SavingDataException(vrInput.getErrorMsg());
			}
			return jobDto;
		} else {
			throw new IllegalStateException(ValidationError.JE201);
		}
	}

	@Override
	public JobDto jobToDto(Job job) {
		JobDto jobDto = new JobDto();
		jobDto.setId(job.getId());
		jobDto.setName(job.getName());
		jobDto.setCity(job.getCity());
		jobDto.setInfo(job.getInfo());
		return jobDto;
	}

}
