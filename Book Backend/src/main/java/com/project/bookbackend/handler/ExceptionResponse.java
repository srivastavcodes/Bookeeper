package com.project.bookbackend.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ExceptionResponse {

	private Integer errorCode;
	private String errorBody;
	private String errDescription;

	private Map<String, String> validationErrors;
	private Map<String, String> errorMap;
}
