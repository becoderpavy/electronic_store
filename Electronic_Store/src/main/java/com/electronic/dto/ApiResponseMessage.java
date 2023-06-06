package com.electronic.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.electronic.entites.Category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponseMessage {

	private String message;

	private boolean success;

	private HttpStatus status;

}
