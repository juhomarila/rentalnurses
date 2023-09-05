package com.rental.nursing.business;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerRatingDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

@Service
public class NurseBusinessImpl implements NurseBusiness {

	@Autowired
	private NurseDao nurseDao;

	@Autowired
	private EmployerRatingDao employerRatingDao;

	private static final Logger logger = LoggerFactory.getLogger(NurseBusinessImpl.class);

	@Override
	public Optional<Nurse> createNurse(NurseDto dto) {
		try {
			Nurse nurse = new Nurse();
			nurse.setJoined(Instant.now());
			nurse = saveNurse(nurse, dto);
			return Optional.of(nurse);
		} catch (Exception e) {
			logger.error(ErrorMessages.NURSE_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Nurse> updateNurse(Nurse nurse, NurseDto newNurseDto) {
		try {
			nurse.setEdited(Instant.now());
			nurse = saveNurse(nurse, newNurseDto);
			return Optional.of(nurse);
		} catch (Exception e) {
			logger.error(ErrorMessages.NURSE_SAVE_ERROR + e.getMessage(), e);
			return Optional.empty();
		}
	}

	@Override
	public Optional<Nurse> getNurseById(Long id) {
		return nurseDao.findById(id);
	}

	@Override
	public List<Nurse> getAllNurses() {
		return nurseDao.findAll();
	}

	@Override
	public void deleteNurse(Nurse nurse) {
		var employerRatings = employerRatingDao.findByNurseId(nurse.getId());
		if (employerRatings.size() > 0) {
			employerRatings.forEach(rating -> {
				employerRatingDao.deleteById(rating.getId());
			});
		}
		nurseDao.delete(nurse);
	}

	private Nurse saveNurse(Nurse nurse, NurseDto dto) {
		nurse.setFirstName(dto.getFirstName());
		nurse.setLastName(dto.getLastName());
		nurse.setCity(dto.getCity());
		nurse.setZipCode(dto.getZipCode());
		nurse.setSector(dto.getSector());
		nurse.setInfo(dto.getInfo());
		nurse.setVerified(dto.isVerified());
		nurseDao.save(nurse);
		return nurse;
	}
}
