package com.rental.nursing.business;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;

@Service
public class EmployerBusinessImpl implements EmployerBusiness {
	@Autowired
	private EmployerDao employerDao;

	private static final Logger logger = LoggerFactory.getLogger(EmployerBusinessImpl.class);

	@Override
	public Optional<Employer> createEmployer(EmployerDto dto) {
		try {
			Employer employer = new Employer();
			employer.setJoined(Instant.now());
			employer = saveEmployer(employer, dto);
			return Optional.of(employer);
		} catch (Exception e) {
			logger.error(ErrorMessages.EMPLOYER_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Employer> updateEmployer(Employer employer, EmployerDto dto) {
		try {
			employer.setEdited(Instant.now());
			employer = saveEmployer(employer, dto);
			return Optional.of(employer);
		} catch (Exception e) {
			logger.error(ErrorMessages.EMPLOYER_UPDATE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public List<Employer> getEmployers() {
		return employerDao.findAll();
	}

	@Override
	public Optional<Employer> getEmployerById(Long id) {
		return employerDao.findById(id);
	}

	@Override
	public void deleteEmployer(Employer employer) {
		employerDao.delete(employer);
	}

	private Employer saveEmployer(Employer employer, EmployerDto dto) {
		employer.setName(dto.getName());
		employer.setAddress(dto.getAddress());
		employer.setCity(dto.getCity());
		employer.setBic(dto.getBic());
		employer.setZipCode(dto.getZipCode());
		employer.setSector(dto.getSector());
		employer.setInfo(dto.getInfo());
		employer.setVerified(dto.isVerified());
		employerDao.save(employer);
		return employer;
	}
}
