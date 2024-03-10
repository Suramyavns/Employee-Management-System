package com.example.demo.exceptions;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
@RestController
public class exceptionhandler {
	@ExceptionHandler(UserNotFoundException.class)
	public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){
		ExceptionResponse ER = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(ER,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(emptyDBexception.class)
	public final ResponseEntity<Object> handleemptyDBException(Exception ex, WebRequest request){
		ExceptionResponse ER = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<Object>(ER,HttpStatus.ACCEPTED);
	}
	
	
}
