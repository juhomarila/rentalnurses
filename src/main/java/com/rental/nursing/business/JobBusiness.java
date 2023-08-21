package com.rental.nursing.business;

import java.util.Optional;

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;

public interface JobBusiness {
	Optional<Job> createJob(JobDto jobDto);
}
