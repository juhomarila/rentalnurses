package com.rental.nursing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dao.NurseDao;
import com.rental.nursing.dto.NurseRatingDto;

@Service
public class NurseRatingValidator {
	@Autowired
	private NurseDao nurseDao;

	@Autowired
	private EmployerDao employerDao;

	public ValidationResult validate(NurseRatingDto dto, boolean isRatingPresent) {
		var errorMsg = new ArrayList<String>();
		checkRequiredEntitiesExist(dto, errorMsg, isRatingPresent);
		checkRequiredFields(dto, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	private void checkRequiredFields(NurseRatingDto dto, List<String> errorMsg) {
		if (dto.getRating() == null || dto.getRating().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".rating");
		}
		if (dto.getEmployerId() == null || dto.getEmployerId().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".employerId");
		}

		if (dto.getNurseId() == null || dto.getNurseId().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".nurseId");
		}
	}

	private void checkRequiredEntitiesExist(NurseRatingDto dto, List<String> errorMsg, boolean isRatingPresent) {
		var nurse = nurseDao.findById(dto.getNurseId()).orElse(null);
		var employer = employerDao.findById(dto.getEmployerId()).orElse(null);

		if (isRatingPresent) {
			errorMsg.add(ValidationError.VE004 + ".exists");
		}
		if (nurse != null) {
			if (!nurse.isVerified()) {
				errorMsg.add(ValidationError.VE005 + ".notVerifiedNurse");
			}
		} else {
			errorMsg.add(ValidationError.VE001 + ".nurseEntity");
		}
		if (employer != null) {
			if (!employer.isVerified()) {
				errorMsg.add(ValidationError.VE005 + ".notVerifiedEmployer");
			}
		} else {
			errorMsg.add(ValidationError.VE001 + ".employerEntity");
		}
	}
}
