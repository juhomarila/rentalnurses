package com.rental.nursing.business;

import java.util.Optional;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseBusiness {
	Optional<Nurse> createNurse(NurseDto nurseDto);

	Optional<Nurse> getNurseById(Long id);
}
