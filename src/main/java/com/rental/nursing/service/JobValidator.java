package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rental.nursing.dto.JobDto;

@Service
public class JobValidator {
	public ValidationResult validate(JobDto jobDto, boolean isEmployerPresent) {
		var errorMsg = new ArrayList<String>();
		if (!isEmployerPresent) {
			errorMsg.add(ValidationError.VE001 + ".employerEntity");
		}
		checkRequiredFields(jobDto, errorMsg);
		checkFieldLengths(jobDto, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	private void checkRequiredFields(JobDto jobDto, List<String> errorMsg) {
		if (jobDto.getName() == null || jobDto.getName().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".name");
		}
		if (jobDto.getInfo() == null || jobDto.getInfo().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".info");
		}
		if (jobDto.getCity() == null || jobDto.getCity().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".city");
		}
		if (jobDto.getStartTime() == null || jobDto.getStartTime().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".startTime");
		}
		if (jobDto.getEndTime() == null || jobDto.getEndTime().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".endTime");
		}
		if (jobDto.getSalary() == null || jobDto.getSalary().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".salary");
		}
		if (jobDto.getEmployerId() == null || jobDto.getEmployerId().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".employerId");
		}
		if (jobDto.getLatitude() == null || jobDto.getLatitude().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".latitude");
		}
		if (jobDto.getLongitude() == null || jobDto.getLongitude().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".longitude");
		}
	}

	private void checkFieldLengths(JobDto jobDto, List<String> errorMsg) {
		if (jobDto.getName() != null) {
			if (jobDto.getName().length() > 99) {
				errorMsg.add(ValidationError.VE002 + ".name");
			}
			if (jobDto.getName().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".name");
			}
		}
		if (jobDto.getInfo() != null) {
			if (jobDto.getInfo().length() > 1000) {
				errorMsg.add(ValidationError.VE002 + ".info");
			}
			if (jobDto.getInfo().length() < 10) {
				errorMsg.add(ValidationError.VE003 + ".info");
			}
		}
		if (jobDto.getCity() != null) {
			if (jobDto.getCity().length() > 40) {
				errorMsg.add(ValidationError.VE002 + ".city");
			}
			if (jobDto.getCity().length() < 2) {
				errorMsg.add(ValidationError.VE003 + ".city");
			}
		}
		if (jobDto.getStartTime() != null) {
			if (jobDto.getStartTime().isBefore(Instant.now())) {
				errorMsg.add(ValidationError.VE006 + ".cannotBeInPast");
			}
		}
		if (jobDto.getEndTime() != null && jobDto.getStartTime() != null) {
			if (jobDto.getEndTime().isBefore(jobDto.getStartTime())) {
				errorMsg.add(ValidationError.VE006 + ".cannotBeBeforeStartTime");
			}
		}
		if (jobDto.getLatitude() != null) {
			if (jobDto.getLatitude() > 90 || jobDto.getLatitude() < -90) {
				errorMsg.add(ValidationError.VE007 + ".latitude");
			}
		}
		if (jobDto.getLongitude() != null) {
			if (jobDto.getLongitude() > 180 || jobDto.getLongitude() < -180) {
				errorMsg.add(ValidationError.VE007 + ".longitude");
			}
		}
	}
}
