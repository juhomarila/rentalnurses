package com.rental.nursing.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	@Size(min = 2, max = 99)
	private String name;
	@NotBlank
	@Size(min = 10, max = 1000)
	private String info;
	@NotBlank
	@Size(min = 2, max = 40)
	private String city;
	@NotNull
	private Instant date;
	@NotNull
	private Double salary;
	@NotNull
	private Instant added;
	private Instant edited;
	@ManyToOne
	@JoinColumn(name = "employer_id", nullable = false)
	private Employer employer;
	// in logic if Job has nurse, it is taken
	@ManyToOne
	@JoinColumn(name = "nurse_id")
	private Nurse nurse;
	@Min(-90)
	@Max(90)
	@NotNull
	private Double latitude;
	@Min(-180)
	@Max(180)
	@NotNull
	private Double longitude;
}
