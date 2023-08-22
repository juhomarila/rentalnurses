package com.rental.nursing.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.rental.nursing.dto.NurseJobUpdateDto;
import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;

@Service
public class NurseJobUpdateValidator {

	public ValidationResult validate(NurseJobUpdateDto dto, Optional<Job> job, Optional<Nurse> nurse) {
		List<String> errorMsg = new ArrayList<>();
		checkRequiredFields(dto, errorMsg);
		checkNurseAndJob(job, nurse, errorMsg);

		if (!errorMsg.isEmpty()) {
			return new ValidationResult(errorMsg, false);
		}
		return new ValidationResult(true);
	}

	private void checkRequiredFields(NurseJobUpdateDto dto, List<String> errorMsg) {
		if (dto.getNurseId() == null || dto.getNurseId().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".nurseId");
		}
		if (dto.getRemoveNurse() == null || dto.getRemoveNurse().toString().length() < 1) {
			errorMsg.add(ValidationError.VE001 + ".removeNurse");
		}
	}

	private void checkNurseAndJob(Optional<Job> job, Optional<Nurse> nurse, List<String> errorMsg) {
		if (!job.isPresent()) {
			errorMsg.add(ValidationError.VE010 + ".job");
		}
		if (!nurse.isPresent()) {
			errorMsg.add(ValidationError.VE010 + ".nurse");
		}
		if (job.isPresent() && job.get().getStartTime().isBefore(Instant.now())) {
			errorMsg.add(ValidationError.VE006 + ".jobHasStarted");
		}
	}
}
