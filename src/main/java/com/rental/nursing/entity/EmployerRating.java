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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EmployerRating {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Min(0)
	@Max(5)
	@NotNull
	private Integer rating;
	@ManyToOne
	@JoinColumn(name = "employer_id", nullable = false)
	@NotNull
	private Employer employer;
	@ManyToOne
	@JoinColumn(name = "nurse_id", nullable = false)
	@NotNull
	private Nurse nurse;
	@Size(min = 5, max = 1000)
	private String comment;
	private Instant added;
}
