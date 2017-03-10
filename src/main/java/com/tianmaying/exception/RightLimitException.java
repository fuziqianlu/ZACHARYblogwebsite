package com.tianmaying.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//@ResponseStatus(value=HttpStatus.FORBIDDEN, reason="Cannot modified blogs not belonging to you")
public class RightLimitException extends RuntimeException{
	public RightLimitException(String message){
		super(message);
	}
}
