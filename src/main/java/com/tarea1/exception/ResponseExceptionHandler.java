package com.tarea1.exception;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController 
//controlador de errores transversal
@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler{


	//tipo de error manejado <-- tipo que creee
	@ExceptionHandler(ModeloNotFoundException.class)
	public final ResponseEntity<Object> manejarModeloExcepciones(ModeloNotFoundException ex, WebRequest request){

		ExceptionResponse excepResp = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(excepResp, HttpStatus.NOT_FOUND);
	}


	//para todas las excepciones
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> manejarTodasExcepciones(Exception ex, WebRequest request){

		//clase que hice
		ExceptionResponse excepResp = new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity(excepResp, HttpStatus.INTERNAL_SERVER_ERROR);
	}



	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		//obtener tdos los erroes
		String errores = "";
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			//separar errores cuando son varios
			if(!errores.equals("")) errores += ";";
			errores +=  error.getDefaultMessage(); 
		}

		ExceptionResponse excepResp = new ExceptionResponse(new Date(),"validacion fallida", errores);
		return new ResponseEntity(excepResp, HttpStatus.BAD_REQUEST);
	}



}
