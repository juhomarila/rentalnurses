package com.rental.nursing.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NurseRatingUpdateDto {
	private Long id;
	private Integer rating;
	private String comment;
}
