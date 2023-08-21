package com.rental.nursing.business;

import java.util.List;
import java.util.Optional;

import com.rental.nursing.dto.EmployerDto;
import com.rental.nursing.entity.Employer;

public interface EmployerBusiness {
	Optional<Employer> createEmployer(EmployerDto employerDto);

	List<Employer> getEmployers();

	Optional<Employer> getEmployerById(Long id);

	Optional<Employer> updateEmployer(Employer employer, EmployerDto newEmployerDto);

	void deleteEmployer(Employer employer);
}
