package com.rental.nursing.business;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.JobDao;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;

@Service
public class JobBusinessImpl implements JobBusiness {
	@Autowired
	private JobDao jobDao;

	@Autowired
	private EmployerDao employerDao;

	private static final Logger logger = LoggerFactory.getLogger(JobBusinessImpl.class);

	@Override
	public Optional<Job> createJob(JobDto dto) {
		try {
			Job job = new Job();
			job.setAdded(Instant.now());
			job = saveJob(job, dto);
			return Optional.of(job);
		} catch (Exception e) {
			logger.error(EmployerErrorMessages.EMPLOYER_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	private Job saveJob(Job job, JobDto dto) {
		job.setName(dto.getName());
		job.setInfo(dto.getInfo());
		job.setCity(dto.getCity());
		job.setDate(dto.getDate());
		job.setSalary(dto.getSalary());
		job.setEmployer(employerDao.findById(dto.getEmployerId()).get());
		job.setLatitude(dto.getLatitude());
		job.setLongitude(dto.getLongitude());
		jobDao.save(job);
		return job;
	}
}
