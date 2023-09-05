package com.rental.nursing.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rental.nursing.dao.EmployerDao;
import com.rental.nursing.dto.EmployerDto;

@Service
public class EmployerValidator {
	@Autowired
	private EmployerDao employerDao;

	public ValidationResult validate(EmployerDto employerDto, boolean isEmployerPresent) {
		var errorMsg = new ArrayList<String>();
		if (isEmployerPresent) {
			if (employerDao.existsByName(employerDto.getName())) {
				errorMsg.add(ValidationError.VE011 + ".name");
			}
			if (employerDao.existsByBic(employerDto.getBic())) {
				errorMsg.add(ValidationError.VE011 + ".bic");
			}
		}
		checkRequiredFields(employerDto, errorMsg);
		checkFieldLengths(employerDto, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	public ValidationResult validateForUpdate(EmployerDto employerDto, boolean isEmployerPresent,
			Long existingEmployerId) {
		var errorMsg = new ArrayList<String>();
		if (!isEmployerPresent) {
			errorMsg.add(ValidationError.VE001 + ".employerEntity");
		}
		if (isEmployerPresent) {
			var employerFromDb = employerDao.findById(existingEmployerId);
			if (employerFromDb.isPresent() && !employerFromDb.get().getName().equals(employerDto.getName())
					&& employerDao.existsByName(employerDto.getName())) {
				errorMsg.add(ValidationError.VE011 + ".name");
			}
			if (employerFromDb.isPresent() && !employerFromDb.get().getBic().equals(employerDto.getBic())
					&& employerDao.existsByBic(employerDto.getBic())) {
				errorMsg.add(ValidationError.VE011 + ".bic");
			}
		}
		checkRequiredFields(employerDto, errorMsg);
		checkFieldLengths(employerDto, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	private void checkRequiredFields(EmployerDto employerDto, List<String> errorMsg) {
		if (employerDto.getName() == null || employerDto.getName().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".name");
		}
		if (employerDto.getBic() == null || employerDto.getBic().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".bic");
		}
		if (employerDto.getAddress() == null || employerDto.getAddress().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".address");
		}
		if (employerDto.getCity() == null || employerDto.getCity().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".city");
		}
		if (employerDto.getZipCode() == null || employerDto.getZipCode().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".zipCode");
		}
		if (employerDto.getSector() == null || employerDto.getSector().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".sector");
		}
	}

	private void checkFieldLengths(EmployerDto employerDto, List<String> errorMsg) {
		if (employerDto.getName() != null) {
			if (employerDto.getName().length() > 99) {
				errorMsg.add(ValidationError.VE002 + ".name");
			}
			if (employerDto.getName().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".name");
			}
		}
		if (employerDto.getAddress() != null) {
			if (employerDto.getAddress().length() > 99) {
				errorMsg.add(ValidationError.VE002 + ".address");
			}
			if (employerDto.getAddress().length() < 5) {
				errorMsg.add(ValidationError.VE003 + ".address");
			}
		}
		if (employerDto.getCity() != null) {
			if (employerDto.getCity().length() > 40) {
				errorMsg.add(ValidationError.VE002 + ".city");
			}
			if (employerDto.getCity().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".city");
			}
		}
		if (employerDto.getBic() != null) {
			if (!isValidBic(employerDto.getBic())) {
				errorMsg.add(ValidationError.VE008 + ".bic");
			}
		}
		if (employerDto.getZipCode() != null) {
			if (employerDto.getZipCode().toString().length() > 5) {
				errorMsg.add(ValidationError.VE002 + ".zipCode");
			}
			if (employerDto.getZipCode().toString().length() < 5) {
				errorMsg.add(ValidationError.VE003 + ".zipCode");
			}
		}
		if (employerDto.getSector() != null) {
			if (employerDto.getSector().length() > 100) {
				errorMsg.add(ValidationError.VE002 + ".sector");
			}
			if (employerDto.getSector().length() < 5) {
				errorMsg.add(ValidationError.VE003 + ".sector");
			}
		}
		if (employerDto.getInfo() != null) {
			if (employerDto.getInfo().length() > 1000) {
				errorMsg.add(ValidationError.VE002 + ".info");
			}
			if (employerDto.getInfo().length() < 10) {
				errorMsg.add(ValidationError.VE003 + ".info");
			}
		}
	}

	public static boolean isValidBic(String yTunnus) {
		if (yTunnus == null || !yTunnus.matches("\\d{7}-\\d")) {
			return false;
		}

		String[] parts = yTunnus.split("-");
		String digitsPart = parts[0];
		int checksumDigit = Character.getNumericValue(parts[1].charAt(0));

		int[] weights = { 7, 9, 10, 5, 8, 4, 2 };
		int sum = 0;

		for (int i = 0; i < digitsPart.length(); i++) {
			int digit = Character.getNumericValue(digitsPart.charAt(i));
			sum += digit * weights[i];
		}

		int remainder = sum % 11;
		int calculatedChecksumDigit = (remainder == 0) ? 0 : (11 - remainder);

		return checksumDigit == calculatedChecksumDigit;
	}
}
