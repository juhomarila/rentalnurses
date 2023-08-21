package com.rental.nursing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rental.nursing.dto.NurseRatingDto;

@Service
public class NurseRatingValidator {
	public ValidationResult validate(NurseRatingDto dto) {
		List<String> errorMsg = new ArrayList<>();
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
}
