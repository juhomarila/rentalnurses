package com.rental.nursing.business;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.EmployerRatingDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dao.NurseRatingDao;
import com.rental.nursing.dto.EmployerRatingDto;
import com.rental.nursing.dto.EmployerRatingUpdateDto;
import com.rental.nursing.dto.NurseRatingDto;
import com.rental.nursing.dto.NurseRatingUpdateDto;
import com.rental.nursing.entity.EmployerRating;
import com.rental.nursing.entity.NurseRating;

@Service
public class RatingBusinessImpl implements RatingBusiness {
	@Autowired
	private EmployerRatingDao employerRatingDao;

	@Autowired
	private NurseRatingDao nurseRatingDao;

	@Autowired
	private EmployerDao employerDao;

	@Autowired
	private NurseDao nurseDao;

	private static final Logger logger = LoggerFactory.getLogger(RatingBusinessImpl.class);

	@Override
	public Optional<EmployerRating> createEmployerRating(EmployerRatingDto dto) {
		try {
			EmployerRating employerRating = new EmployerRating();
			employerRating.setRating(dto.getRating());
			employerRating.setEmployer(employerDao.findById(dto.getEmployerId()).get());
			employerRating.setNurse(nurseDao.findById(dto.getNurseId()).get());
			employerRating.setComment(dto.getComment());
			employerRating.setAdded(Instant.now());
			employerRating = employerRatingDao.save(employerRating);
			return Optional.of(employerRating);
		} catch (Exception e) {
			logger.error(ErrorMessages.RATING_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<EmployerRating> updateEmployerRating(EmployerRating employerRating,
			EmployerRatingUpdateDto newDto) {
		try {
			employerRating.setComment(newDto.getComment());
			employerRating.setRating(newDto.getRating());
			employerRating.setEdited(Instant.now());
			employerRating = employerRatingDao.save(employerRating);
			return Optional.of(employerRating);
		} catch (Exception e) {
			logger.error(ErrorMessages.RATING_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public List<EmployerRating> getEmployerRatingsByEmployerId(Long employerId) {
		List<EmployerRating> ratings = employerRatingDao.findByEmployerId(employerId);
		return ratings;
	}

	@Override
	public Optional<EmployerRating> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId) {
		return Optional.ofNullable(employerRatingDao.findByEmployerIdAndNurseId(employerId, nurseId));
	}

	@Override
	public List<EmployerRating> getEmployerRatingsByNurseId(Long nurseId) {
		List<EmployerRating> ratings = employerRatingDao.findByNurseId(nurseId);
		return ratings;
	}

	@Override
	public Optional<EmployerRating> getEmployerRatingById(Long id) {
		return employerRatingDao.findById(id);
	}

	@Override
	public Optional<NurseRating> createNurseRating(NurseRatingDto dto) {
		try {
			NurseRating nurseRating = new NurseRating();
			nurseRating.setRating(dto.getRating());
			nurseRating.setEmployer(employerDao.findById(dto.getEmployerId()).get());
			nurseRating.setNurse(nurseDao.findById(dto.getNurseId()).get());
			nurseRating.setAdded(Instant.now());
			nurseRating = nurseRatingDao.save(nurseRating);
			return Optional.of(nurseRating);
		} catch (Exception e) {
			logger.error(ErrorMessages.RATING_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<NurseRating> updateNurseRating(NurseRating nurseRating, NurseRatingUpdateDto newDto) {
		try {
			nurseRating.setRating(newDto.getRating());
			nurseRating.setEdited(Instant.now());
			nurseRating = nurseRatingDao.save(nurseRating);
			return Optional.of(nurseRating);
		} catch (Exception e) {
			logger.error(ErrorMessages.RATING_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<NurseRating> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId) {
		return Optional.ofNullable(nurseRatingDao.findByEmployerIdAndNurseId(employerId, nurseId));
	}

	@Override
	public List<NurseRating> getNurseRatingsByNurseId(Long nurseId) {
		return nurseRatingDao.findByNurseId(nurseId);
	}

	@Override
	public List<NurseRating> getNurseRatingsByEmployerId(Long employerId) {
		return nurseRatingDao.findByEmployerId(employerId);
	}

}
