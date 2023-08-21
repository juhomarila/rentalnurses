package com.rental.nursing.service;

import java.util.List;

import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;

public interface EmployerService {
	EmployerDto createEmployer(EmployerDto dto);

	EmployerDto updateEmployer(Long id, EmployerDto newEmployer);

	List<EmployerDto> getEmployers();

	EmployerDto getEmployerById(Long id);

	void deleteEmployer(Long id);

	EmployerDto employerToDto(Employer employer);
}
