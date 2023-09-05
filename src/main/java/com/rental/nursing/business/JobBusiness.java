package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;

public interface JobBusiness {
	Optional<Job> createJob(JobDto jobDto);

	Optional<Job> updateJob(Job job, JobDto dto);

	Optional<Job> getJobById(Long id);

	List<Job> getAllJobs();

	List<Job> findJobsByEmployerId(Long id);

	List<Job> findJobsByNurseId(Long id);

	void deleteJob(Job job);

	public JobDto jobToDto(Job job);
}
