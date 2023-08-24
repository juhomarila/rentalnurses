package com.rental.nursing.business;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.JobDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dto.JobDto;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Payment;

@Service
public class JobBusinessImpl implements JobBusiness {
	@Autowired
	private JobDao jobDao;

	@Autowired
	private EmployerDao employerDao;

	@Autowired
	private NurseDao nurseDao;

	private static final Logger logger = LoggerFactory.getLogger(JobBusinessImpl.class);

	@Override
	public Optional<Job> createJob(JobDto dto) {
		try {
			Job job = new Job();
			job.setAdded(Instant.now());
			job = saveJob(job, dto);
			return Optional.of(job);
		} catch (Exception e) {
			logger.error(ErrorMessages.JOB_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Job> updateJob(Job job, JobDto dto) {
		try {
			job.setEdited(Instant.now());
			job = saveJob(job, dto);
			return Optional.of(job);
		} catch (Exception e) {
			logger.error(ErrorMessages.JOB_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Job> getJobById(Long id) {
		return jobDao.findById(id);
	}

	@Override
	public void deleteJob(Job job) {
		jobDao.delete(job);
	}

	@Override
	public List<Job> getAllJobs() {
		return jobDao.findAll();
	}

	private Job saveJob(Job job, JobDto dto) {
		job.setName(dto.getName());
		job.setInfo(dto.getInfo());
		job.setCity(dto.getCity());
		job.setStartTime(dto.getStartTime());
		job.setEndTime(dto.getEndTime());
		job.setSalary(dto.getSalary());
		job.setEmployer(employerDao.findById(dto.getEmployerId()).get());
		job.setNurse((dto.getNurseId() != null && nurseDao.findById(dto.getNurseId()).get() != null)
				? nurseDao.findById(dto.getNurseId()).get()
				: null);
		job.setLatitude(dto.getLatitude());
		job.setLongitude(dto.getLongitude());
		Payment payment = new Payment();
		payment.setJob(job);
		payment.setPaid(false);
		job.setPayment(payment);
		jobDao.save(job);
		return job;
	}
}
