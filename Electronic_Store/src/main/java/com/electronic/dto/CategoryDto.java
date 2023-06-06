package com.electronic.dto;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
	private String id;

	@NotBlank
	@Size(min = 4, message = "title must be 4 character")
	private String title;

	@NotBlank(message = "Description required")
	private String description;

	private String coverImage;
}
