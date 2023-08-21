package com.rental.nursing.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NurseRatingDto {
	private Long id;
	private Integer rating;
	private Long employerId;
	private Long nurseId;
	private Instant added;
}
