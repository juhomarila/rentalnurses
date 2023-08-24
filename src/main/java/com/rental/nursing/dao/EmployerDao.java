package com.rental.nursing.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rental.nursing.entity.Employer;

public interface EmployerDao extends JpaRepository<Employer, Long> {
	boolean existsByName(String name);

	boolean existsByBic(String bic);
}
