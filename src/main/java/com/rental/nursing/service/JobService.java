package com.rental.nursing.service;

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;

public interface JobService {
	JobDto createJob(JobDto dto);

	JobDto jobToDto(Job job);
}
