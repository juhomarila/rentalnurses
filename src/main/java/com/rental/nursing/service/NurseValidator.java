package com.rental.nursing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rental.nursing.dto.NurseDto;

@Service
public class NurseValidator {
	public ValidationResult validate(NurseDto nurseDto) {
		List<String> errorMsg = new ArrayList<>();
		checkRequiredFields(nurseDto, errorMsg);
		checkFieldLengths(nurseDto, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	private void checkRequiredFields(NurseDto nurseDto, List<String> errorMsg) {
		if (nurseDto.getFirstName() == null || nurseDto.getFirstName().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".firstname");
		}
		if (nurseDto.getLastName() == null || nurseDto.getLastName().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".lastname");
		}
		if (nurseDto.getCity() == null || nurseDto.getCity().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".city");
		}
		if (nurseDto.getZipCode() == null || nurseDto.getZipCode().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".zipCode");
		}
		if (nurseDto.getSector() == null || nurseDto.getSector().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".sector");
		}
	}

	private void checkFieldLengths(NurseDto nurseDto, List<String> errorMsg) {
		if (nurseDto.getFirstName() != null) {
			if (nurseDto.getFirstName().length() > 99) {
				errorMsg.add(ValidationError.VE002 + ".firstname");
			}
			if (nurseDto.getFirstName().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".firstname");
			}
		}
		if (nurseDto.getLastName() != null) {
			if (nurseDto.getLastName().length() > 99) {
				errorMsg.add(ValidationError.VE002 + ".lastname");
			}
			if (nurseDto.getLastName().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".lastname");
			}
		}
		if (nurseDto.getCity() != null) {
			if (nurseDto.getCity().length() > 40) {
				errorMsg.add(ValidationError.VE002 + ".city");
			}
			if (nurseDto.getCity().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".city");
			}
		}
		if (nurseDto.getZipCode() != null) {
			if (nurseDto.getZipCode().toString().length() > 5) {
				errorMsg.add(ValidationError.VE002 + ".zipCode");
			}
			if (nurseDto.getZipCode().toString().length() < 5) {
				errorMsg.add(ValidationError.VE003 + ".zipCode");
			}
		}
		if (nurseDto.getSector() != null) {
			if (nurseDto.getSector().length() > 100) {
				errorMsg.add(ValidationError.VE002 + ".sector");
			}
			if (nurseDto.getSector().length() < 5) {
				errorMsg.add(ValidationError.VE003 + ".sector");
			}
		}
		if (nurseDto.getInfo() != null) {
			if (nurseDto.getInfo().length() > 1000) {
				errorMsg.add(ValidationError.VE002 + ".info");
			}
			if (nurseDto.getInfo().length() < 10) {
				errorMsg.add(ValidationError.VE003 + ".info");
			}
		}
	}
}
