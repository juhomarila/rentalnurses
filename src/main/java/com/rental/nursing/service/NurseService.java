package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.NurseDto;
import com.rental.nursing.entity.Nurse;

public interface NurseService {
	ValidateServiceResult<NurseDto> createNurse(NurseDto dto);

	ValidateServiceResult<NurseDto> getNurseById(Long id);

	ValidateServiceResult<List<NurseDto>> getAllNurses();

	NurseDto nurseToDto(Nurse nurse);
}
