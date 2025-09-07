package com.oauthprovider.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class handleGlobalException {
	
	  @ExceptionHandler(ResourseAlreadyExistException.class)
	  @ResponseStatus(HttpStatus.CONFLICT)
		@ResponseBody
		public ErrorResponse handleResourseAlreadyExistException(ResourseAlreadyExistException exception) {
			return new ErrorResponse(exception.getMessage(),HttpStatus.CONFLICT.value());
		}
	  
	  @ExceptionHandler(ResourseNotFoundException.class)
	  @ResponseStatus(HttpStatus.NOT_FOUND)
		@ResponseBody
		public ErrorResponse handleResourseNotFoundException(ResourseNotFoundException exception) {
			return new ErrorResponse(exception.getMessage(),HttpStatus.NOT_FOUND.value());
		}


}
