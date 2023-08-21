package com.rental.nursing.dto;

import java.time.Instant;
import java.util.List;

import com.rental.nursing.entity.Job;
import com.rental.nursing.entity.Nurse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployerDto {
	private Long id;
	private String name;
	private String address;
	private String city;
	private String bic;
	private Long zipCode;
	private String sector;
	private String info;
	private Instant joined;
	private Instant edited;
	private Instant lastEmployment;
	private Double rating;
	private List<Job> pastJobs;
	private List<Job> futureJobs;
	private List<Nurse> employedNurses;
	private boolean hasOpenJobs;
	private boolean verified;
}
