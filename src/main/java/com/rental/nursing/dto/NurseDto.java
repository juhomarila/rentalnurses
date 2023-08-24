package com.rental.nursing.dto;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NurseDto {
	private Long id;
	private String firstName;
	private String lastName;
	private String city;
	private Long zipCode;
	private String sector;
	private String info;
	private Instant joined;
	private Instant edited;
	private Instant lastEmployment;
	private Double rating;
	private List<Long> pastJobs;
	private List<Long> futureJobs;
	private List<Long> employers;
	private boolean doingJob;
	private boolean verified;
	private Instant deactivated;
}
