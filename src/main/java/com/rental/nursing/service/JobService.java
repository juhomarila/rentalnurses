package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.entity.Job;

public interface JobService {
	ValidateServiceResult<JobDto> createJob(JobDto dto);

	ValidateServiceResult<JobDto> updateJob(Long id, JobDto newJobDto);

	ValidateServiceResult<JobDto> updateOrRemoveJobNurse(Long id, NurseJobUpdateDto nurseJobUpdateDto);

	ValidateServiceResult<JobDto> getJobById(Long id);

	ValidateServiceResult<List<JobDto>> getAllJobs();

	ValidateServiceResult<Boolean> deleteJob(Long id);

	JobDto jobToDto(Job job);
}
