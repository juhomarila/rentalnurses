package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.entity.Job;

public interface JobService {
	JobDto createJob(JobDto dto);

	JobDto updateJob(Long id, JobDto newJobDto);

	JobDto updateOrRemoveJobNurse(Long id, NurseJobUpdateDto nurseJobUpdateDto);

	JobDto getJobById(Long id);

	List<JobDto> getAllJobs();

	void deleteJob(Long id);

	JobDto jobToDto(Job job);
}
