package com.rental.nursing.service;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseService {
	NurseDto createNurse(NurseDto dto);

	NurseDto nurseToDto(Nurse nurse);
}
