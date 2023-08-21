package com.rental.nursing.entity;

import java.time.Instant;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
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
public class Employer {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	@Size(min = 2, max = 99)
	private String name;
	@NotBlank
	@Size(min = 5, max = 99)
	private String address;
	@NotBlank
	@Size(min = 2, max = 40)
	private String city;
	// Mahdollinen rajapinta josta tarkistetaan bic(y-tunnus)
	@NotBlank
	@Size(min = 2, max = 15)
	private String bic;
	@NotNull
	@Max(99999)
	private Long zipCode;
	@NotBlank
	@Size(min = 5, max = 100)
	private String sector;
	@Size(min = 10, max = 1000)
	private String info;
	@NotNull
	private Instant joined;
	private Instant edited;
	private Instant lastEmployment;
	@OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EmployerRating> ratings;
	@OneToMany(mappedBy = "employer")
	private List<Job> jobs;
	private boolean verified;
}
