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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class NurseRating {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Min(0)
	@Max(5)
	@NotNull
	private Integer rating;
	@ManyToOne
	@JoinColumn(name = "employer_id", nullable = false)
	private Employer employer;
	@ManyToOne
	@JoinColumn(name = "nurse_id", nullable = false)
	private Nurse nurse;
	private Instant added;
	private Instant edited;
}
