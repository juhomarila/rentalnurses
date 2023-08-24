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
import com.rental.nursing.dto.NurseRatingDto;
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
	public Optional<EmployerRating> getEmployerRatingByEmployerAndNurseId(Long employerId, Long nurseId) {
		List<EmployerRating> ratings = employerRatingDao.findByEmployerIdAndNurseId(employerId, nurseId);
		return ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0));
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
	public Optional<NurseRating> getNurseRatingByEmployerAndNurseId(Long employerId, Long nurseId) {
		List<NurseRating> ratings = nurseRatingDao.findByEmployerIdAndNurseId(employerId, nurseId);
		return ratings.isEmpty() ? Optional.empty() : Optional.of(ratings.get(0));
	}

	@Override
	public List<EmployerRating> getEmployerRatingsByEmployerId(Long employerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmployerRating> getEmployerRatingsByNurseId(Long nurseId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<EmployerRating> getEmployerRatingById(Long id) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<EmployerRating> getEmployerRatingByEmployerId(Long employerId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Optional<EmployerRating> getEmployerRatingByNurseId(Long nurseId) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}
}
